pipeline {
  agent any
 
tools {
        maven 'maven'
        jdk 'jdk'
    }
 
  stages {
    stage('Build') {
      steps {
        git branch: 'ms-weather', url: 'https://github.com/airavata-courses/alpha.git'
      }
    }
    stage('Install dependencies') {
      steps {
        sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
      }
    }
     
    stage('Test') {
      steps {
         sh 'mvn clean package'
      }
    }      
  }
}