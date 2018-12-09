import {
  SET_LOGIN_PENDING,
  SET_LOGIN_SUCCESS,
  SET_LOGIN_ERROR,
  SET_LOGOUT,
  SET_USER_PREFERENCE
} from "./actions";

//set state
export const login = (username, password, ip, port) => dispatch =>
  new Promise((resolve, reject) => {
    console.log("ip", ip);
    console.log("port", port);
    // fetch("http://" + ip + ":" + port + "/login", {
    fetch("http://db:9101", {
      method: "post",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ username: username, password: password })
    })
      .then(res => {
        if (res.ok) {
          return res.json();
        } else if (res.status == "401" || res.status == "403") {
          dispatch(setLoginError("Invalid User Credentials"));
        } else if (res.status == "500") {
          dispatch(setLoginError("Server Error"));
        }
      })
      .then(result => {
        console.log("result in login state is ", result);
        dispatch(setLoginSuccess(true));
        dispatch(
          setUserPreference({
            city: result.city,
            country: result.country,
            company: result.company,
            subscribedToNewsAlerts: result.subscribedToNewsAlerts,
            subscribedToWeatherAlerts: result.subscribedToWeatherAlerts
          })
        );
        resolve(result);
      })
      .catch(error => {
        dispatch(setLoginError("Server Error"));
        reject(error);
      });
  });

export function logout() {
  return dispatch => {
    //console.log("dispatch logout");
    dispatch(setLogout());
  };
}

function setUserPreference(user_pref) {
  return {
    type: SET_USER_PREFERENCE,
    payload: user_pref
  };
}

function setLoginPending(isLoginPending) {
  return {
    type: SET_LOGIN_PENDING,
    isLoginPending
  };
}

function setLoginSuccess(isLoginSuccess) {
  return {
    type: SET_LOGIN_SUCCESS,
    isLoginSuccess
  };
}
function setLogout() {
  return {
    type: SET_LOGOUT
  };
}

function setLoginError(loginError) {
  return {
    type: SET_LOGIN_ERROR,
    loginError
  };
}

// //set weather fetch from the url and set
// function getWeather(city, ip, port){
//   fetch(`http://${ip}:${port}/data?city=${city}`)
//   .then(res => {
//     if(res.ok()){

//     }
//   })
// }

const initialState = {
  isLoginSuccess: false,
  isLoginPending: false,
  loginError: null,
  logout: false,
  weather: null,
  news: null,
  stocks: null,
  userPreferences: {
    city: "",
    country: "",
    company: "",
    subscribedToNewsAlerts: false,
    subscribedToWeatherAlerts: false
  }
};

export const UserReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_LOGIN_PENDING:
      return {
        ...state,
        isLoginPending: action.isLoginPending
      };

    case SET_LOGIN_SUCCESS:
      return Object.assign({}, state, {
        isLoginSuccess: action.isLoginSuccess
      });

    case SET_LOGIN_ERROR:
      return Object.assign({}, state, {
        loginError: action.loginError
      });

    case SET_USER_PREFERENCE:
      return {
        ...state,
        isLoginSuccess: true,
        userPreferences: action.payload
      };
    case SET_LOGOUT:
      return initialState;

    default:
      return state;
  }
};
