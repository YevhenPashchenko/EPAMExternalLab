import React, {useState, useEffect, useContext} from 'react';
import {
  Alert,
  Button, Col,
  Form, FormFeedback,
  FormGroup, Input, InputGroup, Label,
  Modal,
  ModalBody, ModalFooter,
  ModalHeader
} from "reactstrap";
import {AuthContext} from "../App";

const EditModal = (props) => {
  const {token} = useContext(AuthContext);
  const initState = {
    value: null,
    invalidClassName: "",
    isValid: true
  };
  const [show, setShow] = useState(false);
  const [showError, setShowError] = useState(false);
  const [title, setTitle] = useState(initState);
  const [description, setDescription] = useState(initState);
  const [duration, setDuration] = useState(initState);
  const [price, setPrice] = useState(initState);
  const [tags, setTags] = useState({
    tags: props.certificate.tags,
    invalidClassName: "",
    isValid: true
  });
  const [pass, setPass] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (title.isValid && description.isValid && duration.isValid
        && price.isValid) {
      setPass(true);
    } else {
      setPass(false);
    }
  }, [description.isValid, duration.isValid, price.isValid, title.isValid]);

  const toggle = () => {
    setTitle(initState);
    setDescription(initState);
    setDuration(initState);
    setPrice(initState);
    setShow(!show);
  };

  const toggleError = () => {
    setShowError(!showError);
  }

  const validation = (e, setFunction, oldValue, ...params) => {
    const input = e.currentTarget;
    if (input.value.length === 0 || input.value.length < params[0]
        || input.value.length > params[1]) {
      if (input.value.length === 0) {
        input.value = oldValue;
        setFunction({
          value: null,
          invalidClassName: "is-valid",
          isValid: true
        })
      } else {
        setFunction({
          value: null,
          invalidClassName: "is-invalid",
          isValid: false
        })
      }
    } else {
      setFunction({
        value: input.value,
        invalidClassName: "is-valid",
        isValid: true
      })
    }
  }

  const addTag = (e) => {
    const input = e.currentTarget.previousElementSibling;
    if (input.value.length < 3 || input.value.length > 15) {
      setTags({...tags, invalidClassName: "is-invalid"});
    } else {
      const arr = tags.tags;
      arr.push({name: input.value});
      setTags({...tags, tags: arr, invalidClassName: ""});
      input.value = "";
    }
  }

  const deleteTag = (e) => {
    const value = e.currentTarget.parentElement.innerText;
    const arr = tags.tags.filter(tag => tag.name !== value);
    setTags({...tags, tags: arr});
  }

  const clearInvalidate = (e) => {
    const value = e.currentTarget.value;
    if (value === "" || (value.length > 2 && value.length < 16)) {
      setTags({...tags, invalidClassName: ""});
    } else {
      setTags({...tags, invalidClassName: "is-invalid"});
    }
  }

  const update = () => {
    if (!pass) {
      return;
    }
    const params = {
      name: title.value,
      description: description.value,
      duration: duration.value,
      price: price.value,
      tags: tags.tags
    }
    fetch(`http://localhost:8000/gift-certificates/${props.certificate.name}`, {
      method: "PATCH",
      headers: {
        Authorization: "Bearer " + token.access_token,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(params)
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
        <Button color="warning" onClick={toggle}>Edit</Button>
        <Modal isOpen={show} toggle={toggle}>
          <ModalHeader toggle={toggle}>Edit certificate with title
            = {props.certificate.name}</ModalHeader>
          <ModalBody>
            <Alert color="danger" isOpen={showError}
                   toggle={toggleError}>{message}</Alert>
            <Form>
              <FormGroup row>
                <Label for="title" sm={2}>Title</Label>
                <Col sm={10}>
                  <Input onBlur={(e) => validation(e, setTitle,
                      props.certificate.name, 6, 30)}
                         className={title.invalidClassName}
                         defaultValue={props.certificate.name} type="text"
                         id="title" aria-invalid={title.isValid}/>
                  <FormFeedback>Title must be more than 6 and less than 30
                    characters</FormFeedback>
                </Col>
              </FormGroup>
              <FormGroup row>
                <Label for="description" sm={2}>Description</Label>
                <Col sm={10}>
                  <Input onBlur={(e) => validation(e, setDescription,
                      props.certificate.description, 12, 1000)}
                         className={description.invalidClassName}
                         defaultValue={props.certificate.description}
                         type="textarea" id="description"
                         aria-invalid={description.isValid}/>
                  <FormFeedback>Description must be more than 12 and less than
                    1000 characters</FormFeedback>
                </Col>
              </FormGroup>
              <FormGroup row>
                <Label for="duration" sm={2}>Duration</Label>
                <Col sm={10}>
                  <Input onBlur={(e) => validation(e, setDuration,
                      props.certificate.duration)}
                         className={duration.invalidClassName}
                         defaultValue={props.certificate.duration} type="number"
                         id="duration" min={0} aria-invalid={duration.isValid}/>
                  <FormFeedback>Duration cannot be empty</FormFeedback>
                </Col>
              </FormGroup>
              <FormGroup row>
                <Label for="price" sm={2}>Price</Label>
                <Col sm={10}>
                  <Input onBlur={(e) => validation(e, setPrice,
                      props.certificate.price)}
                         className={price.invalidClassName}
                         defaultValue={props.certificate.price} type="number"
                         id="price" min={1} aria-invalid={price.isValid}/>
                  <FormFeedback>Price cannot be empty</FormFeedback>
                </Col>
              </FormGroup>
              <FormGroup row>
                <Label for="tags" sm={2}>Tags</Label>
                <Col sm={10}>
                  <InputGroup>
                    <Input onBlur={clearInvalidate}
                           className={tags.invalidClassName} type="text"
                           id="tags"/>
                    <Button onClick={addTag} color="primary">Add</Button>
                    <FormFeedback>Tag is optional, but tag name should be not
                      less than 3 and greater than 15 characters</FormFeedback>
                  </InputGroup>
                  <div className="d-flex gap-1 flex-wrap">
                    {
                      tags.tags.map((tag) => {
                        return <Button key={tag.name} color="white"
                                       className="border-black" size="sm">
                          {tag.name}<i onClick={deleteTag}
                                       className="bi bi-x-lg"/>
                        </Button>
                      })
                    }
                  </div>
                </Col>
              </FormGroup>
            </Form>
          </ModalBody>
          <ModalFooter className="d-flex justify-content-center">
            <Button onClick={update} color="primary">Save</Button>
            <Button onClick={toggle} color="light">Cancel</Button>
          </ModalFooter>
        </Modal>
      </div>
  );
};

export default EditModal;