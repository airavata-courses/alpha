
pipeline {
  agent { label 'master' }
 
 environment {
        JENKINS_NODE_COOKIE = credentials('JENKINS_NODE_COOKIE')
    }
    
  tools {nodejs 'node'
  }
  
    stages {
        stage('clone repo'){
            steps{
                sh 'rm -rf $WORKSPACE'
                git branch: 'react-ui', url: 'https://github.com/airavata-courses/alpha.git'
            }
        }
        stage('install dependencies'){
            steps{
             sh '''cd $WORKSPACE/react-ui 
                 npm install 
                 npm install --save react-scripts'''
 
            }
        }
stage('deploy react ') {
      steps {
        sh '''
        fuser -k 3000/tcp || true
        cd $WORKSPACE/react-ui
        JENKINS_NODE_COOKIE=dontKillMe npm start &
        '''
      }
    }
    }
}
