import React, { Component } from "react";
import { connect } from "react-redux";
import { login } from "../redux/reducer";
import { getWeather, getweatheripport } from "../redux/weatherReducer";
import { getNews, getnewsipport } from "../redux/newsReducer";
import { getStocks, getstocksipport } from "../redux/stocksReducer";
import "./LoginForm.css";
import { getip } from "../getip";

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
    console.log("before weather");
    this.props.getweatheripport();
    console.log("weather ip", this.props.weather_ip);
    this.props.getnewsipport();
    console.log("after news");
    this.props.getstocksipport();
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
        </div>
      </div>
    );
  }
  onSubmit = e => {
    e.preventDefault();
    let { username, password } = this.state;
    this.props
      .login(username, password, "149.165.169.102", 9101)
      .then(result => {
        this.props.getWeather(
          this.props.weather_ip,
          this.props.weather_port,
          result.city
        );
        this.props.getNews(
          this.props.news_ip,
          this.props.news_port,
          result.country
        );
        this.props.getStocks(
          this.props.stocks_ip,
          this.props.stocks_port,
          result.company
        );
      });
  };
}

const mapStateToProps = state => {
  return {
    isLoginPending: state.UserReducer.isLoginPending,
    isLoginSuccess: state.UserReducer.isLoginSuccess,
    loginError: state.UserReducer.loginError,
    city: state.UserReducer.userPreferences.city,
    country: state.UserReducer.userPreferences.country,
    company: state.UserReducer.userPreferences.company,
    subscribedToNewsAlerts:
      state.UserReducer.userPreferences.subscribedToNewsAlerts,
    subscribedToWeatherAlerts:
      state.UserReducer.userPreferences.subscribedToWeatherAlerts,
    weather_ip: state.WeatherReducer.ip,
    weather_port: state.WeatherReducer.port,
    news_ip: state.NewsReducer.ip,
    news_port: state.NewsReducer.port,
    stocks_ip: state.StocksReducer.ip,
    stocks_port: state.StocksReducer.port
  };
};

LoginForm = connect(
  mapStateToProps,
  {
    login,
    getweatheripport,
    getnewsipport,
    getstocksipport,
    getWeather,
    getNews,
    getStocks
  }
)(LoginForm);

export default withRouter(LoginForm);
