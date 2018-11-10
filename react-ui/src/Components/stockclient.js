import openSocket from "socket.io-client";
// const socket = openSocket("ws://149.165.170.132:8000");

function subscribeToStock(companyName, url, callback) {
  console.log("url", url);
  let url1 = "ws://" + url.address + ":" + url.port + "/";
  console.log("url ", url1);
  const socket = openSocket("ws://" + url.address + ":" + url.port);
  console.log(callback);
  console.log("company name in stockClient", companyName);
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
