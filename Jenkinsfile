pipeline {
    agent any
    tools {
        maven 'M3'
        jdk 'jdk17'
    }
    stages {
        stage('Start PostgreSQL') {
            steps {
                sh '''
                docker run -d --name postgres-test \
                  -e POSTGRES_DB=autodb \
                  -e POSTGRES_USER=user \
                  -e POSTGRES_PASSWORD=password \
                  -p 5435:5432 \
                  postgres:15
                sleep 15
                '''
            }
        }
        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Cleanup') {
            steps {
                sh 'docker stop postgres-test || true'
                sh 'docker rm postgres-test || true'
            }
        }
    }
}