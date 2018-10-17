import React, { Component } from "react";

class News extends Component {
  getNews() {
    fetch("http://localhost:5000/top_headlines")
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
    this.componentDidMount = this.componentDidMount.bind(this);
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
    setInterval(() => this.getNews(), 300000);
  }
}

export { News };
