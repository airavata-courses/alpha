import React, { Component } from "react";
import { connect } from "react-redux";

class News extends Component {
  getNews() {
    let country;

    console.log("props country", this.props.country);
    // console.log(this.props.country);
    if (this.props.country) {
      country = this.props.country;
      // country = this.props.country;
    } else {
      country = "us";
    }

    // console.log("inside news" + country);
    fetch("http://149.165.157.99:5000/top_headlines?country=" + country)
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

    this.getNews = this.getNews.bind(this);
    // this.getNews();

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
        <div
          style={{ border: "2px solid black", width: "70%", margin: "auto" }}
        >
          <ul>
            {news.map((item, id) => (
              <li key={id}>{item}</li>
            ))}
          </ul>
        </div>
      );
    }
  }

  componentDidMount() {
    // setInterval(this.getNews, 300000);
    this.getNews();
    setInterval(() => this.getNews(), 1000);
    // this.getNews;
  }
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
