import React from 'react'
import NavBar from "./NavBar";
import PageContent from './PageContent';
import GroupList from './GroupList';
import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';

const Groups = () => {
  return (
    <>
    <NavBar></NavBar>
    <GroupList></GroupList>
    </>
  )
}

export default Groups