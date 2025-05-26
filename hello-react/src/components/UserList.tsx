import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import NavBar from "./NavBar";
import { Link } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';
import { loginRequest, apiLoginRequest, graphConfig } from '../helpers/authConfig';
import axios from 'axios';

const UserList = () => {

  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(false);
  const [cookies] = useCookies(['XSRF-TOKEN']);
  const {instance, accounts } = useMsal();
  const [pageData, setPageData] = useState(null);
  const [accessToken, setAccessToken] = useState(String);


  const loadUserData = () => {
    setLoading(true);
    // Silently acquires an access token which is then attached to a request for MS Graph data}
    if(!accessToken){

        instance.acquireTokenSilent({
            scopes: ["api://7f1cf4d7-ca24-47c2-bf17-61a8a796679e/User.Read"],
            account: accounts[0]
        }).then((response) => {

          //console.log("Access Token acquired silently: ", response.accessToken);
          //setAccessToken(response.accessToken);

          axios.defaults.withCredentials = true;
          axios.defaults.headers.common['X-XSRF-TOKEN'] = cookies['XSRF-TOKEN'];
          axios.defaults.headers.common['Accept'] = 'application/json';
          axios.defaults.headers.common['Content-Type'] = 'application/json';
          axios.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`;
          //axios.defaults.baseURL = '/api/v1';
          
          /*
          try {
            const res = axios.get('/users');
            console.log("User data fetched successfully:", res);
          } catch (error) {
            console.error("Error fetching user data:", error);
          }
          */

          axios.get('/api/v1/users')
            .then(response => {
              console.log(response)
              setGroups(response.data);
              setLoading(false);
            }).catch(error => {
              console.error("Error fetching user data:", error);
            });

        }).catch((error) => {
          console.error("Error acquiring token silently:", error);
        });

    }
}; 
  
  // Call when page loaded
  useEffect(() => {

    loadUserData();

  }, []);

  //
  const remove = async (id) => {
    await fetch(`/group/${id}`, {
      method: 'DELETE',
      headers: {
        'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    }).then(() => {
      let updatedGroups = [...groups].filter(i => i.id !== id);
      setGroups(updatedGroups);
    });
  }

  if (loading) {
    return <p>Loading...</p>;
  }

  const groupList = groups.map(group => {
    const address = `${group.id || ''} ${group.name || ''} ${group.status || ''}`;
    return <tr key={group.id}>
      <td>{group.id}</td>
      <td style={{whiteSpace: 'nowrap'}}>{group.name}</td>
      <td>{group.status}</td>
      <td>
        {group.roles.map(role => {return <div key={role.id}>{role.name}</div>})}
      </td>
      <td>
        <ButtonGroup>
          <Button size="sm" color="primary" tag={Link} to={"/groups/" + group.id}>Edit</Button>
          <Button size="sm" color="danger" onClick={() => remove(group.id)}>Delete</Button>
        </ButtonGroup>
      </td>
    </tr>
  });

  return (
    <div>
      <Container fluid>
        <div className="float-end">
          <Button color="success" tag={Link} to="/groups/new">Add User</Button>
        </div>
        <h3>Application Users</h3>
        <Table className="mt-4">
          <thead>
          <tr>
            <th width="20%">ID</th>
            <th width="20%">Name</th>
            <th width="20%">Status</th>
            <th>Roles</th>
            <th width="10%">Actions</th>
          </tr>
          </thead>
          <tbody>
          {groupList}
          </tbody>
        </Table>
      </Container>
    </div>
  );
};

export default UserList;