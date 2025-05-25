import React from 'react'
import { MouseEvent } from 'react';


interface Props {
    children: React.ReactNode;
    color?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark';
    idx: '1' | '2' | '3' | '4'  | '5' | '6' | '7' | '8';
    onButtonClick: (event : MouseEvent, idx: string) => void ;
}

const Button = ({children, color = 'primary', idx, onButtonClick} : Props) => {
  return (
    <button type="button" className={"btn btn-"+color} onClick={(event) => onButtonClick(event, idx)}>{children}</button>
  )
}

export default Button