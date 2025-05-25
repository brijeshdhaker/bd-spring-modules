import React, { useState } from 'react';
import { Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem, NavLink } from 'reactstrap';
import { Link } from 'react-router-dom';
import { useIsAuthenticated } from '@azure/msal-react';
import { SignInButton } from './SignInButton';
import { SignOutButton } from './SignOutButton';

const AppNavbar = () => {

    const isAuthenticated = useIsAuthenticated();
    const [isOpen, setIsOpen] = useState(false);

  return (
    <Navbar color="dark" dark expand="md">
      <NavbarBrand tag={Link} to="/">Home</NavbarBrand>
      <NavbarToggler onClick={() => { setIsOpen(!isOpen) }}/>
      <Collapse isOpen={isOpen} navbar>
        <Nav className="justify-content-end" style={{width: "100%"}} navbar>
          <NavItem>
            <NavLink href="http://localhost:8100">@oktadev</NavLink>
          </NavItem>
          <div className="collapse navbar-collapse justify-content-end">
              {isAuthenticated ? <SignOutButton /> : <SignInButton />}
          </div>
        </Nav>
      </Collapse>
    </Navbar>
  );
};

export default AppNavbar;