import React, { Component } from 'react';

class Weather extends Component {

    getWeather(){
        fetch("http://localhost:9102/data")
        .then(res => {
            if(res.ok){
                return res.json();  
            }
            else {
                this.setState({
                    isLoaded: false,
                    error: "Error fetching data"
                });
            }
        })
        .then(
          (result) => {
            this.setState({
              isLoaded: true,
              weather: result,
              error: 0
            });
          }
        )
    }

    constructor(props){
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            weather: {}
        }

        this.getWeather();
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
            return (
                <div style = {{"border": "2px solid black", "width": "70%", "margin":"auto"}}>
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

    componentDidMount(){
        setInterval(this.getWeather, 300000);
    }
}


export {Weather};
