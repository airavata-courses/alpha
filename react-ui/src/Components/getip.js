const getip = function getip(servicename) {
  let res = fetch(`http://149.165.157.99:8081/service/${servicename}`, {
    method: "GET"
  })
    .then(res => {
      if (res.ok) {
        return res.json();
      }
    })
    .then(result => {
      return result;
    })
    .catch(error => {
      console.log(`error on ${servicename}`);
    });
  return res;
};

export { getip };
