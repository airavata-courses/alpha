import React from "react";
import { withRouter } from "react-router-dom";

class SignUpForm extends React.Component {
  createUser(user) {
    return fetch("http://149.161.203.120:9101/signup", {
      method: "post",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(user)
    })
      .then(res => {
        console.log("res ", res);
        if (res.ok) {
          return res.json();
        }
      })

      .then(result => {
        console.log("result", result);
        return result;
      });
  }
  //   checkCredentials() {
  //     fetch("https://jsonplaceholder.typicode.com/posts", {
  //       method: "post",
  //       headers: {
  //         "Content-Type": "application/json"
  //       },
  //       body: JSON.stringify(this.state)
  //     })
  //       .then(res => res.json())
  //       .then(result => {
  //         return result;
  //       });
  //   }
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      password_confirmation: "",
      city: "",
      country: "",
      company: "",
      subscribedToNewsAlerts: "",
      subscribedToWeatherAlerts: ""
    };
    this.onChange = this.onChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  onChange(e) {
    this.setState({ [e.target.name]: e.target.value });
  }

  onSubmit(e) {
    e.preventDefault();
    let user = {
      credentials: {
        username: this.state.username,
        password: this.state.password
      },
      userPreferences: {
        city: this.state.city,
        country: this.state.country,
        company: this.state.company,
        subscribedToNewsAlerts: this.state.subscribedToNewsAlerts,
        subscribedToWeatherAlerts: this.state.subscribedToWeatherAlerts
      }
    };
    //to check if user already exists, return should be either 409 or -1(to be done)
    this.createUser(user).then(userId => {
      console.log("userid" + userId.message);
      if (userId.status === 409) {
        console.log("user already exists");
        alert("user already exists");
      } else if (userId.status === 200) {
        this.props.history.push("/");
      }
    });
  }

  render() {
    return (
      <form onSubmit={this.onSubmit}>
        <h1>SignUp</h1>

        <div className="form-group">
          <label className="control-label">Name</label>
          <input
            value={this.state.username}
            onChange={e => this.onChange(e)}
            type="text"
            name="username"
            className="form-control"
          />
        </div>

        <div className="form-group">
          <label className="control-label">Password</label>
          <input
            value={this.state.password}
            onChange={this.onChange}
            type="password"
            name="password"
            className="form-control"
          />
        </div>

        <div className="form-group">
          <label className="control-label">Confirm Password</label>
          <input
            value={this.state.password_confirmation}
            onChange={this.onChange}
            type="password"
            name="password_confirmation"
            className="form-control"
          />
        </div>

        <div className="form-group">
          <label className="control-label">City</label>
          <input
            value={this.state.city}
            onChange={this.onChange}
            type="text"
            name="city"
            className="form-control"
          />
        </div>
        <div className="form-group">
          <label className="control-label">Country</label>
          <select>
            <option value={this.state.country}>US</option>
            <option value={this.state.country}>AU</option>
            <option selected value={this.state.country}>
              JP
            </option>
          </select>
          <input
            value="cv"
            onChange={this.onChange}
            type="text"
            name="country"
            className="form-control"
          />
        </div>

        <div className="form-group">
          <label className="control-label">Company</label>
          <input
            value={this.state.company}
            onChange={this.onChange}
            type="text"
            name="company"
            className="form-control"
          />
        </div>

        <div
          className="form-group"
          //   onChange={event => this.state.subscribedToNewsAlerts}
        >
          <label className="control-label">News Alert:</label>
          <input
            type="radio"
            value="true"
            name="subscribedToNewsAlerts"
            checked={this.state.subscribedToNewsAlerts === "true"}
            onChange={this.onChange}
            className="form-control"
          />
          ON
          <input
            type="radio"
            value="false"
            name="subscribedToNewsAlerts"
            checked={this.state.subscribedToNewsAlerts === "false"}
            onChange={this.onChange}
            className="form-control"
          />
          OFF
        </div>

        <div
          className="form-group"
          //   onChange={event => this.state.subscribedToWeatherAlerts}
        >
          <label className="control-label">Weather Alert:</label>
          <input
            type="radio"
            value="true"
            name="subscribedToWeatherAlerts"
            checked={this.state.subscribedToWeatherAlerts === "true"}
            onChange={this.onChange}
            className="form-control"
          />
          ON
          <input
            type="radio"
            value="false"
            name="subscribedToWeatherAlerts"
            checked={this.state.subscribedToWeatherAlerts === "false"}
            onChange={this.onChange}
            className="form-control"
          />
          OFF
        </div>

        <div className="form-group">
          <button className="btn btn-primary btn-lg">SignUp</button>

          <button
            onClick={() => {
              this.props.history.push("/");
            }}
          >
            Login
          </button>
        </div>
      </form>
    );
  }
}

export default withRouter(SignUpForm);
