[![Build Status](https://travis-ci.org/airavata-courses/alpha.svg?branch=ms-db)](https://travis-ci.org/airavata-courses/alpha/branches)  
  
To be able to use the database microservice, make sure you have Java 8+, Apache Maven 3.5+ and PostgreSQL 9+ setup on your system. Make sure to start postgresql service after installing PostgreSQL.

Below commands should execute successfully printing out the versions.
```
1. java -version
2. mvn --version
3. psql --version
```

Now, replace username and password in the below script with the ones of your choice. I have given default values of 'demo' and 'p@ssw0rd'. Run the below script to get the microservice up and running on your system. You can then receive data from the microservice on port 9101.

```
echo 'export DB_USER="demo"' >> ~/.bashrc
echo 'export DB_PASSWORD="p@ssw0rd"' >> ~/.bashrc
mkdir ms-db
git clone https://github.com/airavata-courses/alpha --branch=ms-db ms-db
cd ms-db
sed -i 's/DB_USER/'"$DB_USER"'/' dbscripts.sql
sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' dbscripts.sql
sudo -u postgres psql < dbscripts.sql
mvn --quiet package
java -jar target/microservice-database-*.jar
```
