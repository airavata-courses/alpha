#!/bin/bash

export NEWS_API_KEY=NEWS_API
unset PYTHONPATH
pip install virtualenv
python -m virtualenv flask_env
source flask_env/bin/activate
cd $WORKSPACE/news
pip install -r requirements.txt
python app.py &
