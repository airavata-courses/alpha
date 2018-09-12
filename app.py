from flask import Flask, jsonify, request
import requests
import json

app = Flask(__name__)
url = "https://newsapi.org/v2"

with open('config.json') as f:
    config_json = json.load(f)


@app.route('/', methods=['GET'])
def index():
    return "Hello, World!"


@app.route('/top_headlines', methods=['GET'])
def get_country():

    country = request.args.get('country', 'us')
    r = requests.get(
        url+"/top-headlines",
        headers={"X-Api-Key": config_json["news_api_key"]},
        params={"country": country, "pageSize": 10}
    )

    if r.status_code == 200:
        result = {"news": []}
        for news in r.json()["articles"]:
            result["news"].append(news["title"])
        return jsonify(result)

    return jsonify({
        "success": 0,
        "error": "Unable to fetch news"
    })


if __name__ == '__main__':
    app.run()
