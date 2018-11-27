import React, { Component } from "react";
import { subscribeToStock } from "./stockclient";
import { connect } from "react-redux";
import { getip } from "./getip";

class Stocks extends Component {
  getStocks(port, ip) {
    // let country;
    // console.log("newss ip and port", ip, port);
    // console.log("props country", this.props.country);
    // if (this.props.country) {
    //   country = this.props.country;
    // } else {
    //   country = "us";
    // }
    // + ip + ":" + port
    fetch("http://" + ip + ":" + port + "/stocks/" + this.props.company)
      .then(res => {
        if (res.ok) {
          return res.json();
        } else {
          this.setState({
            isError: true
          });
        }
      })
      .then(result => {
        console.log(`stock value is returned ${result.value}`);
        this.setState({
          stockValue: result.value
        });
      })
      .catch(error => {
        this.setState({ isError: true });
      });
  }

  constructor(props) {
    super(props);
    this.state = {
      isError: false,
      appleStock: "not yet set",
      isStockUp: false,
      fbStock: "not set",
      stockValue: "36.556"
    };

    // const callbackFn = function(value) {
    //   console.log("stock value", value);
    //   if (value == "Error") {
    //     this.setState({
    //       isError: true
    //     });
    //   }
    //   if (value == "Connection Failed") {
    //     this.setState({
    //       isError: true
    //     });
    //   }
    //   this.setState((prevState, props) => {
    //     value = value.toFixed(2);
    //     return { stockValue: value };
    //   });
    // }.bind(this);
    console.log("this state result", this.state.result);
    // getip("stocks").then(result =>
    //   subscribeToStock(this.props.company, result, callbackFn)
    // );
  }

  componentDidMount() {
    let port;
    let ip;
    getip("stocks").then(result => {
      (port = result.port), (ip = result.address);
    });
    // this.getNews("news");
    setInterval(() => this.getStocks(port, ip), 3000);
  }

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
