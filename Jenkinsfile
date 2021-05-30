#!groovy

pipeline {
    agent any

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
                        sh "chmod +x publish.sh"
                        docker
                        sh "./publish.sh"
                    }
                }
            }
        }
    }
}
