import React, {useContext} from 'react';
import {Navbar, NavbarBrand} from "reactstrap";
import CreateModal from "./CreateModal";
import {AuthContext} from "../App";
import UserInfo from "./UserInfo";

const Header = () => {
  const {email} = useContext(AuthContext);
  function isAuth(element) {
    if (email !== null) {
      return element;
    }
  }

  return (
      <header>
        <Navbar color="secondary" fixed="top">
          <div className="d-flex">
            <NavbarBrand tag="span"
                         className="mb-0 h1 text-light text-opacity-50">Admin
              UI</NavbarBrand>
            {isAuth(<CreateModal/>)}
          </div>
          {isAuth(<UserInfo email={email}/>)}
        </Navbar>
      </header>
  );
};

export default Header;