pipeline {
  agent any
 environment {
       WEATHER_API_KEY    = credentials('WEATHER_API_KEY')
    }
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
     
    stage('CI') {
      steps {
         sh 'mvn clean package'
      }
    } 
    
    stage('Deploy') {
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
  }
}
