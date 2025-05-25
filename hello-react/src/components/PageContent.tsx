import React, { useState } from 'react';
import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';
import { loginRequest, apiLoginRequest, graphConfig } from '../helpers/authConfig';

const PageContent = () => {

    const { instance, accounts } = useMsal();
    const [pageData, setPageData] = useState(null);

    
    async function callMsGraph(accessToken: string) {
        const headers = new Headers();
        const bearer = `Bearer ${accessToken}`;

        headers.append("Authorization", bearer);

        const options = {
            method: "GET",
            headers: headers
        };

        return fetch(graphConfig.graphMeEndpoint, options)
            .then(response => response.json())
            .catch(error => console.log(error));
    }

    function RequestProfileData() {
        // Silently acquires an access token which is then attached to a request for MS Graph data
        instance
            .acquireTokenSilent({
                ...loginRequest,
                account: accounts[0],
            })
            .then((response) => {
                console.log("Graph API Token : " + response.accessToken);
                callMsGraph(response.accessToken).then((response) => setPageData(response));
            });
    }

    return (
        <>
                <p className="profileContent">You are signed in with the following account:</p> 
                <h5 className="profileContent">Welcome {accounts[0].name}</h5>
        </>
    )
}

export default PageContent