import React, { useState, useEffect } from 'react';
import { get, loadUserDetails, profile } from '../services/ApiServices';
import Button from 'react-bootstrap/esm/Button';
import Table from 'react-bootstrap/Table';

const Home = () => {
  
  const[user, setUser] = useState({ id: "", name: "", email: "", roles:[]});
  const[dispaly, setDispaly] = useState(false);

  function toggelDisplay() {
    setDispaly(!dispaly);
  }
  
  useEffect(() => {
    //
    if(!user.id){
      loadUserDetails().then((account) => {
        setUser({ 
            id: account.localAccountId, 
            name: account.name, 
            email: account.username,
            roles: ["admin","editor","HR","IT"]
        });
        setDispaly(true);
      }).catch(()=> {
        console.log("Error while loading user details.")
      })
    }

    if(user.id){
      profile().then((response) => {
          console.log(response.data.userPrincipalName);
      }).catch(()=> {

      });
    }

    if(user.id){
      get('/api/v1/users').then((response) => {
          console.log(response.data);
      }).catch(()=> {

      });
    }
    
    //
    /*
    if(!user.id){
        // Silently acquires an access token which is then attached to a request for MS Graph data
        instance.acquireTokenSilent({...loginRequestScope,account: accounts[0],}).then((response) => {
          RestClient.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`;
          RestClient.get("https://graph.microsoft.com/v1.0/me").then((response) => {
            let result = response.data;
            let user = {
              id: result.id,
              name: result.displayName,
              givenName: result.givenName,
              surname: result.surname,
              roles: ["admin","editor"],
              isAuthorized: true,
              isLoaded: true,
              isRequired: true,
              isUser: true
            };
            setUser(user);
          });
      //    
      });
    }
    */
  }, [user]); // Effect runs when user details changes
  
  function displayRoles() {
      return user.roles.map((role)=> role+", ");
  }

  return (
    <div>
      <h3>@ Home Page </h3>
      {dispaly && 
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>#</th>
              <th>Key</th>
              <th>ValueLast Name</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>1</td>
              <td>ID</td>
              <td>{user.id}</td>
            </tr>
            <tr>
              <td>2</td>
              <td>Name</td>
              <td>{user.name}</td>
            </tr>
            <tr>
              <td>3</td>
              <td>Email</td>
              <td>{user.email}</td>
            </tr>
            <tr>
              <td>4</td>
              <td>Roles</td>
              <td>{displayRoles()}</td>
            </tr>
            </tbody>
        </Table>
      }
      <Button variant="secondary" onClick={toggelDisplay}>{ dispaly ? 'Hide Details' : 'Display Details'}</Button>
    </div>
  )
}

export default Home