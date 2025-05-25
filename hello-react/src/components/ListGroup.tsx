
import { MouseEvent, useEffect, useState  }  from "react";
import { useCookies } from 'react-cookie';



interface ListGroupProps {
    items: string[];
    heading: string;
    onSelectItem: (item: string) => void;
}

function    ListGroup({items, heading, onSelectItem}: ListGroupProps) {
  
  //Hook
  const [selectedIndex, setSelectedIndex] = useState(-1);

  // Event Handler
  const handelClick = (event : MouseEvent ) => console.log(event);

  //
  const getData = () => {
    return items.map((item, index) => (
      <a href="#" 
        key={item} 
        className={selectedIndex === index ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'}
        onClick={() => { 
          setSelectedIndex(index);
          onSelectItem(item); 
        }}>{item}
      </a>
    )
  )}

  //
  return (
    <>
    <h1>{heading}</h1>
    {items.length === 0 && <h1>No Items Found</h1>}
    <div className="list-group">{getData()}</div>
    </>
  );


}

export default ListGroup;