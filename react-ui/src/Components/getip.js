function getip() {
  let res = fetch("http://149.165.169.102:5000", {
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
}

export { getip };
