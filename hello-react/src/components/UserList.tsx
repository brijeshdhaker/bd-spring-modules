import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import NavBar from "./NavBar";
import { Link } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';
import { loginRequest, apiLoginRequest, graphConfig } from '../helpers/authConfig';

const UserList = () => {

  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(false);
  const [cookies] = useCookies(['XSRF-TOKEN']);
  const {instance, accounts } = useMsal();
  const [pageData, setPageData] = useState(null);
  const [accessToken, setAccessToken] = useState(String);


  useEffect(() => {

    setLoading(true);
    
    // Silently acquires an access token which is then attached to a request for MS Graph data
    instance.acquireTokenSilent({
        ...apiLoginRequest,
        account: accounts[0],
    })
    .then((response) => {
        //console.log("Rest API Token : " + response.accessToken);  
      return response.accessToken;
    }).then((accessToken) => {
        //console.log("data : " + data);  
        setAccessToken(accessToken);

        fetch('/api/v1/users', {
          method: 'GET',
          headers: {
            'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + accessToken
          },
          credentials: 'include'
        })
        .then(response => response.json())
        .then(data => {
          setGroups(data);
          setLoading(false);
        })

    });

    
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