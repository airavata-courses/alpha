pipeline {
  agent any
 
  tools {nodejs 'node'}
 
  stages {
    stage('Build') {
      steps {
        git branch: 'ms-stocks', url: 'https://github.com/airavata-courses/alpha.git'
      }
    }
    stage('Install dependencies') {
      steps {
        sh 'cd $WORKSPACE/stock-ms/ && npm install --save-dev mocha'
        sh 'cd $WORKSPACE/stock-ms/ && npm install'
      }
    }
     
    stage('Test') {
      steps {
         sh 'cd $WORKSPACE/stock-ms/ && npm test'
      }
    }      
  }
}