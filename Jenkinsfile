pipeline {
  agent { label 'master' }
 
 environment {
        JENKINS_NODE_COOKIE = credentials('JENKINS_NODE_COOKIE')
        DOCKER_USERNAME = credentials('docker_username')
        DOCKER_PASSWORD = credentials('docker_password')
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
        // stage('install dependencies'){
        //     steps{
        //      sh '''cd $WORKSPACE/react-ui 
        //          npm install 
        //          npm install --save react-scripts'''
 
        //     }
        // }
stage('deploy react ') {
      steps {
        sh '''
        
        JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.157.60 ' 
            rm -rf alpha
            git clone https://github.com/airavata-courses/alpha.git 
            cd alpha
            git checkout react-ui
            echo "y" | sudo docker rmi -f react
            rm -rf react-ui/package-lock.json
            sudo docker build -t react .
            echo "y" | sudo apt install gnupg2 pass || true
            echo "y" | gpg2 -k || true 
            echo "y" | pass init "pass" || true
            sudo docker login --username=DOCKER_USERNAME --password=DOCKER_PASSWORD || true
            id=$(sudo docker images -q react)
            sudo docker tag $id aishwaryadhage95/react-ui:1.0.0
            sudo docker push aishwaryadhage95/react-ui:1.0.0
            kubectl delete deployment react
            sleep 20
            kubectl apply -f deploy-react.yml 
            ' 
        '''
      }
    }
    }
}
