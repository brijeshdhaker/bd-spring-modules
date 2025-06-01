
import { LogLevel } from "@azure/msal-browser";
// import { loadConfig } from "../services/ConfigService";

export const getMsalConfig = async () => {
    
    let config = {
        clientId: "7eed8ec9-c714-43f6-8318-710448a55a85",
        authority: "https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148",
        redirectUri: "http://localhost:3000",
    };
    
    if (!config) {
        throw new Error("Configuration not loaded");
    }
    return {
        auth: {
            clientId: config.clientId,
            authority: config.authority,
            redirectUri: config.redirectUri,
        },
        cache: {
            cacheLocation: "sessionStorage",
            storeAuthStateInCookie: false,
        },
        system: {
            loggerOptions: {
                loggerCallback: (level, message, containsPii) => {
                    if (containsPii) {
                        return;
                    }
                    switch (level) {
                        case LogLevel.Error:
                            console.error(message);
                            break;
                        case LogLevel.Info:
                            console.info(message);
                            break;
                        case LogLevel.Verbose:
                            console.debug(message);
                            break;
                        case LogLevel.Warning:
                            console.warn(message);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    };  
}

export const getScopes = () => {
    //let config = await loadConfig();
    return { scopes: ["api://7f1cf4d7-ca24-47c2-bf17-61a8a796679e/User.Read"] };
};

export const getUserScopes = () => {
    //let config = await loadConfig();
    return { scopes: ["api://7f1cf4d7-ca24-47c2-bf17-61a8a796679e/User.Read"] };
};

export const loginRequestScope = { scopes: ["https://graph.microsoft.com/User.Read"] };