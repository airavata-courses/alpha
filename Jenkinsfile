pipeline {
  agent { label 'weather_slave' }
  
 environment {
       WEATHER_API_KEY    = credentials('WEATHER_API_KEY')
       JENKINS_NODE_COOKIE = credentials('JENKINS_NODE_COOKIE')

    }
tools {
        maven 'maven'
        jdk 'jdk'
    }
 
  stages {
    stage('Build') {
      steps {
          sh 'rm -rf $WORKSPACE'
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
     
    stage('CI') {
      steps {
         sh 'mvn clean package'
      }
    } 
    
    stage('Deploy') {
      steps {
        sh ''' 
        fuser -k 9102/tcp || true
        JENKINS_NODE_COOKIE=dontKillMe java -jar target/microservice-weather-*.jar  &
        '''
      }
    }
  }
}
