import { SET_WEATHER_IP_PORT, SET_WEATHER, SET_WEATHER_ERROR } from "./actions";
import { getip } from "../../Components/getip";

export function getWeather(ip, port, city) {
  return dispatch => {
    fetch("http://149.165.157.60:30001/data?city=" + city)
      .then(res => {
        if (res.ok) {
          return res.json();
        } else {
          dispatch(seterror(true));
        }
      })
      .then(result => {
        dispatch(setweather(result));
        // dispatch(seterror(false));
      })
      .catch(error => {
        alert(error);
        dispatch(seterror(true));
      });
  };
}

let initialState = {
  error: false,
  weather: {},
  ip: null,
  port: null
};

export const getweatheripport = () => dispatch => {
  {
    getip("weather").then(result => {
      console.log("result weather ip", result);
      dispatch(set_weather_ip_port(result.address, result.port));
    });
  }
};

function set_weather_ip_port(ip, port) {
  return {
    type: SET_WEATHER_IP_PORT,
    payload: {
      ip: ip,
      port: port
    }
  };
}

function setweather(weather) {
  return {
    type: SET_WEATHER,
    payload: weather
  };
}

function seterror(error) {
  return {
    type: SET_WEATHER_ERROR,
    payload: error
  };
}

export const WeatherReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_WEATHER_IP_PORT:
      return {
        ...state,
        ip: action.payload.ip,
        port: action.payload.port
      };
    case SET_WEATHER:
      return {
        ...state,
        error: false,
        weather: action.payload
      };

    case SET_WEATHER_ERROR:
      return {
        ...state,
        error: action.payload
      };

    default:
      return state;
  }
};
