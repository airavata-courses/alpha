import React, { Component } from "react";
import { subscribeToStock } from "./stockclient";
import { connect } from "react-redux";
import { getip } from "./getip";
import { getStocks } from "./redux/stocksReducer";

class Stocks extends Component {
  componentDidMount() {
    setInterval(
      () =>
        this.props.getStocks(
          this.props.ip,
          this.props.port,
          this.props.company
        ),
      300000
    );
  }

  render() {
    // const { isError } = this.state;
    if (this.props.error) {
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
                {this.props.company}: {this.props.stocks}
              </h3>
            </div>
          </div>
        </div>
      );
    }
  }
}

const mapStateToProps = state => {
  console.log("state in stocks", state.StocksReducer.stocks);
  return {
    company: state.UserReducer.userPreferences.company,
    ip: state.StocksReducer.ip,
    stocks: state.StocksReducer.stocks,
    port: state.StocksReducer.port,
    error: state.StocksReducer.error
  };
};

Stocks = connect(
  mapStateToProps,
  { getStocks }
)(Stocks);
export { Stocks };
