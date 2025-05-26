import React, { MouseEvent, useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Message from "./components/Message";  
import ListGroup from "./components/ListGroup";
import Alert from "./components/Alert";
import Button from "./components/Button";
import PageContent from "./components/PageContent";
import Home from "./components/Home";
import Groups from "./components/Groups";
import GroupEdit from './components/GroupEdit';
import UsersPage from "./components/UsersPage";

import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';

export default function App() {
    return (
      <>
        <Router>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/index.html" element={<Home/>}/>
                <Route exact={true} path='/groups' element={<Groups/>}/>
                <Route exact={true} path='/groups/:id' element={<GroupEdit/>}/>
                <Route exact={true} path='/users' element={<UsersPage/>}/>
            </Routes>
        </Router>
      </>
    );
}