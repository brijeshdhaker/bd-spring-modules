import { useState } from 'react'
import { useIsAuthenticated} from '@azure/msal-react';
// Importing the Bootstrap CSS
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import AppNavbar from './components/AppNavbar';
import { BrowserRouter, Route, Routes } from 'react-router';
import Home from './pages/Home';
import Blogs from './pages/Blogs';
import Contact from './pages/Contact';
import NoPage from './pages/NoPage';
import Groups from './pages/Groups';
import Users from './pages/Users';
import GroupEdit from './pages/GroupEdit';
import UserEdit from './pages/UserEdit';

function App() {

  //Hook
  const [count, setCount] = useState(0)
  const isAuthenticated = useIsAuthenticated();


  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<AppNavbar isAuthenticated={isAuthenticated} heading='SANDBOX-WB' onSelectItem={() => {}}/>}>
            <Route path="home" index element={<Home />} />
            <Route path="groups" element={<Groups />} />
            <Route path="group/:id" element={<GroupEdit />} />
            <Route path="users" element={<Users />} />
            <Route path="users/:id" element={<UserEdit />} />
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
