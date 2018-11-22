pipeline {
  
  agent {label 'news_slave' }
  
  environment {
        NEWS_API_KEY     = credentials('NEWS_API_KEY')
        JENKINS_NODE_COOKIE = credentials('JENKINS_NODE_COOKIE')
    }
  stages {
    stage('Build') {
      steps {
          sh 'rm -rf $WORKSPACE'
        git branch: 'ms-news', url: 'https://github.com/airavata-courses/alpha.git'
      }
    }
    stage('Install dependencies') {
      steps {
        sh 'unset PYTHONPATH && pip install -r requirements.txt'
      }
    }
     
    stage('CI') {
      steps {
         sh 'python -m pytest tests/'
      }
    } 
    
    stage('Deploy') {
      steps {
         sh '''
            fuser -k 5000/tcp || true
            JENKINS_NODE_COOKIE=dontKillMe python app.py &
            '''
      }
    }
    
  }
}
