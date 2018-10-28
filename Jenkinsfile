pipeline {
  
  agent { label 'stocks_slave'}
 
  tools {nodejs 'node'}
 
 environment {
        JENKINS_NODE_COOKIE = credentials('JENKINS_NODE_COOKIE')
    }
    
  stages {
    stage('Build') {
      steps {
        sh 'rm -rf $WORKSPACE'
        git branch: 'ms-stocks', url: 'https://github.com/airavata-courses/alpha.git'
      }
    }
    stage('Install dependencies') {
      steps {
        sh 'cd $WORKSPACE/stock-ms/ && npm install --save-dev mocha'
        sh 'cd $WORKSPACE/stock-ms/ && npm install'
      }
    }
     
    stage('CI') {
      steps {
         sh '''
         fuser -k 8000/tcp || true
         cd $WORKSPACE/stock-ms/ && npm test
         '''
      }
    } 
    stage('deploy stocks'){
        steps{
            sh '''
        fuser -k 8000/tcp || true
        JENKINS_NODE_COOKIE=dontKillMe cd $WORKSPACE/stock-ms && node server.js &
        '''
        }
    }
    
  }
  
}
