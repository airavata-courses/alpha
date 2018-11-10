import React, { Component } from "react";
import { subscribeToStock } from "./stockclient";
import { connect } from "react-redux";

class Stocks extends Component {
  getip() {
    let res = fetch("http://149.165.157.99:8081/service/stocks", {
      method: "GET"
    })
      .then(res => {
        if (res.ok) {
          return res.json();
        } else {
          this.setState({
            isLoaded: false,
            error: "Error fetching data"
          });
        }
      })
      .then(result => {
        return result;
      });
    return res;
  }

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
        value = value.toFixed(2);
        return { stockValue: value };
      });
      // }
    }.bind(this);
    this.getip = this.getip.bind(this);
    // this.getip();
    console.log("this state result", this.state.result);
    this.getip().then(result =>
      subscribeToStock(this.props.company, result, callbackFn)
    );
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
