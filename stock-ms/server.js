var bodyParser = require("body-parser");
var routes = require("./api/Routes/routes");
var cors = require("cors");
const { fork } = require("child_process");

const process = fork("./heartbeat.js");
var express = require("express"),
  app = express(),
  port = 8000;

app.use(cors());

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
routes(app);

app.listen(port);

console.log("Stock API server started on: " + port);
//
// var io = require("socket.io")();
//
// io.on("connection", client => {
//   let basevalue = 100;
//   client.on("subscribeToLiveStocks", interval => {
//     console.log("subscribing to live stock");
//     setInterval(() => {
//       client.emit("stockprice", Math.random() * 100);
//       client.emit("fbstockprice", Math.random());
//     }, interval);
//   });
// });
//
// const port = 8000;
// io.listen(port);
// console.log("listening to port ", port);
