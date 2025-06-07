import React, { useState, useEffect } from 'react';
//import { get } from '../services/ApiServices';
import { get } from '../helpers/fetch-api';
import Button from 'react-bootstrap/esm/Button';
import Table from 'react-bootstrap/Table';
import { ButtonGroup, Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useCookies } from 'react-cookie';

const Users = () => {
  
  const [loading, setLoading] = useState(false);
  const[users, setUsers] = useState([]);
  const[dispaly, setDispaly] = useState(true);
  const [cookies] = useCookies(['XSRF-TOKEN']);

  function toggelDisplay() {
    setDispaly(!dispaly);
  }

  useEffect(() => {
      //
      if(users.length === 0){
        get('/api/v1/users').then((response) => response.json()).then((data) => {
            console.log(data);
            setUsers(data)
            setLoading(false);
        }).catch(()=> {
            setLoading(false);
        });
      }
  }, [users]); // Effect runs when user details changes

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
      //let updatedGroups = [...groups].filter(i => i.id !== id);
      //setUsers(updatedGroups);
    });
  }

  const userList = users.map((user, index) => {
    const address = `${user.id || ''} ${user.name || ''} ${user.email || ''}`;
    return <tr key={user.id}>
      <td style={{whiteSpace: 'nowrap'}}>{index}</td>
      <td>{user.name}</td>
      <td>{user.email}</td>
      <td>
        <ButtonGroup>
            <Link className="btn btn-success" role="button" to={"/user/" + user.id} >Edit</Link>
            <Button variant="danger" onClick={() => remove(user.id)} >Delete</Button>
        </ButtonGroup>
      </td>
    </tr>
  });

  if (loading) {
    return <p>Loading...</p>;
  }

  return (
    <div>
       <Container fluid>
        <h3>@ Users Page </h3>
        <div className="float-end">
          <Link className="btn btn-success" role="button" to="/users/new">Add User</Link>
        </div>
        {dispaly && 
        <Table className="mt-4">
          <thead>
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          {userList}
          </tbody>
        </Table>
        }
      <Button variant="secondary" onClick={toggelDisplay}>{ dispaly ? 'Hide Details' : 'Display Details'}</Button>  
      </Container>
    </div>
  )
}

export default Users