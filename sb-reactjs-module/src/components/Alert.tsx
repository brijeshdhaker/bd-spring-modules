import React, { useEffect, useState } from 'react';
interface AlertProps {
    children: React.ReactNode;
    text: string;
}

const Alert = ({children, text} : AlertProps) => {
  return (
    <div className="alert alert-primary" role="alert">
        {text} {children}
    </div>
  )
}

export default Alert