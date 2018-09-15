import React, { Component } from 'react';

class News extends Component {

    constructor(props){
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            news: []
        }
        fetch("http://localhost:5000/top_headlines")
        .then(res => res.json())
        .then(
          (result) => {
              console.log(result)
            this.setState({
              isLoaded: true,
              news: result.news
            });
          }
        )
    }

    render() {
        const { error, isLoaded, news } = this.state;
        if (error) {
            return <div>Error: {error.message}</div>;
        }
        else if (!isLoaded) {
          return <div>Loading...</div>;
        }
        else {
            return (
                <ul>
                {news.map((item, id) => (
                    <li key={id}>
                    {item}
                    </li>
                ))}
                </ul>
            );
        }
    }
}


export {News};