import React from 'react'
import { useMsal } from "@azure/msal-react";

const SignOutButton = () => {

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
        <button type="button" className="btn btn-secondary" onClick={() => handleLogout("redirect")}>Sign Out</button>
    )
}

export default SignOutButton