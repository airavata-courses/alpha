import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import { Provider } from "react-redux";
import store from "./Components/redux/store";
import LoginForm from "./Components/LoginForm/LoginForm";
import SignUpForm from "./Components/SignUpForm";
import App from "./App";
import { BrowserRouter as Router, Route } from "react-router-dom";
import registerServiceWorker from "./registerServiceWorker";
const Login = () => <LoginForm />;

const SignUp = () => <SignUpForm />;

const app = () => <App />;

ReactDOM.render(
  <Provider store={store}>
    <Router>
      <div className="App">
        <Route exact path="/" component={Login} />
        <Route path="/signup" component={SignUp} />
        <Route path="/app" component={app} />
      </div>
    </Router>
  </Provider>,
  document.getElementById("root")
);
registerServiceWorker();
