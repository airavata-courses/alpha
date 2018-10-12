[![Build Status](https://travis-ci.org/airavata-courses/alpha.svg?branch=ms-db)](https://travis-ci.org/airavata-courses/alpha/branches)  
  
To be able to use the database microservice, make sure you have Java 8+, Apache Maven 3.5+ and PostgreSQL 9+ setup on your system.  

Below commands should execute successfully printing out the versions.
```
1. java -version
2. mvn --version
3. psql --version
```

Once Java, Maven and Postgres are successfully setup, run the below scripts to get the microservice running on your machine.
Once its up and running, you can receive data from the microservice on port 9101.
```
git clone https://github.com/airavata-courses/alpha --branch=ms-db
cd alpha
psql -U postgres < ./dbscripts.sql
mvn clean package
java -jar target/microservice-database-*.jar
```
