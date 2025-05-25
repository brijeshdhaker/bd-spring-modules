import React from 'react'
import NavBar from "./NavBar";
import PageContent from './PageContent';
import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';

// rafce

const MainContent = () => {
    return (
        <div className="App">
            <AuthenticatedTemplate>
                <PageContent/>
            </AuthenticatedTemplate>

            <UnauthenticatedTemplate>
                <h5 className="card-title">Please sign-in to see your profile information.</h5>
            </UnauthenticatedTemplate>
        </div>
    );
};

const Home = () => {
  return (
    <>
    <NavBar></NavBar>
    <div>Home Page</div>
    <MainContent/>
    </>
  )
}

export default Home