pipeline {
  agent any
 
  tools {nodejs 'node'
       maven 'maven'
        jdk 'jdk'
  }
 
 environment {
        NEWS_API_KEY     = credentials('NEWS_API_KEY')
        WEATHER_API_KEY = credentials('WEATHER_API_KEY')
        DB_USER = credentials('DB_USER')
        DB_PASSWORD = credentials('DB_PASSWORD')
        BUILD_ID = credentials('JENKINS_NODE_COOKIE')
    }
    
  stages {
    stage('Clone all repos') {
      steps {
        sh 'rm -rf $WORKSPACE/'
        sh 'git clone -b ms-weather https://github.com/airavata-courses/alpha.git weather'
        sh 'git clone -b ms-db https://github.com/airavata-courses/alpha.git db'
        sh 'git clone -b ms-news https://github.com/airavata-courses/alpha.git news'
        sh 'git clone -b ms-stocks https://github.com/airavata-courses/alpha.git stocks'
        sh 'git clone -b react-ui https://github.com/airavata-courses/alpha.git react'
      }
    }
    
    stage('install dependencies') {
      steps {
          sh 'echo "y" | sudo apt-get install postgresql postgresql-contrib'
          sh 'sudo /etc/init.d/postgresql stop && sudo /etc/init.d/postgresql start'
         sh '''
    fuser -k 5000/tcp || true
    fuser -k 3000/tcp || true
    fuser -k 8000/tcp || true
    fuser -k 9101/tcp || true
    fuser -k 9102/tcp || true
        '''
      }
    }
    
    stage('Start deploying') {
      steps {
        sh '''
        cd $WORKSPACE/db

        sed -i 's/DB_USER/'"$DB_USER"'/' $WORKSPACE/db/dbscripts.sql
        sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' $WORKSPACE/db/dbscripts.sql
        sudo psql -U postgres < ./dbscripts.sql 
        mvn -q package
        JENKINS_NODE_COOKIE=dontKillMe java -jar target/microservice-database-*.jar &
        cd $WORKSPACE/news && pip install -r requirements.txt
        JENKINS_NODE_COOKIE=dontKillMe nohup python app.py &
        cd $WORKSPACE/stocks/stock-ms && JENKINS_NODE_COOKIE=dontKillMe nohup node server.js  &
        cd $WORKSPACE/weather && mvn -q package && JENKINS_NODE_COOKIE=dontKillMe nohup java -jar target/microservice-weather-*.jar & 
        cd $WORKSPACE/react/react-ui && npm install && npm install --save react-scripts
        JENKINS_NODE_COOKIE=dontKillMe nohup npm start &
        '''
        
      }
    }    
  }
}
