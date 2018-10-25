pipeline {
  agent any
 
  tools {nodejs 'node'
       maven 'maven'
        jdk 'jdk'
  }
 
 environment {
        WEATHER_API_KEY    = credentials('WEATHER_API_KEY')
        NEWS_API_KEY     = credentials('NEWS_API_KEY')
        DB_USER = credentials('DB_USER')
        DB_PASSWORD = credentials('DB_PASSWORD')
        BUILD_ID = credentials('JENKINS_NODE_COOKIE')
    }
    
    
  stages {
    stage('CI for news'){
        steps{
        sh 'rm -rf news && git clone -b ms-news https://github.com/airavata-courses/alpha.git news'
        sh 'cd $WORKSPACE/news && pip install -r requirements.txt'
        sh 'cd $WORKSPACE/news && python -m pytest tests/'
    }
    }
    stage('CI for weather'){
        steps{
        sh 'rm -rf weather && git clone -b ms-weather https://github.com/airavata-courses/alpha.git weather'
            sh 'cd $WORKSPACE/weather && mvn clean package'

        }
    }
    stage('CI for stocks'){
        steps{
        sh '''
        rm -rf stocks
        git clone -b ms-stocks https://github.com/airavata-courses/alpha.git stocks 
        cd $WORKSPACE/stocks/stock-ms/ && npm install --save-dev mocha
        cd $WORKSPACE/stocks/stock-ms/ && npm install
        cd $WORKSPACE/stocks/stock-ms/ && npm test &
        
        '''

        }
    }
    
    stage('CI for db'){
        steps{
            sh 'sudo /etc/init.d/postgresql start'
            sh '''
            rm -rf db
            git clone -b ms-db https://github.com/airavata-courses/alpha.git db
            sed -i 's/DB_USER/'"$DB_USER"'/' $WORKSPACE/db/dbscripts.sql
            sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' $WORKSPACE/db/dbscripts.sql
            psql -U postgres < $WORKSPACE/db/dbscripts.sql
            cd db
            mvn clean package
            '''
            
        }
    }
    stage('deploy weather') {
      steps {
        sh 'rm -rf $WORKSPACE/'
        sh '''JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.170.138 'rm -rf weather
        echo "y" | sudo apt install openjdk-8-jdk
        fuser -k 9102/tcp || true
        echo "y" | sudo apt install maven
        export WEATHER_API_KEY='9dca9959f87919bc8b8aaf9686ef2d9d'
        mvn -v
        git clone -b ms-weather https://github.com/airavata-courses/alpha.git weather
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
        export DB_USER="demo"
        export DB_PASSWORD="p@ssw0rd"
        fuser -k 9101/tcp || true
        echo "y" | sudo apt install maven
        echo "y" | sudo apt-get install postgresql postgresql-contrib
        sudo /etc/init.d/postgresql stop
        sudo /etc/init.d/postgresql start
        git clone -b ms-db https://github.com/airavata-courses/alpha.git db
        sed -i 's/DB_USER/'"$DB_USER"'/' db/dbscripts.sql
        sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' db/dbscripts.sql
        sudo psql -U postgres < ./dbscripts.sql 
        cd db
        mvn install
        java -jar target/microservice-database-*.jar ' &
        '''
      }
        }
    stage('deploy news') {
      steps {
        sh '''JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.170.184 'rm -rf news
        fuser -k 5000/tcp || true
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
        fuser -k 8000/tcp || true
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
    stage('deploy kafka-ms'){
        steps{
            sh '''
            JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.156.133 'rm -rf kafka
            fuser -k 2181/tcp || true
            fuser -k 9102/tcp || true
            git clone -b ms-kafka https://github.com/airavata-courses/alpha.git kafka
            cd kafka
            wget https://www-us.apache.org/dist/kafka/2.0.0/kafka_2.11-2.0.0.tgz
            tar -xvzf kafka_2.11-2.0.0.tgz
            cd kafka_2.11-2.0.0
            bin/zookeeper-server-start.sh config/zookeeper.properties
            bin/kafka-server-start.sh config/server.properties
            bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic topic_news --config retention.ms=6000 
            cd ..
            pip install -r requirements.txt
            python producer.py > producer.txt
            python consumer.py > consumer.txt' &
            '''
        }
    }
    stage('deploy react ') {
      steps {
        sh '''
        git clone -b react-ui https://github.com/airavata-courses/alpha.git react
        cd $WORKSPACE/react/react-ui && npm install && npm install --save react-scripts
        fuser -k 3000/tcp || true
        JENKINS_NODE_COOKIE=dontKillMe nohup npm start &
        '''
      }
    }    
  }
}
