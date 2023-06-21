import React from 'react';
import {Button} from "reactstrap";

const UserInfo = (props) => {
  function logout() {
    sessionStorage.removeItem("user-email");
    sessionStorage.removeItem("user-token");
    window.location.reload();
  }

  return (
      <div className="d-flex align-items-center">
        <h6 className="me-3 mb-0 text-light">{props.email}</h6>
        <Button onClick={logout} size="sm">Logout</Button>
      </div>
  );
};

export default UserInfo;