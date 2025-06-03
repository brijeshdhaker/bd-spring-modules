import React, { useState, useEffect } from 'react';
import { get } from '../services/ApiServices';
import Button from 'react-bootstrap/esm/Button';
import Table from 'react-bootstrap/Table';
import { ButtonGroup, Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useCookies } from 'react-cookie';

const Blogs = () => {
  
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
        get('/api/v1/users').then((response) => {
            console.log(response.data);
            setUsers(response.data)
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
          <Link className="btn btn-success" role="button" to={"/group/" + user.id} >Edit</Link>
          <Link className="btn btn-danger" role="button" onClick={() => remove(user.id)} >Delete</Link>
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
        <h3>@ Blogs Page </h3>
        <div className="float-end">
          <Link className="btn btn-success" role="button" to="/group/new">Add Group</Link>
        </div>
        {dispaly && 
        <Table className="mt-4">
          <thead>
          <tr>
            <th width="20%">#</th>
            <th width="20%">Name</th>
            <th>Email</th>
            <th width="10%">Actions</th>
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

export default Blogs