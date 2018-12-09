#FROM postgres:10.0-alpine
FROM openjdk:8-jdk-alpine  
#FROM alpine:edge
EXPOSE 9101
EXPOSE 5432

COPY . /app

WORKDIR /app

ENV DB_USER='demo'
ENV DB_PASSWORD='p@ssw0rd'

USER root
#RUN echo "postgres ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers
#RUN apk add --no-cache openjdk8
RUN apk add maven --update-cache --repository http://dl-4.alpinelinux.org/alpine/edge/community/ --allow-untrusted \
        && rm -rf /var/cache/apk/*
#RUN echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf
#RUN chmod 777 /app  && sed -i 's/DB_USER/'"$DB_USER"'/' /app/dbscripts.sql && sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' /app/dbscripts.sql
#USER postgres
#RUN chmod 0700 /var/lib/postgresql/data &&\
#    initdb /var/lib/postgresql/data &&    echo "host all  all    0.0.0.0/0  md5" >> /var/lib/postgresql/data/pg_hba.conf &&\
#    echo "host all all ::/0 md5" >> /var/lib/postgresql/data/pg_hba.conf &&\
#    echo "listen_addresses='*'" >> /var/lib/postgresql/data/postgresql.conf && pg_ctl start && psql < /app/dbscripts.sql 
#USER roo
RUN chmod 777 /app/startdb.sh
#RUN mvn clean package
#ADD target/microservice-database-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["sh","startdb.sh"]
#ENTRYPOINT ["mvn","clean","package"]
#ENTRYPOINT ["java","-jar","/app/target/microservice-database-1.0-SNAPSHOT.jar"]
