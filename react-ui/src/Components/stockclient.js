import openSocket from "socket.io-client";
const socket = openSocket("ws://149.165.157.99:8000");

function subscribeToStock(companyName, callback) {
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
  socket.emit("subscribeToLiveStocks", 1000);
}

export { subscribeToStock };
