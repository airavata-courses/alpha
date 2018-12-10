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
        sh '''
        fuser -k 9102/tcp || true
        mvn clean package
        '''
      }
    } 
    
    stage('Deploy') {
      steps {
        sh ''' 
            JENKINS_NODE_COOKIE=dontKillMe 
            echo "y" | sudo docker system prune -a
            sudo docker build -t weather .
            echo "y" | sudo apt install gnupg2 pass || true
            gpg2 -k || true 
            pass init "pass" || true
            sudo docker login --username=aishwaryadhage95 --password=vebsd1987 || true
            id=$(sudo docker images | grep -E 'weather' | awk -e '{print $3}')
            sudo docker tag $id aishwaryadhage95/ms-weather:1.0.0
            sudo docker push aishwaryadhage95/ms-weather:1.0.0
            
            JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.157.60 ' 
            rm -rf alpha
            git clone https://github.com/airavata-courses/alpha.git 
            cd alpha
            git checkout ms-weather
            kubectl delete deployment weather
            sleep 20
            kubectl apply -f deploy-weather.yml '
            
        '''
      }
    }
  }
}
