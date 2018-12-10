pipeline {
  agent { label 'db_slave'}
 
  tools {
       maven 'maven'
        jdk 'jdk'
  }
  
  environment {
        DB_USER = credentials('DB_USER')
        DB_PASSWORD = credentials('DB_PASSWORD')
        JENKINS_NODE_COOKIE= credentials('JENKINS_NODE_COOKIE')
        DOCKER_USERNAME = credentials('docker_username')
        DOCKER_PASSWORD = credentials('docker_password')
  }
  
  stages {
    stage('Build'){
        steps{
            sh 'rm -rf $WORKSPACE'
            git branch: 'ms-db', url: 'https://github.com/airavata-courses/alpha.git'
        }
    }
    
    // stage('Setup DB'){
    //     steps{
    //         sh '''sed -i 's/DB_USER/'"$DB_USER"'/' $WORKSPACE/dbscripts.sql
    //         sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' $WORKSPACE/dbscripts.sql'''
    //     }
    // }
        
    // stage('ci'){
    //     steps{
    //         sh '''
    //         echo "y" | sudo apt-get install postgresql postgresql-contrib
    //         fuser -k 9101/tcp || true
    //         sudo /etc/init.d/postgresql start
    //         sudo psql -U postgres < ./dbscripts.sql 
    //         mvn clean package
    //         '''
    //     }
    // }
    
    stage('deploy'){
        steps{
            sh'''
            fuser -k 9101/tcp || true
            JENKINS_NODE_COOKIE=dontKillMe 
            echo "y" | sudo docker rmi -f db
            sudo docker build -t db .
            echo "y" | sudo apt install gnupg2 pass || true
            gpg2 -k || true 
            pass init "pass" || true
            sudo docker login --username=DOCKER_USERNAME --password=DOCKER_PASSWORD || true
            id=$(sudo docker images -q db)
            sudo docker tag $id aishwaryadhage95/ms-db:1.0.0
            sudo docker push aishwaryadhage95/ms-db:1.0.0
            
            JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.157.60 ' 
            rm -rf alpha
            git clone https://github.com/airavata-courses/alpha.git 
            cd alpha
            git checkout ms-db
            kubectl delete deployment db
            sleep 20
            kubectl apply -f deploy-db.yml ' 

            '''    
            }
        }
     } 
 }
