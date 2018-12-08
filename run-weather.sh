#!/bin/bash
  
mvn clean package
java -jar target/microservice-weather-1.0-SNAPSHOT.jar
