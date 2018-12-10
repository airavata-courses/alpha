import { SET_STOCKS_IP_PORT, SET_STOCKS, SET_STOCKS_ERROR } from "./actions";
import { getip } from "../../Components/getip";

export function getStocks(ip, port, company) {
  return dispatch => {
    fetch("http://149.165.157.60:30004/stocks/" + company)
      .then(res => {
        if (res.ok) {
          return res.json();
        } else {
          console.log("response not ok");
          dispatch(seterror(true));
        }
      })
      .then(result => {
        dispatch(setstocks(result.value));
        // dispatch(seterror(false));
      })
      .catch(error => {
        console.log("in catch for stocks");
        dispatch(seterror(true));
      });
  };
}

export function getstocksipport() {
  return dispatch => {
    getip("stocks").then(result => {
      console.log("result in ip ", result);
      dispatch(set_stocks_ip_port(result.address, result.port));
    });
  };
}

let initialState = {
  error: false,
  stocks: "36.556",
  ip: null,
  port: null
};

function set_stocks_ip_port(ip, port) {
  return {
    type: SET_STOCKS_IP_PORT,
    payload: {
      ip: ip,
      port: port
    }
  };
}

function setstocks(stocks) {
  return {
    type: SET_STOCKS,
    payload: stocks
  };
}

function seterror(error) {
  return {
    type: SET_STOCKS_ERROR,
    payload: error
  };
}

export const StocksReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_STOCKS_IP_PORT:
      return {
        ...state,
        ip: action.payload.ip,
        port: action.payload.port
      };
    case SET_STOCKS:
      return {
        ...state,
        error: false,
        stocks: action.payload
      };

    case SET_STOCKS_ERROR:
      return {
        ...state,
        error: action.payload
      };

    default:
      return state;
  }
};
