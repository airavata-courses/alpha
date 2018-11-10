import React, { Component } from "react";
import { connect } from "react-redux";
import { login } from "../redux/reducer";
import "./LoginForm.css";

import {
  BrowserRouter as Router,
  Route,
  NavLink,
  Redirect
} from "react-router-dom";
import { withRouter } from "react-router-dom";
import App from "../../App";

class LoginForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      credentials: {}
    };
  }

  render() {
    let { username, password } = this.state;
    let { isLoginPending, isLoginSuccess, loginError } = this.props;

    return (
      <div>
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
              <a href="/signup">SignUp</a>
            </div>
          </div>
        </div>

        <div
          className="login-form-wrapper"
          style={{ width: "50%", margin: "auto" }}
          onSubmit={this.onSubmit}
        >
          <form name="loginForm">
            <h1>Login</h1>

            <div class="imgcontainer">
              <img src={require("./avatar.png")} class="avatar" />
            </div>
            <label style={{ padding: "15px", float: "left" }}>Email:</label>
            <input
              type="email"
              name="username"
              onChange={e => this.setState({ username: e.target.value })}
              value={username}
              style={{ padding: "15px", float: "right" }}
            />
            <br />
            <label style={{ padding: "15px", float: "left" }}>Password:</label>
            <input
              type="password"
              name="password"
              onChange={e => this.setState({ password: e.target.value })}
              value={password}
              style={{ padding: "15px", float: "right" }}
            />
            <br />
            <input type="submit" value="Login" />

            {isLoginPending && <div>Please wait...</div>}
            {isLoginSuccess && <Redirect to="/App" />}

            {loginError && <div>{loginError}</div>}
            <div
              class="container"
              style={{ backgroundColor: "#f1f1f1", textAlign: "right" }}
            >
              <span class="psw">
                <a
                  href="#"
                  onClick={() => {
                    this.props.history.push("/signup");
                  }}
                >
                  Forgot Pass/SignUp
                </a>
              </span>
            </div>
          </form>

          {/* <button
          onClick={() => {
            this.props.history.push("/signup");
          }}
        >
          SignUp
        </button> */}
        </div>
      </div>
    );
  }
  onSubmit = e => {
    e.preventDefault();
    let { username, password } = this.state;
    this.props.login(username, password);
  };
}

const mapStateToProps = state => {
  console.log("login state", state);
  return {
    isLoginPending: state.isLoginPending,
    isLoginSuccess: state.isLoginSuccess,
    loginError: state.loginError,
    city: state.userPreferences.city,
    country: state.userPreferences.country,
    company: state.userPreferences.company,
    subscribedToNewsAlerts: state.userPreferences.subscribedToNewsAlerts,
    subscribedToWeatherAlerts: state.userPreferences.subscribedToWeatherAlerts
  };
};

function getip() {
  let res = fetch("http://149.165.157.99:8081/service/db", {
    method: "GET"
  })
    .then(res => {
      if (res.ok) {
        return res.json();
      } else {
        this.setState({
          isLoaded: false,
          error: "Error fetching data"
        });
      }
    })
    .then(result => {
      return result;
    });
  return res;
}

let ip;
let port;
//this.getWeather();
getip().then(result => {
  (port = result.port), (ip = result.address);
});

const mapDispathToProps = dispatch => {
  return {
    login: (username, password) => dispatch(login(username, password, ip, port))
  };
};
LoginForm = connect(
  mapStateToProps,
  mapDispathToProps
)(LoginForm);

export default withRouter(LoginForm);
