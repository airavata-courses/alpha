pipeline {
  agent any
 
  stages {
    stage('Build') {
      steps {
        git branch: 'ms-news', url: 'https://github.com/airavata-courses/alpha.git'
      }
    }
    stage('Install dependencies') {
      steps {
        sh 'unset PYTHONPATH && pip install -r requirements.txt'
      }
    }
     
    stage('Test') {
      steps {
         sh 'unset PYTHONPATH && python -m pytest tests/'
      }
    }      
  }
}