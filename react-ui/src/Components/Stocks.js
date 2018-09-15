import React, { Component } from "react";
import { subscribeToStock } from "./stockclient";

class Stocks extends Component {
  constructor(props) {
    super(props);
    this.state = {
      appleStock: "not yet set",
      isStockUp: false,
      fbStock: "not set"
    };

    const callbackFn = function(str, arg) {
      this.setState((prevState, props) => {
        if (str == "apple") {
          return {
            appleStock: arg
          };
        } else if (str == "fb") {
          return {
            fbStock: arg
          };
        }
      });
      // }
    }.bind(this);
    subscribeToStock("apple", callbackFn);
    subscribeToStock("fb", callbackFn);
  }

  render() {
    return (
      <div className="Stocks">
        <p className="Stocks-intro">
          <h3> Apple stock value is : {this.state.appleStock} </h3>
          <h3> FB stock value is : {this.state.fbStock} </h3>
        </p>
      </div>
    );
  }
}

export { Stocks };
