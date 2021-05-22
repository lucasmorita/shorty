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
                sh "./gradlew test --no-daemon"
            }
        }
    }
}
