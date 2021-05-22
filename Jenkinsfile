#!groovy

pipeline {
    agent any

    def gradle = './gradlew'

    stages {
        stage('Build') {
            echo 'Building...'
            sh "${gradle} clean build -x test"
        }
        stage(name: "test") {
            sh "${gradle} test"
        }
    }
}
