#!groovy

node("java11") {
    try {
        env.JAVA_HOME = tool "java11"
        env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    } catch (Exception e) {
        println "Didnt find java 11 on path ${e.getMessage()}"
    }

    try {
        def gradle = "./gradlew"

        stage(name: "build") {
            // skip test for now
            sh "${gradle} clean build -x test"
        }

        stage(name: "test") {
            sh "${gradle} test"
        }

    } catch(Exception e) {
        println "error ${e.getMessage()}"
    }
}