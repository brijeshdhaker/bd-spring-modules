import { graphConfig } from "./authConfig";

/**
 * Attaches a given access token to a MS Graph API call. Returns information about the user
 * @param accessToken 
 */
export async function callMsGraph(accessToken : string) {
    const headers = new Headers();
    const bearer = `Bearer ${accessToken}`;

    headers.append("Authorization", bearer);

    const options = {
        method: "GET",
        headers: headers
    };

    return fetch(graphConfig.graphMeEndpoint, options)
        .then(response => response.json())
        .catch(error => console.log(error));
}
/*
// Using .catch()
fetch('your-api-endpoint')
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(data => {
    // Process the data
    console.log(data);
  })
  .catch(error => {
    // Handle the error
    console.error('There was a problem fetching the data:', error);
  });

// Using async/await with try/catch
async function fetchData() {
  try {
    const response = await fetch('your-api-endpoint');
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    const data = await response.json();
    // Process the data
    console.log(data);
  } catch (error) {
    // Handle the error
    console.error('There was a problem fetching the data:', error);
  }
}

fetchData();
*/
