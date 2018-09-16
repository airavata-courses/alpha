from flask import Flask, jsonify, request
import requests
import json
from flask_cors import CORS
import os

# Connects to news.org API to get  latest news.
# Requires news.org API key stored in config.json file


def flask_news(api_key):

    app = Flask(__name__)
    CORS(app)
    url = "https://newsapi.org/v2"
    # For removing x_hr warning
    app.config["JSONIFY_PRETTYPRINT_REGULAR"] = False

    @app.route('/', methods=['GET'])
    def index():
        return "Hello, World!"

    @app.route('/top_headlines', methods=['GET'])
    def get_news():
        """
            Returns top 10 news from news.org API for the input country.
            Default country is USA.

            Output Format:
            {
                news: [
                    0: "news 1",
                    1: "news 2",
                    ...
                ]
            }
        """

        country = request.args.get('country', 'us')
        r = requests.get(
            url + "/top-headlines",
            headers={"X-Api-Key": api_key},
            params={"country": country, "pageSize": 10}
        )

        if r.status_code == 200:
            result = {"success": 1, "news": []}
            for news in r.json()["articles"]:
                result["news"].append(news["title"])
            return jsonify(result)

        return jsonify({
            "success": 0,
            "error": "Unable to fetch news"
        })

    return app


if __name__ == '__main__':

    app = flask_news(os.environ["NEWS_API_KEY"])
    app.run(host='0.0.0.0')
