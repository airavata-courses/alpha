import React, { Component } from "react";
import { connect } from "react-redux";
import { getip } from "./getip";
import { getNews } from "./redux/newsReducer";

class News extends Component {
  render() {
    const { news } = this.props;
    if (this.props.error) {
      return <div>Error: {this.props.error}</div>;
    }
    {
      return (
        <div style={{ border: "1px solid #ccc", width: "60%", margin: "auto" }}>
          <div style={{ border: "1px solid #ccc", backgroundColor: "gray" }}>
            <h3 style={{ textAlign: "center" }}>NEWS FEED: </h3>
          </div>

          {news.map((item, id) => (
            <div>
              <div
                style={{
                  border: "1px solid #ccc ",
                  padding: "10px",
                  display: "flex",
                  justifyContent: "left"
                }}
                key={id}
              >
                <img
                  src={require("./news_images/News.jpeg")}
                  style={{ width: "50px", height: "50px", margin: "left" }}
                />
                <div style={{ padding: "15px" }}>{item}</div>
              </div>
            </div>
          ))}
        </div>
      );
    }
  }

  componentDidMount() {
    // let port;
    // let ip;
    // getip("news").then(result => {
    //   console.log("news ip port", result),
    //     (port = result.port),
    //     (ip = result.address);
    // });
    // this.getNews("news");
    setInterval(
      () => this.props.getNews(this.props.port, this.props.ip),
      30000
    );
  }
}

const mapStateToProps = state => {
  console.log("News state", state);
  return {
    city: state.UserReducer.userPreferences.city,
    country: state.UserReducer.userPreferences.country,
    company: state.UserReducer.userPreferences.company,
    subscribedToNewsAlerts:
      state.UserReducer.userPreferences.subscribedToNewsAlert,
    ip: state.NewsReducer.ip,
    port: state.NewsReducer.port,
    error: state.NewsReducer.error,
    news: state.NewsReducer.news
  };
};

News = connect(
  mapStateToProps,
  { getNews }
)(News);
export { News };
