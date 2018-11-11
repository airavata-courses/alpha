const getip = function getip(servicename) {
  let res = fetch(`http://149.165.157.99:8081/service/${servicename}`, {
    method: "GET"
  })
    .then(res => {
      if (res.ok) {
        return res.json();
      } else {
        this.setState({
          isLoaded: false,
          error: "Error fetching data"
        });
      }
    })
    .then(result => {
      return result;
    });
  return res;
};

export { getip };
