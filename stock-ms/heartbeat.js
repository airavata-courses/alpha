function zookeep(callback) {
  var request = require("request");

  request(
    {
      url: "http://149.165.157.99:8081/service",
      method: "PUT",
      json: {
        serviceName: "stocks",
        instanceId: "_1",
        address: "149.165.170.132",
        port: 8000
      }
    },
    callback
  );
}
setInterval(zookeep, 30000);
