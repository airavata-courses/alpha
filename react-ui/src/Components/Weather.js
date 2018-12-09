import React, { Component } from "react";
import { connect } from "react-redux";
import { getip } from "./getip";
import { getWeather } from "./redux/weatherReducer";

class Weather extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { weather } = this.props;
    if (this.props.error) {
      return <div>Error: {this.props.error}</div>;
    } else if (Object.keys(this.props.weather).length === 0) {
      return <div>Failed to fetch</div>;
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

  componentDidMount() {
    setInterval(
      () =>
        this.props.getWeather(this.props.ip, this.props.port, this.props.city),
      30000
    );
  }
}

const mapStateToProps = state => {
  return {
    city: state.UserReducer.userPreferences.city,
    country: state.UserReducer.userPreferences.country,
    company: state.UserReducer.userPreferences.company,
    subscribedToNewsAlerts:
      state.UserReducer.userPreferences.subscribedToNewsAlerts,
    ip: state.WeatherReducer.ip,
    port: state.WeatherReducer.port,
    error: state.WeatherReducer.error,
    weather: state.WeatherReducer.weather
  };
};

Weather = connect(
  mapStateToProps,
  { getWeather }
)(Weather);
export { Weather };
