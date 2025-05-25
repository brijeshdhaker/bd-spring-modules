import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SignInButton from './SignInButton';
import SignOutButton from './SignOutButton';
import { useIsAuthenticated, AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';

interface NavBarProps {
  children?: React.ReactNode;
}

const NavBar = ({children}: NavBarProps) => {
  
  const isAuthenticated = useIsAuthenticated();
  const { instance, accounts } = useMsal();
  const account = accounts[0] || {};
  const userName = account.name || "User";

  return (
    <nav className="navbar navbar-expand-lg bg-light" data-bs-theme="dark">
        <div className="container-fluid">
            <a className="navbar-brand" href="#">Navbar</a>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                    <li className="nav-item">
                        <Link className="nav-link active" aria-current="page" to="/">Home</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="/groups">Manage Tours</Link>
                    </li>
                    <li className="nav-item dropdown">
                    <a className="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Dropdown
                    </a>
                    <ul className="dropdown-menu">
                        <li>
                            <Link className="dropdown-item" to="/users">Users</Link>
                        </li>
                        <li>
                            <Link className="dropdown-item" to="/users">Manage Users</Link>
                        </li>
                        <li><hr className="dropdown-divider"/></li>
                        <li>
                            <Link className="dropdown-item" to="/users">Something else here</Link>
                        </li>
                    </ul>
                    </li>
                    <li className="nav-item">
                    <a className="nav-link disabled" aria-disabled="true">Disabled</a>
                    </li>
                </ul>
            </div>
            <div className="col-2 d-flex justify-content-start">
                <form className="d-flex" role="search">
                    <input className="form-control me-2" type="search" placeholder="Search" aria-label="Search"/>
                    <button className="btn btn-outline-success" type="submit">Search</button>
                </form>
            </div>
            <div className="col-1 d-flex justify-content-end">
                <ul className="navbar-nav">
                <li className="nav-item">
                        <a className="nav-link disabled">Hello {userName}</a>
                    </li>
                </ul>
            </div> 
            <div className="col-1 d-flex justify-content-end">
                {isAuthenticated ? <SignOutButton /> : <SignInButton />}
            </div>
        </div>
    </nav>
  )
}

export default NavBar