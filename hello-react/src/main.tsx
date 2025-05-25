import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { PublicClientApplication } from '@azure/msal-browser';
import { MsalProvider } from '@azure/msal-react';
import { msalConfig } from './helpers/authConfig';
import { CookiesProvider } from 'react-cookie';

const msalInstance = new PublicClientApplication(msalConfig);

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
     <CookiesProvider>
      <MsalProvider instance={msalInstance}>
        <App />
      </MsalProvider>
    </CookiesProvider>
  </React.StrictMode>,
)
