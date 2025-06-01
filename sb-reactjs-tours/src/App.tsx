import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import { useIsAuthenticated, AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';
// Importing the Bootstrap CSS
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import AppNavbar from './components/AppNavbar';
import { BrowserRouter, Route, Router, Routes } from 'react-router';
import Layout from './pages/Layout';
import Home from './pages/Home';
import Blogs from './pages/Blogs';
import Contact from './pages/Contact';
import NoPage from './pages/NoPage';
import Groups from './pages/Groups';
import Users from './pages/Users';
import GroupEdit from './pages/GroupEdit';

function App() {
  const [count, setCount] = useState(0)
  const isAuthenticated = useIsAuthenticated();
  /*
  <BrowserRouter>
        <Routes>
          <Route path="/" element={<AppNavbar />}>
            <Route index element={<Home />} />
            <Route path="blogs" element={<Blogs />} />
            <Route path="contact" element={<Contact />} />
            <Route path="*" element={<NoPage />} />
        </Route>
        </Routes>
      </BrowserRouter>
  */

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<AppNavbar />}>
            <Route path="home" index element={<Home />} />
            <Route path="groups" element={<Groups />} />
            <Route path="group/:id" element={<GroupEdit />} />
            <Route path="users" element={<Users />} />
            <Route path="contact" element={<Contact />} />
            <Route path="blogs" element={<Blogs />} />
            <Route path="contact" element={<Contact />} />
            <Route path="*" element={<NoPage />} />
        </Route>
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
