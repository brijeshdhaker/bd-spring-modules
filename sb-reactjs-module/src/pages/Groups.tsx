import React, { useState, useEffect } from 'react';
//import { get } from '../services/ApiServices';
import { get } from '../helpers/fetch-api';
import Button from 'react-bootstrap/esm/Button';
import Table from 'react-bootstrap/Table';
import { ButtonGroup, Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useCookies } from 'react-cookie';

const Groups = () => {
  
  const [loading, setLoading] = useState(false);
  const[groups, setGroups] = useState([]);
  const[dispaly, setDispaly] = useState(true);
  const [cookies] = useCookies(['XSRF-TOKEN']);

  function toggelDisplay() {
    setDispaly(!dispaly);
  }

  useEffect(() => {
    
          if(groups.length === 0){
            get('/api/v1/group/list').then((response) => response.json()).then((data) => {
                console.log(data);
                setGroups(data)
                setLoading(false);
            }).catch(()=> {
                setLoading(false);
            });
          }
    /*     
    
      if(groups.length === 0){
        get('/api/v1/group/list').then((response) => {
            console.log(response.data);
            setGroups(response.data)
            setLoading(false);
        }).catch(()=> {
            setLoading(false);
        });
      }
    */
  }, [groups]); // Effect runs when user details changes

  const remove = async (id) => {
    await fetch(`/api/v1/group/${id}`, {
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

  const userList = groups.map((group, index) => {
    const address = `${group.id || ''} ${group.name || ''} ${group.email || ''}`;
    return <tr key={group.id}>
      <td style={{whiteSpace: 'nowrap'}}>{index}</td>
      <td>{group.name}</td>
      <td>{group.email}</td>
      <td>
        <ButtonGroup>
          <Link className="btn btn-success" role="button" to={"/group/" + group.id} >Edit</Link>
          <Link className="btn btn-danger" role="button" onClick={() => remove(group.id)} >Delete</Link>
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
        <h3>@ Group Page </h3>
        <div className="float-end">
          <Link className="btn btn-success" role="button" to="/group/new">Add Group</Link>
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

export default Groups