pipeline {
    agent any

    triggers {
        githubPush()
    }
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-arm64'
        PATH = "${JAVA_HOME}/bin:/usr/share/maven/bin:${PATH}"
    }

    stages {
        stage('Check Tools') {
            steps {
                script {
                    echo 'Checking available tools...'
                    sh 'java -version'
                    sh 'mvn --version'
                }
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    // Используйте встроенную БД для тестов (H2, SQLite)
                    sh 'mvn clean package -DskipTests' // или настройте тесты без Docker
                }
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts 'target/*.jar'
            }
        }
    }

    post {
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}