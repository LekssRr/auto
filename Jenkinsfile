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

        stage('Build Docker Image') {
            steps {
                script {
                    // Используем Docker daemon от Minikube
                    sh """
                        eval \$(minikube docker-env)
                        docker build -t ${env.DOCKER_IMAGE} .
                        docker tag ${env.DOCKER_IMAGE} spring-app:latest
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
                    sh "kubectl apply -f k8s/ --namespace=${env.KUBE_NAMESPACE} || echo 'No k8s manifests found'"

                    // Если деплоймента нет - создаем базовый
                    sh """
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

                        # Обновляем образ в деплойменте
                        kubectl set image deployment/spring-app \
                            spring-app=${env.DOCKER_IMAGE} \
                            --namespace=${env.KUBE_NAMESPACE}

                        # Ждем готовности
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
                    // Проверяем статус
                    sh "kubectl get pods --namespace=${env.KUBE_NAMESPACE}"
                    sh "kubectl get services --namespace=${env.KUBE_NAMESPACE}"
                    sh "kubectl get deployments --namespace=${env.KUBE_NAMESPACE}"

                    // Получаем URL приложения
                    def appUrl = sh(
                        script: "minikube service spring-app --url --namespace=${env.KUBE_NAMESPACE}",
                        returnStdout: true
                    ).trim()
                    echo "Application deployed successfully! URL: ${appUrl}"

                    // Простая проверка здоровья (ждем немного)
                    sleep 30
                    sh "curl -s ${appUrl}/actuator/health || echo 'Health endpoint not available yet'"
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
                def appUrl = sh(
                    script: "minikube service spring-app --url --namespace=${env.KUBE_NAMESPACE}",
                    returnStdout: true
                ).trim()
                echo "🎉 Application URL: ${appUrl}"
            }
        }
        failure {
            echo 'Build or deployment failed!'
            script {
                // Диагностика при ошибке
                sh "kubectl get pods --namespace=${env.KUBE_NAMESPACE}"
                sh "kubectl describe deployment/spring-app --namespace=${env.KUBE_NAMESPACE}"
                sh "kubectl logs -l app=spring-app --tail=50 --namespace=${env.KUBE_NAMESPACE} || true"
            }
        }
    }
}