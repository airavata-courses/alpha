var io = require("socket.io")();

io.on("connection", client => {
  let basevalue = 100;
  client.on("subscribeToLiveStocks", interval => {
    console.log("subscribing to live stock");
    setInterval(() => {
      client.emit("stockprice", Math.random() * 1000);
      client.emit("fbstockprice", Math.random() * 1000);
    }, interval);
  });
});

const port = 8000;
io.listen(port);
console.log("listening to port ", port);
