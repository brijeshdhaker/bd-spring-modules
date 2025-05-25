import React from 'react'
import NavBar from "./NavBar";
import PageContent from './PageContent';
import UserList from './UserList';
import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';

const UsersPage = () => {
  return (
    <>
    <NavBar></NavBar>
    <UserList></UserList>
    </>
  )
}

export default UsersPage