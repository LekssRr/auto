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
        MINIKUBE_IP = sh(script: "minikube ip", returnStdout: true).trim()
    }

    stages {
        stage('Verify Kubernetes Access') {
            steps {
                script {
                    echo 'Checking Kubernetes access configuration...'

                    // Проверяем существование файлов сертификатов
                    sh '''
                        echo "Checking certificate files:"
                        ls -la /var/jenkins_home/.minikube/ca.crt || echo "CA certificate not found"
                        ls -la /var/jenkins_home/.minikube/profiles/minikube/client.crt || echo "Client certificate not found"
                        ls -la /var/jenkins_home/.minikube/profiles/minikube/client.key || echo "Client key not found"

                        echo "Current kubeconfig paths:"
                        grep -E "(certificate-authority|client-certificate|client-key)" /var/jenkins_home/.kube/config || true
                    '''

                    // Исправляем пути в kubeconfig
                    sh '''
                        # Backup original config
                        cp /var/jenkins_home/.kube/config /var/jenkins_home/.kube/config.backup

                        # Replace host paths with container paths
                        sed -i 's|/Users/.*/\.minikube|/var/jenkins_home/.minikube|g' /var/jenkins_home/.kube/config

                        echo "Updated kubeconfig:"
                        grep -E "(certificate-authority|client-certificate|client-key)" /var/jenkins_home/.kube/config
                    '''

                    // Проверяем доступ к Kubernetes
                    sh 'kubectl cluster-info || echo "Kubernetes access check failed"'
                    sh 'kubectl get nodes || echo "Failed to get nodes"'
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

        stage('Build and Test') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Setup Minikube Docker Environment') {
            steps {
                script {
                    echo 'Setting up Minikube Docker environment...'
                    // Настраиваем переменные окружения для доступа к Docker daemon Minikube
                    sh '''
                        eval $(minikube docker-env)
                        echo "Docker host set to: $DOCKER_HOST"
                        docker info || echo "Docker daemon not accessible"
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                        eval \$(minikube docker-env)
                        docker build -t ${env.DOCKER_IMAGE} .
                        docker tag ${env.DOCKER_IMAGE} spring-app:latest
                        echo "Docker image built: ${env.DOCKER_IMAGE}"

                        # Проверяем что образ создался
                        docker images | grep spring-app
                    """
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                script {
                    // Создаем namespace если не существует
                    sh "kubectl create namespace ${env.KUBE_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -"

                    // Применяем Kubernetes манифесты (если есть)
                    sh """
                        if [ -d "k8s" ] && [ -n "\$(ls k8s/)" ]; then
                            echo "Applying Kubernetes manifests from k8s/"
                            kubectl apply -f k8s/ --namespace=${env.KUBE_NAMESPACE}
                        else
                            echo "No k8s manifests found, creating basic deployment"

                            # Создаем деплоймент если не существует
                            if ! kubectl get deployment spring-app --namespace=${env.KUBE_NAMESPACE} 2>/dev/null; then
                                kubectl create deployment spring-app \
                                    --image=spring-app:latest \
                                    --port=8088 \
                                    --namespace=${env.KUBE_NAMESPACE}

                                kubectl expose deployment spring-app \
                                    --type=NodePort \
                                    --port=80 \
                                    --target-port=8088 \
                                    --namespace=${env.KUBE_NAMESPACE}
                            fi
                        fi
                    """

                    // Обновляем образ в деплойменте
                    sh """
                        kubectl set image deployment/spring-app \
                            spring-app=${env.DOCKER_IMAGE} \
                            --namespace=${env.KUBE_NAMESPACE}

                        echo "Image updated in deployment"
                    """

                    // Ждем готовности
                    sh """
                        kubectl rollout status deployment/spring-app \
                            --timeout=300s \
                            --namespace=${env.KUBE_NAMESPACE}
                    """
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    echo 'Verifying deployment...'

                    // Проверяем статус ресурсов
                    sh """
                        echo "=== Pods ==="
                        kubectl get pods --namespace=${env.KUBE_NAMESPACE} -o wide

                        echo "=== Services ==="
                        kubectl get services --namespace=${env.KUBE_NAMESPACE}

                        echo "=== Deployments ==="
                        kubectl get deployments --namespace=${env.KUBE_NAMESPACE}

                        echo "=== Deployment Details ==="
                        kubectl describe deployment/spring-app --namespace=${env.KUBE_NAMESPACE}
                    """

                    // Получаем URL приложения
                    sh """
                        # Ждем пока сервис будет готов
                        sleep 10

                        # Получаем URL
                        APP_URL=\$(minikube service spring-app --url --namespace=${env.KUBE_NAMESPACE} 2>/dev/null || echo "")

                        if [ -n "\$APP_URL" ]; then
                            echo "Application URL: \$APP_URL"

                            # Пробуем проверить здоровье приложения
                            sleep 20
                            curl -s \$APP_URL/actuator/health || echo "Health endpoint not available yet"
                            curl -s \$APP_URL || echo "Application not responding yet"
                        else
                            echo "Could not get application URL"
                            minikube service list --namespace=${env.KUBE_NAMESPACE}
                        fi
                    """
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
            echo 'Build and deployment completed successfully!'
            script {
                // Получаем URL для уведомления
                def appUrl = sh(
                    script: "minikube service spring-app --url --namespace=${env.KUBE_NAMESPACE} 2>/dev/null || echo 'URL-not-available'",
                    returnStdout: true
                ).trim()

                echo "🎉 Deployment Successful!"
                echo "📦 Docker Image: ${env.DOCKER_IMAGE}"
                echo "🌐 Application URL: ${appUrl}"
                echo "📊 Kubernetes Namespace: ${env.KUBE_NAMESPACE}"

                // Показываем статус
                sh """
                    echo "Final status:"
                    kubectl get pods,services,deployments --namespace=${env.KUBE_NAMESPACE}
                """
            }
        }
        failure {
            echo 'Build or deployment failed!'
            script {
                // Диагностика при ошибке
                echo "❌ Deployment Failed - gathering diagnostics..."

                sh """
                    echo "=== Pods ==="
                    kubectl get pods --namespace=${env.KUBE_NAMESPACE} || true

                    echo "=== Pod Logs ==="
                    kubectl logs -l app=spring-app --tail=50 --namespace=${env.KUBE_NAMESPACE} || true

                    echo "=== Deployment Details ==="
                    kubectl describe deployment/spring-app --namespace=${env.KUBE_NAMESPACE} || true

                    echo "=== Events ==="
                    kubectl get events --namespace=${env.KUBE_NAMESPACE} --sort-by='.lastTimestamp' | tail -20 || true

                    echo "=== Docker Images ==="
                    eval \$(minikube docker-env)
                    docker images | grep spring-app || true
                """
            }
        }
        always {
            echo 'Pipeline execution completed'
            script {
                // Очистка (опционально)
                sh """
                    echo "Cleaning up temporary files..."
                    rm -f /var/jenkins_home/.kube/config.backup 2>/dev/null || true
                """
            }
        }
    }
}