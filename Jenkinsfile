pipeline {
    agent any
    tools {
        maven 'M3'
        jdk 'jdk17'
    }
    stages {
        stage('Check Docker') {
            steps {
                script {
                    // Проверяем что Docker доступен
                    sh 'docker --version'
                    echo 'Docker is available!'
                }
            }
        }

        stage('Start PostgreSQL') {
            steps {
                script {
                    // Останавливаем и удаляем старый контейнер если есть
                    sh 'docker stop postgres-test || true'
                    sh 'docker rm postgres-test || true'

                    // Запускаем PostgreSQL контейнер
                    sh '''
                    docker run -d --name postgres-test \\
                      -e POSTGRES_DB=autodb \\
                      -e POSTGRES_USER=user \\
                      -e POSTGRES_PASSWORD=password \\
                      -e POSTGRES_HOST_AUTH_METHOD=trust \\
                      -p 5435:5432 \\
                      postgres:15
                    '''

                    // Ждем пока PostgreSQL запустится
                    sleep 15

                    // Проверяем что PostgreSQL работает
                    sh '''
                    docker exec postgres-test pg_isready -U user -d autodb || echo "PostgreSQL is not ready yet"
                    '''
                }
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    // Запускаем сборку с тестами
                    sh 'mvn clean package'
                }
            }
        }

        stage('Archive Artifact') {
            steps {
                // Сохраняем собранный JAR файл
                archiveArtifacts 'target/*.jar'
            }
        }
    }

    post {
        always {
            // Всегда очищаем контейнеры, даже если были ошибки
            script {
                sh '''
                docker stop postgres-test || true
                docker rm postgres-test || true
                echo "Cleanup completed"
                '''
            }
        }

        success {
            // Действия при успешной сборке
            echo 'Build completed successfully!'
        }

        failure {
            // Действия при неудачной сборке
            echo 'Build failed!'
        }
    }
}