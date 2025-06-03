import React from 'react'
import { Button, Card, Col, Container, Row } from 'react-bootstrap'

const Contact = () => {
  return (
    <div>
      <h1>@ Contact Page </h1>
      <Container fluid>
      <Row xs={1} md={2} className="g-4">
      {Array.from({ length: 4 }).map((_, idx) => (
        <Col key={idx}>
          <Card>
            <Card.Img variant="top" style={{height:100, width:150}} src="/vite.svg" />
            <Card.Body>
              <Card.Title>Card title</Card.Title>
              <Card.Text>
                This is a longer card with supporting text below as a natural
                lead-in to additional content. This content is a little bit
                longer.
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      ))}
    </Row>
      </Container>
    </div>
  )
}

export default Contact