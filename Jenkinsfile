pipeline {
  
  agent any
  
  environment {
        NEWS_API_KEY     = credentials('NEWS_API_KEY')
    }
  stages {
    stage('Build') {
      steps {
        git branch: 'ms-news', url: 'https://github.com/airavata-courses/alpha.git'
      }
    }
    stage('Install dependencies') {
      steps {
        sh 'unset PYTHONPATH && pip install -r requirements.txt'
      }
    }
     
    stage('CI') {
      steps {
         sh 'unset PYTHONPATH && python -m pytest tests/'
      }
    } 
    
    stage('Deploy') {
      steps {
         sh '''JENKINS_NODE_COOKIE=dontKillMe nohup ssh -tt ubuntu@149.165.170.184 'rm -rf news
                fuser -k 5000/tcp || true
                export NEWS_API_KEY='2ee42b7d4d0a4575aa73168cdf303854'
                git clone -b ms-news https://github.com/airavata-courses/alpha.git news
                cd news && pip install -r requirements.txt
                python app.py ' &
            '''
      }
    }
    
  }
}
