
pipeline {
  agent any
 
  tools {
       maven 'maven'
        jdk 'jdk'
  }
  
  environment {
        DB_USER = credentials('DB_USER')
        DB_PASSWORD = credentials('DB_PASSWORD')
  }
  
  stages {
    stage('Build'){
        steps{
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
            psql -U postgres < $WORKSPACE/dbscripts.sql
            mvn clean package
            '''
        }
    }
    stage('deploy'){
        steps{
            sh'''
            JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.169.102 'rm -rf db
            echo "y" | sudo apt install openjdk-8-jdk
            export DB_USER="demo"
            export DB_PASSWORD="p@ssw0rd"
            fuser -k 9101/tcp || true
            echo "y" | sudo apt install maven
            echo "y" | sudo apt-get install postgresql postgresql-contrib
            sudo /etc/init.d/postgresql stop
            sudo /etc/init.d/postgresql start
            git clone -b ms-db https://github.com/airavata-courses/alpha.git db
            sed -i 's/DB_USER/'"$DB_USER"'/' db/dbscripts.sql
            sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' db/dbscripts.sql
            sudo psql -U postgres < ./dbscripts.sql 
            cd db
            mvn install
            java -jar target/microservice-database-*.jar '& 
            '''    
            }
        }
        
    }
}
