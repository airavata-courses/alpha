pipeline {
  agent any
 
  tools {nodejs 'node'
       maven 'maven'
        jdk 'jdk'
  }
 
 environment {
        BUILD_ID = credentials('JENKINS_NODE_COOKIE')
    }
    
  stages {
    stage('kill all processes') {
      steps {
        sh 'fuser -k 9102/tcp || true '
        sh 'fuser -k 9101/tcp || true '
        sh 'fuser -k 5000/tcp || true '
        sh 'fuser -k 8000/tcp || true '
        sh 'fuser -k 3000/tcp || true '
      }
      }
    stage('deploy weather') {
      steps {
        sh 'rm -rf $WORKSPACE/'
        sh '''JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.170.138 'rm -rf weather
        echo "y" | sudo apt install openjdk-8-jdk
        echo "y" | sudo apt install maven
        mvn -v
        git clone -b ms-weather https://github.com/airavata-courses/alpha.git weather
        export WEATHER_API_KEY='9dca9959f87919bc8b8aaf9686ef2d9d'
        cd weather 
        mvn -q package 
        java -jar target/microservice-weather-*.jar ' &
        '''
      }
    }
    stage('deploy db') {
      steps {
        sh '''JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.169.102 'rm -rf db
        echo "y" | sudo apt install openjdk-8-jdk
        echo "y" | sudo apt install maven
        echo "y" | sudo apt-get install postgresql postgresql-contrib
        sudo /etc/init.d/postgresql stop && sudo /etc/init.d/postgresql start
        git clone -b ms-db https://github.com/airavata-courses/alpha.git db
        export DB_USER="demo"
        export DB_PASSWORD="p@ssw0rd"
        sed -i 's/DB_USER/'"$DB_USER"'/' db/dbscripts.sql
        sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' db/dbscripts.sql
        sudo psql -U postgres < ./dbscripts.sql 
        cd db
        mvn package
        java -jar target/microservice-database-*.jar ' &
        '''
      }
        }
    stage('deploy news') {
      steps {
        sh '''JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.170.184 'rm -rf news
        export NEWS_API_KEY='2ee42b7d4d0a4575aa73168cdf303854'
        git clone -b ms-news https://github.com/airavata-courses/alpha.git news
        cd news && pip install -r requirements.txt
        python app.py ' &
        '''
      }
        }
        
    stage('deploy stocks') {
      steps {
        sh '''
        JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.170.132 'rm -rf stocks
        echo "y" | sudo apt-get install nodejs
        echo "y" | sudo apt-get install npm
        git clone -b ms-stocks https://github.com/airavata-courses/alpha.git stocks
        cd stocks/stock-ms
        node server.js' &
        '''
      }
        }
        
    stage('deploy kafka') {
      steps {
          sh 'echo'
      }
    }
    
    stage('deploy react ') {
      steps {
        sh '''
        git clone -b react-ui https://github.com/airavata-courses/alpha.git react
        cd $WORKSPACE/react/react-ui && npm install && npm install --save react-scripts
        JENKINS_NODE_COOKIE=dontKillMe nohup npm start &
        '''
      }
    }    
  }
}
