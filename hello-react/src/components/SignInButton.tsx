import React from 'react'

import { useMsal } from "@azure/msal-react";
import { loginRequest } from "../helpers/authConfig";

const SignInButton = () => {
  
  const { instance } = useMsal();

    const handleLogin = (loginType : string) => {
        if (loginType === "popup") {
            instance.loginPopup(loginRequest).catch(e => {
                console.log(e);
            });
        } else if (loginType === "redirect") {
            instance.loginRedirect(loginRequest).catch(e => {
                console.log(e);
            });
        }
    }

  return (
    <button type="button" className="btn btn-warning" onClick={() => handleLogin("redirect")}>Sign In</button>
  )
}

export default SignInButton