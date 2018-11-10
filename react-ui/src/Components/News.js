import React, { Component } from "react";
import { connect } from "react-redux";

class News extends Component {
  getip() {
    let res = fetch("http://149.165.157.99:8081/service/news", {
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

  getNews(port, ip) {
    let country;
    console.log("newss ip and port", ip, port);
    console.log("props country", this.props.country);
    //console.log(this.props.country);
    if (this.props.country) {
      country = this.props.country;
      // country = this.props.country;
    } else {
      country = "us";
    }
    let url = "http://" + ip + ":" + port + "/top_headlines?country=" + country;
    console.log("news url", url);
    // console.log("inside news" + country);
    fetch("http://" + ip + ":" + port + "/top_headlines?country=" + country)
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
        this.setState({
          isLoaded: true,
          news: result.news,
          error: 0
        });
      });
  }

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      news: []
    };
    this.getip = this.getip.bind(this);
    // this.getNews(result.port, result.ip);
    this.getNews = this.getNews.bind(this);
    //"""Make this work, then add a call if ip fails"""
    // this.componentDidMount = this.componentDidMount.bind(this);
  }

  render() {
    const { error, isLoaded, news } = this.state;
    if (error) {
      return <div>Error: {error}</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else {
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
    let port;
    let ip;
    this.getip().then(
      result => {
        (port = result.port), (ip = result.address);
      }
      // this.getNews(result.port, result.ip)
    );

    // this.getNews();
    setInterval(() => this.getNews(port, ip), 3000);
  }

  // componentWillReceiveProps({ country }) {
  //   // setInterval(this.getNews, 300000);
  //   this.getNews();
  //   // setInterval(() => this.getNews(), 1000);
  //   // this.getNews;
  // }
}
const mapStateToProps = state => {
  console.log("News state", state);
  return {
    city: state.userPreferences.city,
    country: state.userPreferences.country,
    company: state.userPreferences.company,
    subscribedToNewsAlerts: state.userPreferences.subscribedToNewsAlerts
  };
};

News = connect(mapStateToProps)(News);
export { News };
