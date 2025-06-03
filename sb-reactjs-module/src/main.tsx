import React from 'react'
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { CookiesProvider } from 'react-cookie';
import { MsalProvider } from "@azure/msal-react";
import App from './App.tsx'
import { msalInstance } from './utils/AuthenticationService';

// const msalInstance = new PublicClientApplication(msalConfig);

createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <CookiesProvider>
      <MsalProvider instance={msalInstance}>
        <App />
      </MsalProvider>
    </CookiesProvider>
  </React.StrictMode>
)
