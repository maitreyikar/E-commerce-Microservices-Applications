pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'https://registry.hub.docker.com'
        DOCKER_REGISTRY_CREDENTIALS = 'docker-registry-auth'
        KUBERNETES_MANIFESTS_DIR = './kubernetes-manifests'
    }

    stages {
        stage('Build Jar Files') {
            steps {
                dir('product-service') {
                    sh 'mvn clean package'
                }
                dir('user-service') {
                    sh 'mvn clean package'
                }
                dir('order-service') {
                    sh 'mvn clean package'
                }
            }
        }
    
        stage('Build Docker Images') {
            steps {
                dir('product-service') {
                    sh 'docker build -t product-service .'
                }
                dir('user-service') {
                    sh 'docker build -t user-service .'
                }
                dir('order-service') {
                    sh 'docker build -t order-service .'
                }
            }
        }
        
        stage('Push Docker Images to Docker Hub') {
            steps {
                script {
                    docker.withRegistry(DOCKER_REGISTRY, DOCKER_REGISTRY_CREDENTIALS) {
                        sh 'docker tag mysql malayshikhar/mysql:1.0'
                        sh 'docker push malayshikhar/mysql:1.0'
                        
                        sh 'docker tag product-service malayshikhar/product-service:1.0'
                        sh 'docker push malayshikhar/product-service:version1.0'
                        
                        sh 'docker tag user-service malayshikhar/user-service:1.0'
                        sh 'docker push malayshikhar/user-service:1.0'
                        
                        sh 'docker tag order-service malayshikhar/order-service:1.0'
                        sh 'docker push malayshikhar/order-service:1.0'
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh 'kubectl apply -f mysql-secret.yaml'
                    sh 'kubectl apply -f mysql-storage.yaml'
                    sh 'kubectl apply -f mysql-deployment.yaml'
                    
                    sh "kubectl apply -f ${KUBERNETES_MANIFESTS_DIR}/product-service-deployment.yaml"
                    sh "kubectl apply -f ${KUBERNETES_MANIFESTS_DIR}/user-service-deployment.yaml"
                    sh "kubectl apply -f ${KUBERNETES_MANIFESTS_DIR}/order-service-deployment.yaml"
                }
            }
        } 
    }

    post {
        failure {
            error 'Pipeline failed'
        }
    }
}
