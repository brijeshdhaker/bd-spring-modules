import React from 'react'
import { Link } from 'react-router'

const NoPage = () => {
  return (
    <div>
      <h1>404</h1>
      <p>
        <Link to="/home">Go to the home page</Link>
      </p>
    </div>
  )
}

export default NoPage