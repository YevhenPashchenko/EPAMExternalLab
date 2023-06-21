import React, {useContext, useState} from 'react';
import {
  Alert,
  Button,
  Modal,
  ModalBody, ModalFooter,
  ModalHeader
} from "reactstrap";
import {AuthContext} from "../App";

const DeleteModal = (props) => {
  const {token} = useContext(AuthContext);
  const [show, setShow] = useState(false);
  const [showError, setShowError] = useState(false);
  const [message, setMessage] = useState("");

  const toggle = () => {
    setShow(!show);
  };

  const toggleError = () => {
    setShowError(!showError);
  }

  const deleteCertificate = () => {
    fetch(`http://localhost:8000/gift-certificates/${props.name}`, {
      method: "DELETE",
      headers: {
        Authorization: "Bearer " + token.access_token
      }
    }).then(res => {
      if (res.status === 200) {
        window.location.reload();
      } else {
        res.json().then(error => {
          setMessage(error.message);
          toggleError();
        });
      }
    });
  }

  return (
      <div>
        <Button color="danger" onClick={toggle}>Delete</Button>
        <Modal isOpen={show} toggle={toggle}>
          <ModalHeader toggle={toggle}>Delete confirmation</ModalHeader>
          <ModalBody>
            <Alert color="danger" isOpen={showError}
                   toggle={toggleError}>{message}</Alert>
            <div>Do you really want to delete certificate with title
              = {props.name}</div>
          </ModalBody>
          <ModalFooter className="d-flex justify-content-center">
            <Button onClick={deleteCertificate} color="danger">Yes</Button>
            <Button onClick={toggle} color="light">Cancel</Button>
          </ModalFooter>
        </Modal>
      </div>
  );
};

export default DeleteModal;