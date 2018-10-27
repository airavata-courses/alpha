
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
     
    stage('CI') {
      steps {
         sh 'cd $WORKSPACE/stock-ms/ && npm test'
      }
    } 
    stage('deploy stocks'){
        steps{
            sh '''
        JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.170.132 'rm -rf stocks
        echo "y" | sudo apt-get install nodejs
        echo "y" | sudo apt-get install npm
        fuser -k 8000/tcp || true
        git clone -b ms-stocks https://github.com/airavata-courses/alpha.git stocks
        cd stocks/stock-ms
        node server.js' &
        '''
        }
    }
  }
}
