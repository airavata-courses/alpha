import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import { Provider } from "react-redux";
// import persistor from "./Components/redux/store";
import LoginForm from "./Components/LoginForm/LoginForm";
import SignUpForm from "./Components/SignUpForm";
import App from "./App";
import { BrowserRouter as Router, Route } from "react-router-dom";
import registerServiceWorker from "./registerServiceWorker";
import { PersistGate } from "redux-persist/integration/react";
import factory from "./Components/redux/store";

const { store, persistor } = factory();

const Login = () => <LoginForm />;

const SignUp = () => <SignUpForm />;

const app = () => <App />;

// const store;
// const persistor;

ReactDOM.render(
  <Provider store={store}>
    <PersistGate persistor={persistor}>
      <Router>
        <div className="App">
          <Route exact path="/" component={Login} />
          <Route path="/signup" component={SignUp} />
          <Route path="/app" component={app} />
        </div>
      </Router>
    </PersistGate>
  </Provider>,

  document.getElementById("root")
);
registerServiceWorker();
