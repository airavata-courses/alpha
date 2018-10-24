import openSocket from "socket.io-client";
const socket = openSocket("ws://149.165.157.99:8000");

function subscribeToStock(companyName, callback) {
  socket.on("stockprice", val => callback("apple", val));
  socket.on("fbstockprice", val2 => callback("fb", val2));
  socket.emit("subscribeToLiveStocks", 1000);
}

export { subscribeToStock };
