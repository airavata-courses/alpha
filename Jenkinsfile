pipeline {
  
  agent {label 'stocks_slave' }
  
  environment {
        NEWS_API_KEY = credentials('NEWS_API_KEY')
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
         sh 'unset PYTHONPATH && pip3 install -r requirements.txt'
       }
     }
     
    stage('CI') {
      steps {
         sh 'fuser -k 5000/tcp || true'
        sh 'python3 -m pytest tests/'
      }
    } 
    
    stage('Deploy Docker') {
      steps {
         sh '''
            JENKINS_NODE_COOKIE=dontKillMe 
            echo "y" | sudo docker system prune -a
            sudo docker build -t news .
            sudo apt install gnupg2 pass || true
            gpg2 -k || true 
            pass init "pass" || true
            sudo docker login --username=aishwaryadhage95 --password=vebsd1987 || true
            id=$(sudo docker images | grep -E 'news' | awk -e '{print $3}')
            sudo docker tag $id aishwaryadhage95/ms-news:1.0.0
            sudo docker push aishwaryadhage95/ms-news:1.0.0
            
            JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.157.60 ' 
            rm -rf alpha
            git clone https://github.com/airavata-courses/alpha.git 
            cd alpha
            git checkout ms-news
            kubectl delete deployment news
            sleep 20
            kubectl apply -f deploy-news.yml 
            ' 
            JENKINS_NODE_COOKIE=dontKillMe python3 app.py &
            
           
            '''
      }
    }
    
  }
}
