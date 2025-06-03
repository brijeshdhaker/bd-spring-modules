import axios from "axios";

// 'Access-Control-Allow-Origin': '*',
// 'Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS'

const getBaseURL = () => {
    if(import.meta.env.VITE_API_BASE_URL) {
        return import.meta.env.VITE_API_BASE_URL;
    }
    return '/'; 
}

const Client = axios.create({
    baseURL: getBaseURL(),
    withCredentials: true,
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'X-Src-ID': 'SANDBOX-WB'
    }
});

export const ConfigService = {
    getClient: () => Client,
    setBaseURL: (url: string) => {
        Client.defaults.baseURL = url;
    },
    setHeader: (key: string, value: string) => {
        Client.defaults.headers.common[key] = value;
    },
    removeHeader: (key: string) => {
        delete Client.defaults.headers.common[key];
    }
};

const _config = {
    clientId: "7eed8ec9-c714-43f6-8318-710448a55a85",
    authority: "https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148",
    redirectUri: "http://localhost:3000",
};

const loadConfig = async () => {
    if(!_config) {
        await Client.get('/config').then(response => (response.data));   
    }
    return _config;
    /*
    new Promise<any>((resolve, reject) => {
        setTimeout(() => {
            resolve(_config);
        }, 2000);
    });
    */
};

export { loadConfig }