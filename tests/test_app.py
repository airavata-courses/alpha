import pytest
import app
import os
import json


def test_home():
    test_app = app.flask_news(os.environ["NEWS_API_KEY"])
    test_client = test_app.test_client()
    test_app.testing = True
    home = test_client.get('/')
    assert b"Use /top_headlines endpoint" in home.data


def test_news():

    # Test if we are able to get news
    test_app = app.flask_news(os.environ["NEWS_API_KEY"])
    test_client = test_app.test_client()
    test_app.testing = True
    news = test_client.get('/top_headlines')
    assert news.status_code == 200

    # Test if we are able to get error when token is wrong
    test_app = app.flask_news("wrong_token")
    test_client = test_app.test_client()
    test_app.testing = True
    news = test_client.get('/top_headlines')
    assert news.status_code != 200

test_home()
