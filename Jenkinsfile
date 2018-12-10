
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
    // stage('Install dependencies') {
    //   steps {
    //     sh 'cd $WORKSPACE/stock-ms/ && npm install --save-dev mocha'
    //     sh 'cd $WORKSPACE/stock-ms/ && npm install'
    //   }
    // }
     
    stage('CI') {
      steps {
         sh '''
         fuser -k 8000/tcp || true
         '''
      }
    } 
    stage('deploy stocks'){
        steps{
            sh '''    
        JENKINS_NODE_COOKIE=dontKillMe
        echo "y" | sudo docker system prune -a
        sudo docker build -t stocks .
        sudo apt install gnupg2 pass || true
        gpg2 -k || true 
        pass init "pass" || true
        sudo docker login --username=aishwaryadhage95 --password=vebsd1987 || true
        id=$(sudo docker images | grep -E 'stocks' | awk -e '{print $3}')
        sudo docker tag $id aishwaryadhage95/ms-stocks:1.0.0
        sudo docker push aishwaryadhage95/ms-stocks:1.0.0
        JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.157.60 ' 
        rm -rf alpha
        git clone https://github.com/airavata-courses/alpha.git 
        cd alpha
        git checkout ms-stocks
        kubectl delete deployment stocks
        sleep 20
        kubectl apply -f deploy-stocks.yml 
        ' 
        JENKINS_NODE_COOKIE=dontKillMe cd $WORKSPACE/stock-ms && node server.js &
        '''
        }
    }
    
  }
  
}
