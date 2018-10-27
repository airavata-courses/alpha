import React, { Component } from "react";
import logo from "./logo.svg";
import "./App.css";
import { Stocks } from "./Components/Stocks";
import { News } from "./Components/News";
import { Weather } from "./Components/Weather";

class App extends Component {
  render() {
    return (
      <div>
        <div class="header">
          <a href="#default" class="logo">
            DASHBOARD
          </a>
          <div class="header-right">
            <a class="active" href="/App">
              Home
            </a>
            {/* <a href="#contact">Contact</a>  */}
            <a href="/">Log Out</a>
          </div>
        </div>
        <div style={{ display: "flex", justifyContent: "center" }}>
          <div
            style={{
              marginTop: "30px",
              marginRight: "-50px",
              marginLeft: "-100px"
            }}
          >
            <Stocks />{" "}
          </div>
          <div
            style={{
              marginTop: "30px",
              marginRight: "-100px",
              marginLeft: "200px"
            }}
          >
            <Weather />
          </div>
          <div
            style={{
              marginRight: "-200px",
              marginTop: "30px",
              marginLeft: "100px"
            }}
          >
            <News />
          </div>
        </div>
      </div>
    );
  }
}

export default App;
