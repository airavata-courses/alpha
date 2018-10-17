const SET_LOGIN_PENDING = "SET_LOGIN_PENDING";
const SET_LOGIN_SUCCESS = "SET_LOGIN_SUCCESS";
const SET_LOGIN_ERROR = "SET_LOGIN_ERROR";
const SET_USER_PREFERENCE = "SET_USER_PREFERENCE";

//set state
export function login(username, password) {
  return dispatch => {
    fetch("https://jsonplaceholder.typicode.com/posts.login", {
      method: "post",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ username: username, password: password })
    })
      .then(res => res.json())
      .then(result => {
        if (result) {
          dispatch(setLoginSuccess(true));
          dispatch(
            setUserPreference({
              city: "Bloomington, IN",
              country: "US",
              company: "Apple",
              subscribedToNewsAlerts: false,
              subscribedToWeatherAlerts: true
            })
          );
        }
      });
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

//check credentials befoe logging in
function checkCredentials(username, password) {
  fetch("https://jsonplaceholder.typicode.com/posts.login", {
    method: "post",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ username: username, password: password })
  })
    .then(res => res.json())
    .then(result => {
      return result;
    });
}

// login check and then callback
function callLoginApi(username, password, callback) {
  setTimeout(() => {
    //get user pref
    var jsonobj = {
      username: "adhage@iu.edu",
      password: "adhage"
    };
    //jsonobj = checkCredentials(username, password);
    if (jsonobj !== -1) {
      return callback(null);
    } else {
      return callback(new Error("Invalid username and password"));
    }
  }, 1000);
}

const initialState = {
  isLoginSuccess: false,
  isLoginPending: false,
  loginError: null,
  userPreferences: {
    city: "",
    country: "",
    company: "",
    subscribedToNewsAlerts: false,
    subscribedToWeatherAlerts: true
  }
};

export default function reducer(state = initialState, action) {
  switch (action.type) {
    case SET_LOGIN_PENDING:
      return Object.assign({}, state, {
        isLoginPending: action.isLoginPending
      });

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
        userPreferences: action.payload
      };

    default:
      return state;
  }
}
