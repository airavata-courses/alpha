from flask import Flask, jsonify, request, abort
import requests
import json
from flask_cors import CORS
import os
import time
import logging
import multiprocessing
import redis


# Connects to news.org API to get latest news.
# Requires news.org API key stored in config.json file
logging.basicConfig(filename="/var/tmp/news_logs", format="%(asctime)s - %(message)s", level=logging.INFO)


def flask_news(api_key):

    app = Flask(__name__)
    CORS(app)
    url = "https://newsapi.org/v2"
    # For removing x_hr warning
    app.config["JSONIFY_PRETTYPRINT_REGULAR"] = False

    @app.route("/", methods=['GET'])
    def index():
        return "Use /top_headlines endpoint to get latest headlines. Add country to get country specific news. " \
               "Default country is USA."

    @app.route("/top_headlines", methods=["GET"])
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
            country = request.args.get("country", "us")
            redis_client = redis.Redis(host='localhost', port=6379, db=0)
            if redis_client.get(country) is None:
                logging.info(f"Sending request to get news for country {country}")
                r = requests.get(
                    url + "/top-headlines",
                    headers={"X-Api-Key": api_key},
                    params={"country": country, "pageSize": 10}
                )

                if r.status_code == 200:
                    logging.info("Get News request completed successfully.")
                    result = {"success": 1, "news": []}
                    for news in r.json()["articles"]:
                        result["news"].append(news["title"])
                    redis_client.set(country, json.dumps(result["news"]), ex=1800)
                    return jsonify(result)
            else:
                logging.info("Data present in redis, not sending request to API")
                result = {"success": 1, "news": json.loads(redis_client.get(country))}
                return jsonify(result)

            logging.error(f"Get News request failed. Received status code {r.status_code}")

            response = app.response_class(
                response=json.dumps({"success": 0, "error": "Unable to fetch data"}),
                status=r.status_code,
                mimetype='application/json'
            )

            return response

        except Exception as e:
            logging.exception("Exception occured")

    return app


def zk_heartbeat(heartbeat=30):
    """
        Sends heartbeat to zookeeper every few seconds as per configuration of heartbeat
    """

    data = json.dumps({
        "serviceName": "news",
        "instanceId": "_2",
        "address": "149.165.170.184",
        "port": 5000
    })

    while True:
        r = requests.put("http://149.165.157.99:8081/service", data=data, headers={"Content-type": "application/json"})
        logging.info(f"Sent heartbeat. Got response {r.status_code}")
        time.sleep(heartbeat)


if __name__ == "__main__":

    try:
        app = flask_news(os.environ["NEWS_API_KEY"])
        # fork and if child process make heart beat requests to ZK else start the server
        p = multiprocessing.Process(target=zk_heartbeat)
        p.daemon = True
        p.start()
        app.run(host="0.0.0.0")

    except KeyError as e:
        print("News API key is not set in Environment Variable")
