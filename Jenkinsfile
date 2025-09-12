pipeline {
    agent any

    triggers {
        githubPush()
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-arm64'
        PATH = "${JAVA_HOME}/bin:/usr/share/maven/bin:${PATH}"
        DOCKER_IMAGE = "spring-app:${env.BUILD_NUMBER}"
        KUBE_NAMESPACE = "default"
    }

    stages {
        stage('Fix Kubernetes Config') {
            steps {
                script {
                    echo 'Fixing Kubernetes configuration...'

                    // Полностью пересоздаем kubeconfig с правильными путями
                    sh '''
                        # Создаем новый kubeconfig с правильными путями
                        cat > /var/jenkins_home/.kube/config << 'EOL'
apiVersion: v1
clusters:
- cluster:
    certificate-authority: /var/jenkins_home/.minikube/ca.crt
    server: https://127.0.0.1:61665
  name: minikube
contexts:
- context:
    cluster: minikube
    user: minikube
    namespace: default
  name: minikube
current-context: minikube
kind: Config
users:
- name: minikube
  user:
    client-certificate: /var/jenkins_home/.minikube/profiles/minikube/client.crt
    client-key: /var/jenkins_home/.minikube/profiles/minikube/client.key
EOL

                        echo "New kubeconfig created with correct paths:"
                        cat /var/jenkins_home/.kube/config
                    '''

                    // Проверяем доступ
                    sh 'kubectl cluster-info && echo "Kubernetes access OK" || echo "Kubernetes access failed"'
                }
            }
        }

        stage('Check Tools') {
            steps {
                script {
                    echo 'Checking available tools...'
                    sh 'java -version'
                    sh 'mvn --version'
                    sh 'docker --version'
                    sh 'kubectl version --client'
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build and Load Docker Image') {
            steps {
                script {
                    sh '''
                        # Настраиваем доступ к Docker daemon Minikube
                        eval $(minikube docker-env)

                        # Собираем Docker образ
                        docker build -t ${env.DOCKER_IMAGE} .
                        docker tag ${env.DOCKER_IMAGE} spring-app:latest

                        echo "Docker images:"
                        docker images | grep spring-app
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh '''
                        # Создаем деплоймент если не существует
                        if ! kubectl get deployment spring-app 2>/dev/null; then
                            kubectl create deployment spring-app \
                                --image=spring-app:latest \
                                --port=8088

                            kubectl expose deployment spring-app \
                                --type=NodePort \
                                --port=80 \
                                --target-port=8088
                        fi

                        # Обновляем образ
                        kubectl set image deployment/spring-app spring-app=${env.DOCKER_IMAGE}

                        # Ждем развертывания
                        kubectl rollout status deployment/spring-app --timeout=120s
                    '''
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    sh '''
                        echo "=== Deployment Status ==="
                        kubectl get deployment spring-app

                        echo "=== Pod Status ==="
                        kubectl get pods -l app=spring-app

                        echo "=== Service Status ==="
                        kubectl get service spring-app
                    '''
                }
            }
        }

        stage('Get Application URL') {
            steps {
                script {
                    sh '''
                        echo "=== Application URL ==="
                        minikube service spring-app --url || echo "Getting URL failed"
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed'
        }
        success {
            echo '✅ Deployment successful!'
            script {
                def appUrl = sh(
                    script: "minikube service spring-app --url 2>/dev/null || echo 'not-available'",
                    returnStdout: true
                ).trim()
                echo "Application URL: ${appUrl}"
            }
        }
        failure {
            echo '❌ Deployment failed!'
            script {
                // Простая диагностика без kubectl (на случай если он не работает)
                sh '''
                    echo "=== Basic diagnostics ==="
                    echo "Docker images in Minikube:"
                    eval $(minikube docker-env)
                    docker images | grep spring-app || echo "No spring-app images found"

                    echo "=== Minikube status ==="
                    minikube status || echo "Minikube status check failed"
                '''
            }
        }
    }
}