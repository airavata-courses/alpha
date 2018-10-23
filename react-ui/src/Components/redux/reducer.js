const SET_LOGIN_PENDING = "SET_LOGIN_PENDING";
const SET_LOGIN_SUCCESS = "SET_LOGIN_SUCCESS";
const SET_LOGIN_ERROR = "SET_LOGIN_ERROR";
const SET_USER_PREFERENCE = "SET_USER_PREFERENCE";

//set state
export function login(username, password) {
  return dispatch => {
    fetch("http://149.165.168.71:9101/login", {
      method: "post",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ username: username, password: password })
    })
      .then(res => res.json())
      .then(result => {
        dispatch(setLoginSuccess(true));
        // dispatch(
        //   setUserPreference({
        //     // city: "Bloomington, IN",
        //     // country: "US",
        //     // company: "Apple",
        //     // subscribedToNewsAlerts: true,
        //     // subscribedToWeatherAlerts: false
        //     city: message.city,
        //     country: message.country,
        //     company: message.company,
        //     subscribedToNewsAlerts: message.subscribedToNewsAlerts,
        //     subscribedToWeatherAlerts: message.subscribedToWeatherAlerts
        //   })
        // );
        if (result.status === 200) {
          console.log(result);
          let message = JSON.parse(result.message);
          console.log(message.city);
          dispatch(setLoginSuccess(true));
          dispatch(
            setUserPreference({
              city: "Bloomington, IN",
              country: "US",
              company: "Apple",
              subscribedToNewsAlerts: true,
              subscribedToWeatherAlerts: false,
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
// function checkCredentials(username, password) {
//   fetch("https://149.161.203.120:9101", {
//     method: "post",
//     headers: {
//       "Content-Type": "application/json"
//     },
//     body: JSON.stringify({ username: username, password: password })
//   })
//     .then(res => res.json())
//     .then(result => {
//       return result;
//     });
// }

// login check and then callback
// function callLoginApi(username, password, callback) {
//   setTimeout(() => {
//     //get user pref
//     //jsonobj = checkCredentials(username, password);
//     if (jsonobj !== -1) {
//       return callback(null);
//     } else {
//       return callback(new Error("Invalid username and password"));
//     }
//   }, 1000);
// }

const initialState = {
  isLoginSuccess: false,
  isLoginPending: false,
  loginError: null,
  userPreferences: {
    city: "",
    country: "",
    company: "",
    subscribedToNewsAlerts: false,
    subscribedToWeatherAlerts: false
  }
};

export default function reducer(state = initialState, action) {
  switch (action.type) {
    case SET_LOGIN_PENDING:
      // return Object.assign({}, state, {
      //   isLoginPending: action.isLoginPending
      // });
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
        userPreferences: action.payload
      };

    default:
      return state;
  }
}
