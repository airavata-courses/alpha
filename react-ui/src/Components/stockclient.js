import openSocket from "socket.io-client";
// const socket = openSocket("ws://149.165.170.132:8000");

function subscribeToStock(companyName, url, callback) {
  //console.log("url", url);
  let url1 = "ws://" + url.address + ":" + url.port + "/";
  console.log("url in stocks", url1);
  const socket = openSocket("ws://" + url.address + ":" + url.port);
  //console.log(callback);
  //console.log("company name in stockClient", companyName);
  socket.on("connect_failed", function() {
    //console.log("Sorry, there seems to be an issue with the connection!");
    callback("Connection Failed");
  });
  socket.on("error", function() {
    //console.log("Sorry, there seems to be an issue with the connection!");
    callback("Error");
  });
  //console.log("will go in switch case", companyName);
  switch (companyName) {
    case "apple":
      socket.on("stockprice", val => callback(val));
      break;

    case "facebook":
      socket.on("fbstockprice", val => callback(val));
      break;

    default:
      socket.on("stockprice", val => callback(val));
      break;
  }
  socket.emit("subscribeToLiveStocks", 3000);
}

export { subscribeToStock };
