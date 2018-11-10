const SET_LOGIN_PENDING = "SET_LOGIN_PENDING";
const SET_LOGIN_SUCCESS = "SET_LOGIN_SUCCESS";
const SET_LOGIN_ERROR = "SET_LOGIN_ERROR";
const SET_LOGOUT = "SET_LOGOUT";
const SET_USER_PREFERENCE = "SET_USER_PREFERENCE";

//set state
export function login(username, password, ip, port) {
  return dispatch => {
    // console.log("inside news" + country);

    fetch("http://" + ip + ":" + port + "/login", {
      method: "post",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ username: username, password: password })
    })
      .then(res => res.json())
      .then(result => {
        if (result.status === 200) {
          console.log("result", result);
          let message = JSON.parse(result.message);
          dispatch(setLoginSuccess(true));
          dispatch(
            setUserPreference({
              city: message.city,
              country: message.country,
              company: message.company,
              subscribedToNewsAlerts: message.subscribedToNewsAlerts,
              subscribedToWeatherAlerts: message.subscribedToWeatherAlerts
            })
          );
        } else if (result.status === 401) {
          dispatch(setLoginError("Invalid User Credentials"));
        } else if (result.status === 500) {
          dispatch(setLoginError("Server Error"));
        }
      });
  };
}

export function logout() {
  return dispatch => {
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

var dict = {};

dict["admin@example.com"] = "admin";
dict["adhage@iu.edu"] = "adhage";
dict["sidpath@iu.edu"] = "sidpath";
dict["hardik@iu.edu"] = "hardik";

const initialState = {
  isLoginSuccess: false,
  isLoginPending: false,
  loginError: null,
  logout: false,
  userPreferences: {
    city: "",
    country: "",
    company: "apple",
    subscribedToNewsAlerts: false,
    subscribedToWeatherAlerts: false
  }
};

export default function reducer(state = initialState, action) {
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
}
