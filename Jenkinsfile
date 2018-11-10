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
  }
  
  stages {
    stage('Build'){
        steps{
            sh 'rm -rf $WORKSPACE'
            git branch: 'ms-db', url: 'https://github.com/airavata-courses/alpha.git'
        }
    }
    
    stage('Setup DB'){
        steps{
            sh '''sed -i 's/DB_USER/'"$DB_USER"'/' $WORKSPACE/dbscripts.sql
            sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' $WORKSPACE/dbscripts.sql'''
        }
    }
        
    stage('ci'){
        steps{
            sh '''
            echo "y" | sudo apt-get install postgresql postgresql-contrib
            fuser -k 9101/tcp || true
            sudo /etc/init.d/postgresql stop
            sudo /etc/init.d/postgresql start
            sudo psql -U postgres < ./dbscripts.sql 
            mvn clean package
            '''
        }
    }
    
    stage('deploy'){
        steps{
            sh'''
            JENKINS_NODE_COOKIE=dontKillMe
            fuser -k 9101/tcp || true
            java -jar $WORKSPACE/target/microservice-database-*.jar & 
            '''    
            }
        }
     } 
 }
