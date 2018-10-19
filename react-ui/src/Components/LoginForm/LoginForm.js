import React, { Component } from "react";
import { connect } from "react-redux";
import { login } from "../redux/reducer";
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
      <div
        className="login-form-wrapper"
        style={{ width: "50%", margin: "auto" }}
        onSubmit={this.onSubmit}
      >
        <h1>Login</h1>
        <form name="loginForm">
          <label style={{ padding: "10px" }}>Username:</label>
          <input
            type="text"
            name="username"
            onChange={e => this.setState({ username: e.target.value })}
            value={username}
          />
          <br />
          <label style={{ padding: "10px" }}>Password:</label>
          <input
            type="password"
            name="password"
            onChange={e => this.setState({ password: e.target.value })}
            value={password}
            style={{ margin: "10px" }}
          />
          <br />
          <input type="submit" value="Login" />

          {isLoginPending && <div>Please wait...</div>}
          {isLoginSuccess && <Redirect to="/App" />}
          {loginError && <div>{loginError}</div>}
        </form>
        <button
          onClick={() => {
            this.props.history.push("/signup");
          }}
        >
          SignUp
        </button>
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
  console.log("state", state);
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

const mapDispathToProps = dispatch => {
  return {
    login: (username, password) => dispatch(login(username, password))
  };
};
LoginForm = connect(
  mapStateToProps,
  mapDispathToProps
)(LoginForm);

export default withRouter(LoginForm);
