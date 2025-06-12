import { getScopes,} from "../utils/authConfig";
import { msalInstance } from "../utils/AuthenticationService";

/**
 * Attaches a given access token to a MS Graph API call. Returns information about the user
 * @param accessToken 
 */
export async function callRestApi(accessToken) {
    const headers = new Headers();
    const bearer = `Bearer ${accessToken}`;

    headers.append("Authorization", bearer);
    headers.append("X-Src-ID", "SANDBOX-WB");
    headers.append("X-Tenant-Id", "DC-R0");
    headers.append("Accept", "*");
    headers.append("Content-Type", "application/json");
    headers.append("Access-Control-Allow-Origin", "*");

    const options = {
        method: "GET",
        headers: headers
    };

    return fetch("/api/v1/users", options)
        .then(response => response.json())
        .catch(error => console.log(error));
}


/** **/
async function getAccessToken(){
    const instance = msalInstance;
    //const { instance, accounts } = useMsal();
    let scopes = getScopes("api");
    const apiTokenRequest = { ...scopes, account: instance.getAllAccounts()[0]}
    if (apiTokenRequest.account) {
        const accessTokenResponse = await instance.acquireTokenSilent(apiTokenRequest)
        return `Bearer ${accessTokenResponse.accessToken}`;
    }
    return null;
}

const fetchRestCall = async (req) => {
    return getAccessToken().then(async (accessToken : string | null ) => {
        req.options.headers.append("Authorization", `${accessToken}`);
        const targetUrl = 'http://localhost:8100';
        return fetch(req.endpoint, req.options);
    })
    
}

const getDefaultHeaders = () => {
    const headers = new Headers();
//    headers.append("X-Tenant-Id", "GLOBAL");
    headers.append("X-Src-ID", "SANDBOX-WB");
//    headers.append("Accept", "*");
//    headers.append("Content-Type", "application/json");
//    headers.append("Access-Control-Allow-Origin", "*");
    return headers;
}

const get = async (endpoint: string) => {
    const options = { 
        method: "GET", 
        headers: getDefaultHeaders() 
    };
    const req = { endpoint: endpoint, options: options}
    return fetchRestCall(req);
}

const post = async (endpoint: string, data: any) => {
    
    // Automatically converted to "username=example&password=password"
    //body: new URLSearchParams({ username: "example", password: "password" }),

    const options = {
        method: "POST",
        headers: getDefaultHeaders(),
        body: JSON.stringify(data)
    };
    const req = { endpoint: endpoint, options: options}
    return fetchRestCall(req);
}

export { 
    get,
    post
};