[![Build Status](https://travis-ci.org/airavata-courses/alpha.svg?branch=ms-weather)](https://travis-ci.org/airavata-courses/alpha)  
  
To be able to use the weather microservice you should first install Java and Apache Maven on your system.  

Check if Java is installed properly by executing the below command
  ```
java -version
```  

Check if Maven is installed by executing the below command
```
mvn -v
```
After installing Java and Maven, run the below scripts to get the microservice running on your machine.
Once its up and running, you can receive data from the microservice on port 9102 and /data path
```
git clone https://github.com/airavata-courses/alpha --branch=ms-weather
cd alpha
mvn clean package
java -jar target/microservice-weather-*.jar
```
