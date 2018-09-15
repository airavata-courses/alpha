import React, { Component } from "react";
import logo from "./logo.svg";
import "./App.css";
import { Stocks } from "./Components/Stocks";

class App extends Component {
  render() {
    return (
      <div className="App">
        <p className="App-intro" />
        <Stocks />
      </div>
    );
  }
}

export default App;
