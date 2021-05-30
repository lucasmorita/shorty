#!groovy

pipeline {
    agent { label 'docker'}

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                sh "./gradlew clean build -x test"
            }
        }
        stage('Test') {
            steps {
                sh "./gradlew test --fail-fast"
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == "master") {
                        sh '''
                        echo "Deploying..."
                        docker-compose stop shorty 
                        docker-compose rm -f shorty
                        docker-compose up --build -d
                        '''
                    }
                }
            }
        }
    }
}
