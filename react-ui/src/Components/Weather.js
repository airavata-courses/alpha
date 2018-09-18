import React, { Component } from 'react';

class Weather extends Component {

    constructor(props){
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            weather: {}
        }
        fetch("http://localhost:9102/data")
        .then(res => res.json())
        .then(
          (result) => {
              console.log(result)
            this.setState({
              isLoaded: true,
              weather: result
            });
          }
        )
    }

    render() {
        const { error, isLoaded, weather } = this.state;
        if (error) {
            return <div>Error: {error.message}</div>;
        }
        else if (!isLoaded) {
          return <div>Loading...</div>;
        }
        else {
        	console.log(weather)
            return (
            	<div>
                	<h2>{weather.shortDesc}</h2>
                	<p>Description: {weather.description}</p>
                	<p>Temperature: {weather.temperature} F</p>
                	<p>Wind speed: {weather.wind.speed} miles/hour</p>
                	<p>Humidity: {weather.humidity} %</p>
                	<p>Sunrise: {weather.event.sunrise} &nbsp; Sunset: {weather.event.sunset}</p>
                </div>
            );
        }
    }
}


export {Weather};
