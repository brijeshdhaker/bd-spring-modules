import axios from "axios";
import { getScopes,} from "../utils/authConfig";
import { msalInstance } from "../utils/AuthenticationService";


const getBaseURL = () => {
    if(import.meta.env.VITE_API_BASE_URL) {
        return import.meta.env.VITE_API_BASE_URL;
    }
    return '/'; 
}

const RestClient = axios.create({
    baseURL: getBaseURL(),
    withCredentials: true,
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'X-Src-ID': 'SANDBOX-WB',
        'X-Tenant-Id': 'GLOBAL',
//        "Access-Control-Allow-Origin": "*"
    }
});

/**
 * Attaches a given access token to a MS Graph API call. Returns information about the loggedin user
 */
export async function loadUserDetails() {
    const instance = msalInstance;
    //const { instance, accounts } = useMsal();
    let scopes = getScopes("graph");
    const graphTokenRequest = { ...scopes, account: instance.getAllAccounts()[0]}
    if (graphTokenRequest.account) {
        const accessTokenResponse = await instance.acquireTokenSilent(graphTokenRequest)
        return accessTokenResponse.account;
    }
    return null;
    
}

/**
 * Attaches a given access token to a MS Graph API call. Returns information about the loggedin user
 */
export async function loadProfile() {
    const instance = msalInstance;
    //const { instance, accounts } = useMsal();
    let scopes = getScopes("graph");
    const graphTokenRequest = { ...scopes, account: instance.getAllAccounts()[0]}
    if (graphTokenRequest.account) {
        const accessTokenResponse = await instance.acquireTokenSilent(graphTokenRequest)
        return accessTokenResponse.account;
    }
    return null;
    
}

export const profile = async () => {
    let scopes = getScopes("graph");
    const graphTokenRequest = { ...scopes, account: msalInstance.getAllAccounts()[0]}
    const accessToken = await msalInstance.acquireTokenSilent(graphTokenRequest);
    if (accessToken) {
        RestClient.defaults.headers.common['Authorization'] = `Bearer ${accessToken.accessToken}`;
        return await RestClient.get('https://graph.microsoft.com/v1.0/me');
    }
    //return await authenticatedRestCall(() => RestClient.get("https://graph.microsoft.com/v1.0/me"));
}

export const authorize = async () => {
    /*
    const accessToken = await msalInstance.acquireTokenSilent(getScopes());

    if (accessToken) {
        RestClient.defaults.headers.common['Authorization'] = `Bearer ${accessToken.accessToken}`;
    } else {
        throw new Error("No access token available");
    }
    
    return RestClient.get('/authorize');
    */
    
    return new Promise<any>((resolve, reject) => {
        setTimeout(() => {
            resolve({
                status: 200,
                data:{
                    id: "1",
                    fullName: "Brijesh K Dhaker",
                    name: "Brijesh K.",
                    surName: "Dhaker",
                    displayName: "Brijesh D.",
                    email: "brijeshdhaker@gmail.com",
                    mobile: "+91-9820937445",
                    roles: ["admin", "user","guest", "editor"]
                }   
            });
            reject({
                status: 401,
                data:{} 
            });
        }, 2000);
    });
    
    
    //return await get('/authorize');

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

const authenticatedRestCall = async (restApiCall) => {

    return getAccessToken().then(async (token : string | null ) => {
        RestClient.defaults.headers.common['Authorization'] = token;
        return await restApiCall()
    })
    
}

/** */

const get = async (endpoint: string) => {
    return await authenticatedRestCall(() => RestClient.get(endpoint));
}
const post = async (endpoint: string, data: any) => {
    return authenticatedRestCall(() => RestClient.post(endpoint, data));
}
const put = async (endpoint: string, data: any) => {
    return authenticatedRestCall(() => RestClient.put(endpoint, data));
}
const del = async (endpoint: string) => {
    return authenticatedRestCall(() => RestClient.delete(endpoint));
}
const patch = async (endpoint: string, data: any) => {
    return authenticatedRestCall(() => RestClient.patch(endpoint, data));
}
const head = async (endpoint: string) => {
    return authenticatedRestCall(() => RestClient.head(endpoint));
}
const options = async (endpoint: string) => {
    return authenticatedRestCall(() => RestClient.options(endpoint));
}

export { 
    RestClient,
    get,
    post,
    put,
    del,
    patch,
    head,
    options
};
// Export the RestClient for direct access if needed
