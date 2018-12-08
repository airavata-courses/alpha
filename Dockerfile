FROM openjdk:8-jdk-alpine

EXPOSE 9102
COPY . /app
WORKDIR /app

RUN apk add maven --update-cache --repository http://dl-4.alpinelinux.org/alpine/edge/community/ --allow-untrusted \
	&& rm -rf /var/cache/apk/*

ENV MAVEN_HOME /usr/share/java/maven-3
ENV PATH $PATH:$MAVEN_HOME/bin

#ENV WEATHER_API_KEY="9dca9959f87919bc8b8aaf9686ef2d9d"
ARG WEATHER_API_KEY
ENV WEATHER_API_KEY=WEATHER_API_KEY

#RUN mvn clean package
RUN chmod 777 run-weather.sh
#ADD target/microservice-weather-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["sh","run-weather.sh"]

