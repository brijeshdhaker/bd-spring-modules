import React, { useEffect, useState } from 'react';
import { useMsal } from "@azure/msal-react";
import Dropdown from 'react-bootstrap/Dropdown';
import PropTypes from 'prop-types';

function SignOutButton({...args}){

  const [dropdownOpen, setDropdownOpen] = useState(false);

  const toggle = () => setDropdownOpen((prevState) => !prevState);

  const { instance } = useMsal();

  const handleLogout = (logoutType: string) => {
      if (logoutType === "popup") {
          instance.logoutPopup({
              postLogoutRedirectUri: "/",
              mainWindowRedirectUri: "/"
          });
      } else if (logoutType === "redirect") {
          instance.logoutRedirect({
              postLogoutRedirectUri: "/",
          });
      }
  }

  return (
    <div>
      <Dropdown>
      <Dropdown.Toggle variant="warning" id="dropdown-basic">
        Sign Out
      </Dropdown.Toggle>
      <Dropdown.Menu>
        <Dropdown.Item href="#/action-1" onClick={() => { handleLogout('popup')}}>Popup</Dropdown.Item>
        <Dropdown.Item href="#/action-2" onClick={() => { handleLogout('redirect')}}>Redirect</Dropdown.Item>
        <Dropdown.Divider />
        <Dropdown.Item eventKey="4">Separated link</Dropdown.Item>
        <Dropdown.Item href="#/action-3">Something else</Dropdown.Item>
      </Dropdown.Menu>
    </Dropdown>
    </div>
  )
}

SignOutButton.propTypes = {
  direction: PropTypes.string,
};

export default SignOutButton