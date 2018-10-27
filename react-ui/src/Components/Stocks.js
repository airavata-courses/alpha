import React, { Component } from "react";
import { subscribeToStock } from "./stockclient";
import { connect } from "react-redux";

class Stocks extends Component {
  constructor(props) {
    super(props);
    this.state = {
      appleStock: "not yet set",
      isStockUp: false,
      fbStock: "not set",
      stockValue: "36.556"
    };

    const callbackFn = function(value) {
      console.log("stock value", value);
      this.setState((prevState, props) => {
        // if (str == "apple") {
        //   return {
        //     appleStock: value
        //   };
        // } else if (str == "fb") {
        //   return {
        //     fbStock: arg
        //   };
        // }
        value = value.toFixed(2);
        return { stockValue: value };
      });
      // }
    }.bind(this);
    subscribeToStock(this.props.company, callbackFn);

    // const callbackFn = function(value) {
    //   this.setState((prevState, props) => {
    //     // if (str == "apple") {
    //     //   return {
    //     //     appleStock: value
    //     //   };
    //     // } else if (str == "fb") {
    //     //   return {
    //     //     fbStock: arg
    //     //   };
    //     // }
    //     return { stockValue: value };
    //   });
    //   // }
    // }.bind(this);
    // subscribeToStock(this.props.company, callbackFn);
    // subscribeToStock(this.props.company, callbackFn);
  }

  componentDidMount() {}

  render() {
    return (
      <div className="Stocks">
        <div className="Stocks-intro">
          <div style={{ width: "100%" }}>
            <div style={{ border: "1px solid #ccc", backgroundColor: "Gray" }}>
              <h3>Stocks Update</h3>
            </div>
            {/* <h3> Apple stock value is : {this.state.appleStock} </h3>
            <h3> FB stock value is : {this.state.fbStock} </h3> */}
            <h3>
              {" "}
              {this.props.company}: {this.state.stockValue}
            </h3>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => {
  console.log("state in stocks", state);
  return {
    company: state.userPreferences.company
  };
};

Stocks = connect(mapStateToProps)(Stocks);
export { Stocks };

// export { Stocks };
