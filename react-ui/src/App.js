import React, { Component } from "react";
import logo from "./logo.svg";
import "./App.css";
import { Stocks } from "./Components/Stocks";
import { News } from "./Components/News";
import { Weather } from "./Components/Weather";

class App extends Component {
  render() {
    return (
      <div className="App">
        <p className="App-intro" />
        <Stocks />
        <News />
        <Weather />
      </div>
    );
  }
}

export default App;
