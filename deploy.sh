#!/bin/bash


sudo psql -U postgres < ./dbscripts.sql 
mvn -q package
java -jar target/microservice-database-*.jar &
cd $WORKSPACE/news && pip install -r requirements.txt
python app.py &
cd $WORKSPACE/stocks/stock-ms && nohup node server.js  &
cd $WORKSPACE/weather && mvn -q package && nohup java -jar target/microservice-weather-*.jar & 
cd $WORKSPACE/react/react-ui && npm install && npm install --save react-scripts
npm start &
