import React, { useState } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { PageLayout } from './components/PageLayout';
import GroupList from './components/GroupList';
import GroupEdit from './components/GroupEdit';
import UserList from './components/UserList';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route exact path="/" element={<Home/>}/>
        <Route path='/groups' exact={true} element={<GroupList/>}/>
        <Route path='/users' exact={true} element={<UserList/>}/>
        <Route path='/groups/:id' element={<GroupEdit/>}/>
      </Routes>
    </Router>
  )
}

export default App;