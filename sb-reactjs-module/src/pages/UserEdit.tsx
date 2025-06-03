import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { Button, Container, Form, FormGroup, FormLabel } from 'react-bootstrap';

const UserEdit = () => {

  const initialFormState = {
    name: '',
    address: '',
    city: '',
    stateOrProvince: '',
    country: '',
    postalCode: ''
  };

  const [group, setGroup] = useState(initialFormState);
  const navigate = useNavigate();
  const { id } = useParams();
  const [cookies] = useCookies(['XSRF-TOKEN']);

  useEffect(() => {
    if (id !== 'new') {
      fetch(`/group/${id}`)
        .then(response => response.json())
        .then(data => setGroup(data));
    }
  }, [id, setGroup]);

  const handleChange = (event) => {
    const { name, value } = event.target

    setGroup({ ...group, [name]: value })
  }

  const handleSubmit = async (event) => {
    event.preventDefault();

    await fetch(`/group${group.id ? `/${group.id}` : ''}`, {
      method: (group.id) ? 'PUT' : 'POST',
      headers: {
        'X-XSRF-TOKEN': cookies['XSRF-TOKEN'],
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(group),
      credentials: 'include'
    });

    setGroup(initialFormState);
    navigate('/groups');
  }

  const title = <h2>{group.id ? 'Edit Group' : 'Add Group'}</h2>;

  return (<div>
      <Container>
        {title}
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <Form.Label>Name</Form.Label>
            <Form.Control type="text" name="name" id="name"  placeholder="Normal text" value={group.name || ''} onChange={handleChange} autoComplete="address-level1"/>
          </FormGroup>
          <FormGroup>
            <FormLabel for="address">Address</FormLabel>
            <Form.Control type="text" name="address" id="address"  placeholder="Normal text" value={group.address || ''} onChange={handleChange} autoComplete="address-level1"/>
          </FormGroup>
          <FormGroup>
            <FormLabel for="address">City</FormLabel>
            <Form.Control type="text" name="city" id="city"  placeholder="Normal text" value={group.city || ''} onChange={handleChange} autoComplete="address-level1"/>
          </FormGroup>
          <div className="row">
            <FormGroup className="col-md-4 mb-3">
              <Form.Label>State/Province</Form.Label>
              <Form.Control type="text" name="stateOrProvince" id="stateOrProvince"  placeholder="Normal text" value={group.stateOrProvince || ''} onChange={handleChange} autoComplete="address-level1"/>
            </FormGroup>
            <FormGroup className="col-md-5 mb-3">
              <Form.Label>Country</Form.Label>
              <Form.Control type="text" name="country" id="country"  placeholder="Normal text" value={group.country || ''} onChange={handleChange} autoComplete="address-level1"/>
            </FormGroup>
            <FormGroup className="col-md-3 mb-3">
              <Form.Label>Postal Code</Form.Label>
              <Form.Control type="text" name="postalCode" id="postalCode"  placeholder="Normal text" value={group.postalCode || ''} onChange={handleChange} autoComplete="address-level1"/>
            </FormGroup>
          </div>
          <FormGroup>
            <Button variant="primary" type="submit">Save</Button>{' '}
            <Link className="btn btn-secondary" role="button" to="/groups">Cancel</Link>
          </FormGroup>
        </Form>
      </Container>
    </div>
  )
};

export default UserEdit;