import React from "react";
import ReactDOM from "react-dom";
import App from "./App";
import io from "socket.io-client";

it("renders without crashing", () => {
  const div = document.createElement("div");
  ReactDOM.render(<App />, div);
  ReactDOM.unmountComponentAtNode(div);
});

it("socket connection is connected", () => {
  let socket = io("ws://localhost:8000");
  socket.on("connect", () => {
    console.log("socket connected");
  });
});

it("data returned when subscribed to event", () => {
  let socket = io("ws://localhost:8000");
  socket.emit("subscribeToLiveStocks", 1000);
  socket.on("stockprice", stockvalue => {
    expect(stockvalue).toBeDefined();
  });
});
