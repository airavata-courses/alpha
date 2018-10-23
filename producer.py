from kafka import KafkaProducer
import json
from time import sleep
import requests
import os


def connect_kafka_producer():
    producer = None
    producer = KafkaProducer(bootstrap_servers=['localhost:9092'], value_serializer=lambda x: json.dumps(x).encode('utf-8'), api_version=(0, 10))
    return producer

try:
    api_key = os.environ["NEWS_API_KEY"]
    producer_instance = connect_kafka_producer()
    while True:
        # get data from DB here and do the following for each country. 
        # Store pairs of country and user list so we won't need to do multiple requests

        # Request for data from the DB endpoint
        r = requests.get(
                "http://149.165.168.71:9101/newssubscribers"
            )

        if r.status_code == 200 and r.json()["status"] == 200:
            data = r.json()
        else:
            sleep(60)
            continue
        
        data = json.loads(data["message"])

        for group in data:
            country = group["country"]
            # Now we request the NEWS API for each country's news
            r = requests.get(
                    "https://newsapi.org/v2" + "/top-headlines",
                    headers={"X-Api-Key": api_key},
                    params={"country": country, "pageSize": 1}
                )
            print(r.json())
            if r.status_code == 200:
                for emailid in json.loads(group["usernames"]):
                    # If request is successful then send out an email
                    print(country+emailid)
                    data = r.json()
                    data = {
                        "toAddr": emailid,
                        "news": {
                            "title": data["articles"][0]["title"],
                            "content": data["articles"][0]["description"]
                        }
                    }

                    producer_instance.send("topic_2", value=data)
                    producer_instance.flush()
                    print('Message published successfully.')
        
        sleep(1800) 

except Exception as ex:
    print('Exception while connecting Kafka')
    print(str(ex))
