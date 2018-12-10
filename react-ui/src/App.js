import React, { Component } from "react";
import logo from "./logo.svg";
import "./App.css";
import { Stocks } from "./Components/Stocks";
import { News } from "./Components/News";
import { Weather } from "./Components/Weather";
import { connect } from "react-redux";
import { logout } from "./Components/redux/reducer";
import {
  BrowserRouter as Router,
  Route,
  NavLink,
  Redirect
} from "react-router-dom";
import { withRouter } from "react-router-dom";

class App extends Component {
  render() {
    if (!this.props.isLoginSuccess) {
      this.props.history.push("/");
      return null;
    } else {
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
              <button
                onClick={() => {
                  this.props.history.push("/");
                  this.props.logout();
                }}
              >
                LogOut
              </button>
            </div>
          </div>
          <div
            style={{
              display: "flex",
              flexDirection: "rows",
              justifyContent: "center"
            }}
          >
            <div
              style={{
                margin: "4%"
              }}
            >
              <Stocks />{" "}
            </div>
            <div
              style={{
                margin: "4%"
              }}
            >
              <Weather />
            </div>
            <div
              style={{
                margin: "4%"
              }}
            >
              <News />
            </div>
          </div>
        </div>
      );
    }
  }
}

const mapStateToProps = state => {
  // console.log("state in stocks", state.StocksReducer.stocks);
  return {
    isLoginSuccess: state.UserReducer.isLoginSuccess,
    loginError: state.UserReducer.loginError
  };
};

const mapDispathToProps = dispatch => {
  return {
    logout: () => dispatch(logout())
  };
};

App = connect(
  mapStateToProps,
  mapDispathToProps
)(App);

export default withRouter(App);
