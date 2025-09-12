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
        MINIKUBE_HOME = '/var/jenkins_home/.minikube'
        KUBECONFIG = '/var/jenkins_home/.kube/config'
        K8S_MANIFESTS_DIR = 'k8s'
    }

    stages {
        stage('Verify Setup') {
            steps {
                script {
                    echo 'Verifying environment setup...'

                    // Проверяем основные инструменты
                    sh 'java -version'
                    sh 'mvn --version'
                    sh 'kubectl version --client'

                    // Проверяем манифесты
                    sh '''
                        echo "=== Checking Kubernetes manifests ==="
                        ls -la ${K8S_MANIFESTS_DIR}/ || echo "Manifests directory not found"

                        if [ -f "${K8S_MANIFESTS_DIR}/deployment.yaml" ]; then
                            echo "Deployment manifest found"
                            cat ${K8S_MANIFESTS_DIR}/deployment.yaml | head -10
                        fi

                        if [ -f "${K8S_MANIFESTS_DIR}/service.yaml" ]; then
                            echo "Service manifest found"
                            cat ${K8S_MANIFESTS_DIR}/service.yaml | head -10
                        fi
                    '''
                }
            }
        }

        stage('Fix Kubernetes Access') {
            steps {
                script {
                    echo 'Configuring Kubernetes access...'

                    sh '''
                        # Создаем базовый kubeconfig если не существует
                        if [ ! -f "${KUBECONFIG}" ]; then
                            echo "Creating kubeconfig..."
                            MINIKUBE_IP=$(minikube ip 2>/dev/null || echo "192.168.49.2")
                            mkdir -p $(dirname ${KUBECONFIG})
                            cat > ${KUBECONFIG} << EOF
apiVersion: v1
clusters:
- cluster:
    certificate-authority: ${MINIKUBE_HOME}/ca.crt
    server: https://${MINIKUBE_IP}:8443
  name: minikube
contexts:
- context:
    cluster: minikube
    user: minikube
  name: minikube
current-context: minikube
kind: Config
preferences: {}
users:
- name: minikube
  user:
    client-certificate: ${MINIKUBE_HOME}/profiles/minikube/client.crt
    client-key: ${MINIKUBE_HOME}/profiles/minikube/client.key
EOF
                        fi

                        # Исправляем пути в существующем kubeconfig
                        if [ -f "${KUBECONFIG}" ]; then
                            cp ${KUBECONFIG} ${KUBECONFIG}.backup
                            sed -i "s|/Users/.*/\\.minikube|${MINIKUBE_HOME}|g" ${KUBECONFIG}
                            echo "Kubeconfig updated successfully"
                        fi

                        # Проверяем доступ
                        echo "=== Testing Kubernetes access ==="
                        kubectl cluster-info && echo "✓ Kubernetes access successful" || echo "✗ Kubernetes access failed"
                    '''
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    echo 'Building Spring Boot application...'
                    sh 'mvn clean package -DskipTests'

                    // Архивируем артефакт
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Build and Load Docker Image') {
            steps {
                script {
                    echo 'Building Docker image and loading into Minikube...'

                    sh """
                        # Собираем Docker образ
                        docker build -t ${env.DOCKER_IMAGE} .

                        # Загружаем образ в Minikube
                        minikube image load ${env.DOCKER_IMAGE}

                        # Тегируем как latest для использования в манифестах
                        minikube image tag ${env.DOCKER_IMAGE} spring-app:latest

                        echo "✓ Docker image built and loaded: ${env.DOCKER_IMAGE}"

                        # Проверяем что образ доступен в Minikube
                        minikube image ls | grep spring-app || echo "⚠ Image not found in Minikube"
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo 'Deploying to Kubernetes using existing manifests...'

                    sh """
                        # Создаем namespace если нужно
                        kubectl create namespace ${env.KUBE_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -

                        echo "=== Applying manifests from ${K8S_MANIFESTS_DIR} ==="

                        # Применяем deployment
                        if [ -f "${K8S_MANIFESTS_DIR}/deployment.yaml" ]; then
                            kubectl apply -f ${K8S_MANIFESTS_DIR}/deployment.yaml --namespace=${env.KUBE_NAMESPACE}
                        else
                            echo "❌ deployment.yaml not found!"
                            exit 1
                        fi

                        # Применяем service
                        if [ -f "${K8S_MANIFESTS_DIR}/service.yaml" ]; then
                            kubectl apply -f ${K8S_MANIFESTS_DIR}/service.yaml --namespace=${env.KUBE_NAMESPACE}
                        else
                            echo "❌ service.yaml not found!"
                            exit 1
                        fi

                        echo "✓ Manifests applied successfully"
                    """
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    echo 'Verifying deployment...'

                    sh """
                        # Даем время для запуска подов
                        sleep 10

                        echo "=== Deployment Status ==="
                        kubectl get deployment spring-app --namespace=${env.KUBE_NAMESPACE} -o wide

                        echo "=== Pod Status ==="
                        kubectl get pods --namespace=${env.KUBE_NAMESPACE} -l app=spring-app -o wide

                        echo "=== Service Status ==="
                        kubectl get service spring-app --namespace=${env.KUBE_NAMESPACE} -o wide

                        echo "=== Pod Logs ==="
                        POD_NAME=\$(kubectl get pods --namespace=${env.KUBE_NAMESPACE} -l app=spring-app -o jsonpath='{.items[0].metadata.name}' 2>/dev/null)
                        if [ -n "\$POD_NAME" ]; then
                            kubectl logs \$POD_NAME --namespace=${env.KUBE_NAMESPACE} --tail=20
                        else
                            echo "No pods found for spring-app"
                        fi
                    """
                }
            }
        }

        stage('Get Application URL') {
            steps {
                script {
                    echo 'Getting application access information...'

                    sh """
                        echo "=== Application Access ==="

                        # Получаем URL через minikube service
                        minikube service spring-app --url --namespace=${env.KUBE_NAMESPACE} || \\
                        echo "Service URL not available. Use: minikube service spring-app --namespace=${env.KUBE_NAMESPACE}"

                        # Альтернативно: получаем NodePort
                        NODE_PORT=\$(kubectl get service spring-app --namespace=${env.KUBE_NAMESPACE} -o jsonpath='{.spec.ports[0].nodePort}')
                        MINIKUBE_IP=\$(minikube ip)
                        if [ -n "\$NODE_PORT" ] && [ -n "\$MINIKUBE_IP" ]; then
                            echo "Direct access: http://\${MINIKUBE_IP}:\${NODE_PORT}"
                        fi
                    """
                }
            }
        }
    }

    post {
        success {
            echo '🎉 Deployment completed successfully!'
            script {
                def appUrl = sh(
                    script: """
                        MINIKUBE_IP=\$(minikube ip 2>/dev/null || echo "192.168.49.2")
                        NODE_PORT=\$(kubectl get service spring-app --namespace=${env.KUBE_NAMESPACE} -o jsonpath='{.spec.ports[0].nodePort}' 2>/dev/null)
                        if [ -n "\$NODE_PORT" ]; then
                            echo "http://\${MINIKUBE_IP}:\${NODE_PORT}"
                        else
                            echo "Use: minikube service spring-app --namespace=${env.KUBE_NAMESPACE}"
                        fi
                    """,
                    returnStdout: true
                ).trim()

                echo "📦 Docker Image: ${env.DOCKER_IMAGE}"
                echo "🌐 Application URL: ${appUrl}"
                echo "📊 Namespace: ${env.KUBE_NAMESPACE}"
                echo "🚀 replicas: 2"
            }
        }
        failure {
            echo '❌ Deployment failed!'
            script {
                sh """
                    echo "=== Debug Information ==="
                    echo "Kubernetes pods:"
                    kubectl get pods --namespace=${env.KUBE_NAMESPACE} || true

                    echo "Kubernetes events:"
                    kubectl get events --namespace=${env.KUBE_NAMESPACE} --sort-by=.lastTimestamp | tail -10 || true

                    echo "Deployment details:"
                    kubectl describe deployment spring-app --namespace=${env.KUBE_NAMESPACE} || true
                """
            }
        }
        always {
            echo 'Pipeline execution completed'
            cleanWs()
        }
    }
}