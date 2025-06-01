import { PublicClientApplication } from "@azure/msal-browser";
import { getMsalConfig } from "./authConfig";

export const msalInstance = new PublicClientApplication(await getMsalConfig());
