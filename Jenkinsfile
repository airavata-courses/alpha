
pipeline {
  agent any
 
  tools {nodejs 'node'
  }
  
    stages {
stage('deploy react ') {
      steps {
        sh '''
        git clone -b react-ui https://github.com/airavata-courses/alpha.git react
        cd $WORKSPACE/react/react-ui && npm install && npm install --save react-scripts
        fuser -k 3000/tcp || true
        JENKINS_NODE_COOKIE=dontKillMe nohup npm start &
        '''
      }
    }
    }
}
