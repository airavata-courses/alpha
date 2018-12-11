import { SET_NEWS_IP_PORT, SET_NEWS, SET_NEWS_ERROR } from "./actions";
import { getip } from "../../Components/getip";

let initialState = {
  error: null,
  news: [],
  ip: null,
  port: null
};

export function getNews(ip, port, country) {
  return dispatch => {
    fetch("http://149.165.157.60:30002/top_headlines?country=" + country)
      .then(res => {
        if (res.ok) {
          return res.json();
        } else {
          console.log("result of news", res);
          dispatch(seterror(true));
        }
      })
      .then(result => {
        console.log("result of news", result.news);
        dispatch(setnews(result.news));
        dispatch(seterror(false));
      })
      .catch(error => {
        console.log("error in catch");
        dispatch(seterror(true));
      });
  };
}

export function getnewsipport() {
  return dispatch => {
    getip("news").then(result => {
      console.log("news result to set is", result);
      dispatch(set_news_ip_port(result.address, result.port));
    });
  };
}

function set_news_ip_port(ip, port) {
  return {
    type: SET_NEWS_IP_PORT,
    payload: {
      ip: ip,
      port: port
    }
  };
}

function setnews(news) {
  return {
    type: SET_NEWS,
    payload: news
  };
}

function seterror(error) {
  return {
    type: SET_NEWS_ERROR,
    payload: error
  };
}

export const NewsReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_NEWS_IP_PORT:
      return {
        ...state,
        ip: action.payload.ip,
        port: action.payload.port
      };
    case SET_NEWS:
      return {
        ...state,
        error: false,
        news: action.payload
      };
    case SET_NEWS_ERROR:
      return {
        ...state,
        error: action.payload
      };

    default:
      return state;
  }
};
