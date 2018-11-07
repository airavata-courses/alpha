from flask import Flask, jsonify, request, abort
import requests
import json
from flask_cors import CORS
import os
import time

# Connects to news.org API to get latest news.
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

        try:
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

            response = app.response_class(
                response=json.dumps({"success": 0, "error": "Unable to fetch data"}),
                status=r.status_code,
                mimetype='application/json'
            )

            return response

        except Exception as e:
            print(str(e))

    return app


def zk_heartbeat(heartbeat=30):

    data = json.dumps({
        "serviceName": "news",
        "instanceId": "_1",
        "address": "149.165.170.184",
        "port": 5000
    })

    while True:
        r = requests.put('http://149.165.157.99:8081/service', data=data, headers={'Content-type':'application/json'})
        print(r.status_code)
        time.sleep(heartbeat)


if __name__ == '__main__':

    try:
        app = flask_news(os.environ["NEWS_API_KEY"])
        # fork and if child process make heart beat requests to ZK else start the server
        newpid = os.fork()
        if newpid == 0:
            zk_heartbeat()
        else:
            app.run(host='0.0.0.0')

    except KeyError as e:
        print("News API key is not set in Environment Variable")
