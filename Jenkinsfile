 pipeline {
  agent { label 'news_slave'}
 
 environment {
        NEWS_API_KEY = credentials('NEWS_API_KEY')
        EMAIL_PASSWORD = credentials('EMAIL_PASSWORD')
        EMAIL_ID = credentials('EMAIL_ID')
        JENKINS_NODE_COOKIE = credentials('JENKINS_NODE_COOKIE')
    }
    
      stages {
    
    stage('deploy kafka-ms'){
        steps{
            sh '''
            pkill -f "producer.py" || true
            pkill -f "consumer.py" || true
            rm -rf kafka
            git clone -b ms-kafka https://github.com/airavata-courses/alpha.git kafka
            cd kafka
            pip install -r requirements.txt
            JENKINS_NODE_COOKIE=dontKillMe python producer.py > producer.txt &
            JENKINS_NODE_COOKIE=dontKillMe python consumer.py > consumer.txt &
            '''
        }
    }
    }
    }
