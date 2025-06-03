import React, { useEffect, useState } from 'react';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { useMsal} from '@azure/msal-react';
import SignOutButton from './SignOutButton';
import SignInButton from './SignInButton';
import { Outlet, Link } from "react-router-dom";
import { loadUserDetails } from '../services/ApiServices';

interface AppNavbarProps {
    isAuthenticated: boolean;
    heading: string;
    onSelectItem: (item: string) => void;
}

function AppNavbar({isAuthenticated, heading, onSelectItem}: AppNavbarProps) {
  
  //Hook
  const[user, setUser] = useState({ id: "", name: "", email: "", roles:[]});
  const[dispaly, setDispaly] = useState(false);
  const { instance, accounts } = useMsal();
  const account = accounts[0] || {};
  const userName = account.name || "User";
  const luser = instance.getAllAccounts()[0] || "";
  
  // Event Handler
  const handelClick = (event : MouseEvent ) => console.log(event);
  const handelNavClick = (eventKey, event) => console.log(eventKey, event);

  //
  useEffect(() => {
    if(!user.id){
      loadUserDetails().then((account) => {
        console.log(account);
        setUser({ 
            id: account.localAccountId, 
            name: account.name, 
            email: account.username,
            roles: ["admin","editor","HR","IT"]
        });
        setDispaly(true);
      }).catch(()=> {
        console.log("Error while loading user details.")
      });
    }
  },[user]);

  //
  const getNavLinks = () => {

      return (isAuthenticated ? <Navbar.Collapse id="navbarScroll">
            <Nav className="me-auto my-2 my-lg-0" style={{ maxHeight: '100px' }} navbarScroll >
              <Link className='nav-link' to="/home">Home</Link>
              <Link className='nav-link' to="/groups">Groups</Link>
              <Link className='nav-link' to="/users">Users</Link>
              <Link className='nav-link' to="/blogs">Blogs</Link>
              <NavDropdown title="Action Menu" id="navbarScrollingDropdown" onSelect={handelNavClick}>
                <NavDropdown.Item href="#action3" eventKey="AAA">Action-1</NavDropdown.Item>
                <NavDropdown.Item href="#action4" eventKey="BBB">Action-2</NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item href="#action5" eventKey="CCC" onClick={handelClick}>Action-2</NavDropdown.Item>
                <Link className='dropdown-item' to="/nothing-here">Nothing Here</Link>
              </NavDropdown>
              <Nav.Link href="/contact">Contact US</Nav.Link>
            </Nav>
            <Form className="d-flex">
              <Form.Control id="navsearch" name="navsearch" type="search" placeholder="Search" className="me-2" aria-label="Search" defaultValue={ 'Hello ' + userName}/>
            </Form>
            <div className="col-1 d-flex justify-content-end"><SignOutButton /></div>
          </Navbar.Collapse>
          :
          <Navbar.Collapse id="navbarScroll">
            <Nav className="me-auto my-2 my-lg-0" style={{ maxHeight: '100px' }} navbarScroll >
              {/*
              <Nav.Link href="/home">Link</Nav.Link>
              */}
            </Nav>
            <div className="col-1 d-flex justify-content-end"><SignInButton/></div>
          </Navbar.Collapse>);
  }

  //
  return (
    <>
      <Navbar expand="lg" className="bg-body-tertiary" bg="dark" data-bs-theme="dark">
        <Container fluid>
          <Navbar.Brand href="/">
              <img alt="" src="/react.svg" width="30" height="30" className="d-inline-block align-top"/>{'  ' + heading}
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarScroll" />
          {getNavLinks()}
        </Container>
      </Navbar>
      <Outlet />
    </>
  );
}

export default AppNavbar;