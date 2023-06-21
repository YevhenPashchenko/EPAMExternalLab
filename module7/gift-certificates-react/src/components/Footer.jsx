import React from 'react';
import {Navbar, NavbarBrand} from "reactstrap";

const Footer = () => {
  return (
      <footer>
        <Navbar color="secondary" fixed="bottom">
          <div className="d-flex container-fluid justify-content-center">
            <NavbarBrand tag="span"
                         className="mb-0 h1 text-light text-opacity-50">2023</NavbarBrand>
          </div>
        </Navbar>
      </footer>
  );
};

export default Footer;