import React, { Component } from "react";
import { connect } from "react-redux";
import { getip } from "./getip";

class Weather extends Component {
  getWeather(port, ip) {
    let city;
    if (this.props.city) {
      city = this.props.city;
      console.log("weather city", city);
    } else {
      city = "";
    }
    let url = "http://" + ip + ":" + port + "/data?city=" + city;
    console.log("weather  url", url);
    fetch("http://" + ip + ":" + port + "/data?city=" + city)
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
        console.log("result", result);
        this.setState({
          isLoaded: true,
          weather: JSON.parse(result.message),
          error: 0
        });
      })
      .catch(error => {
        this.setState({ isLoaded: false });
      });
  }

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      weather: {}
    };

    this.getWeather = this.getWeather.bind(this);

    // this.componentDidMount = this.componentDidMount.bind(this);
  }

  render() {
    const { error, isLoaded, weather } = this.state;
    if (error) {
      return <div>Error: {error.message}</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else {
      return (
        <div style={{ width: "90%" }}>
          <div style={{ border: "1px solid #ccc", backgroundColor: "Gray" }}>
            <h3>Weather Feed</h3>
          </div>
          <h4>{weather.shortDesc}</h4>
          <p>Description: {weather.description}</p>
          <p>Temperature: {weather.temperature} F</p>
          <p>Wind speed: {weather.wind.speed} miles/hour</p>
          <p>Humidity: {weather.humidity} %</p>
          <p>
            Sunrise: {weather.event.sunrise} &nbsp; Sunset:{" "}
            {weather.event.sunset}
          </p>
        </div>
      );
    }
  }

  getipport() {
    let ip;
    let port;
    getip("weather").then(result => {
      console.log("result inside getipweather", result), (port = result.port);
      ip = result.address;
    });
    console.log(`result of getip ip = ${ip} and port = ${port}`);
    return { port: port, ip: ip };
  }
  componentDidMount() {
    let ip;
    let port;
    let statusip = null;
    getip("weather").then(result => {
      port = result.port;
      ip = result.address;
    });
    console.log(`result of getip ip = ${ip} and port = ${port}`);
    setInterval(() => this.getWeather(port, ip), 3000);
  }
}

const mapStateToProps = state => {
  console.log("state in weather", state);
  return {
    city: state.userPreferences.city,
    country: state.userPreferences.country,
    company: state.userPreferences.company,
    subscribedToNewsAlerts: state.userPreferences.subscribedToNewsAlerts
  };
};

Weather = connect(mapStateToProps)(Weather);
export { Weather };
