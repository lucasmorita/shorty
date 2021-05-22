#!groovy

pipeline {
    agent any
    sh "pwd"
    def gradle = './gradlew'

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                sh "${gradle} clean build -x test"
            }
        }
        stage(name: "test") {
            steps {
                sh "${gradle} test"
            }
        }
    }
}
