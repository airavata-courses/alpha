#!/bin/bash

mvn clean package
java -jar /app/target/microservice-database-1.0-SNAPSHOT.jar

