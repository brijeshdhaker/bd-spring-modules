import React, { useEffect, useState } from 'react';
import Dropdown from 'react-bootstrap/Dropdown';
import PropTypes from 'prop-types';
import {getScopes} from '../utils/authConfig';

import { useIsAuthenticated, AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';

function SignInButton(){

  const [dropdownOpen, setDropdownOpen] = useState(false);

  const toggle = () => setDropdownOpen((prevState) => !prevState);
  const { instance } = useMsal();

  const handleLogin = (loginType: string) => {
      if (loginType === "popup") {
          instance.loginPopup(getScopes("graph")).catch(e => {
              console.log(e);
          });
      } else if (loginType === "redirect") {
          instance.loginRedirect(getScopes("graph")).catch(e => {
              console.log(e);
          });
      }
  }

  return (
    <div>
      <Dropdown>
      <Dropdown.Toggle variant="warning" id="dropdown-basic">
        Sign In
      </Dropdown.Toggle>
      <Dropdown.Menu>
        <Dropdown.Item href="#/action-1" onClick={() => { handleLogin('popup')}}>Popup</Dropdown.Item>
        <Dropdown.Item href="#/action-2" onClick={() => { handleLogin('redirect')}}>Redirect</Dropdown.Item>
        <Dropdown.Divider />
        <Dropdown.Item eventKey="4">Separated link</Dropdown.Item>
        <Dropdown.Item href="#/action-3">Something else</Dropdown.Item>
      </Dropdown.Menu>
    </Dropdown>
    </div>
  )
}

SignInButton.propTypes = {
  direction: PropTypes.string,
};

export default SignInButton
