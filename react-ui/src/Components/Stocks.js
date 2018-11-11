import React, { Component } from "react";
import { subscribeToStock } from "./stockclient";
import { connect } from "react-redux";
import { getip } from "./getip";

class Stocks extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isError: false,
      appleStock: "not yet set",
      isStockUp: false,
      fbStock: "not set",
      stockValue: "36.556"
    };

    const callbackFn = function(value) {
      console.log("stock value", value);
      if (value == "Error") {
        this.setState({
          isError: true
        });
      }
      if (value == "Connection Failed") {
        this.setState({
          isError: true
        });
      }
      this.setState((prevState, props) => {
        value = value.toFixed(2);
        return { stockValue: value };
      });
    }.bind(this);
    console.log("this state result", this.state.result);
    getip("stocks").then(result =>
      subscribeToStock(this.props.company, result, callbackFn)
    );
  }

  componentDidMount() {}
  render() {
    const { isError } = this.state;
    if (isError) {
      return <div>Loading </div>;
    } else {
      return (
        <div className="Stocks">
          <div className="Stocks-intro">
            <div style={{ width: "100%" }}>
              <div
                style={{ border: "1px solid #ccc", backgroundColor: "Gray" }}
              >
                <h3>Stocks Update</h3>
              </div>
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
}

const mapStateToProps = state => {
  console.log("state in stocks", state);
  return {
    company: state.userPreferences.company
  };
};

Stocks = connect(mapStateToProps)(Stocks);
export { Stocks };
