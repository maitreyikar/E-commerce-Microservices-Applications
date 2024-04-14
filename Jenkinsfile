pipeline {
    agent any

    stages {
        stage('Build Jar Files') {
            steps {
		
                dir('product-service') {
                    bat 'mvn clean package'
                }
                dir('user-service') {
                    bat 'mvn clean package'
                }
                dir('order-service') {
                    bat 'mvn clean package'
                }
            }
        }
    
        stage('Build Docker Images') {
            steps {
                dir('product-service') {
                    bat 'docker build -t product-service .'
                }
                dir('user-service') {
                    bat 'docker build -t user-service .'
                }
                dir('order-service') {
                    bat 'docker build -t order-service .'
                }
            }
        }
        stage('Push Docker Images to Docker Hub') {
            steps {
                script {
                    bat 'docker tag mysql pes2ug21cs270maitreyikar/mysql:1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-credentials')
                    {
                        bat 'docker push pes2ug21cs270maitreyikar/mysql:1.0'          	
                    }
                    bat 'docker tag product-service pes2ug21cs270maitreyikar/product-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-credentials')
                    {
                        bat 'docker push pes2ug21cs270maitreyikar/product-service:version1.0'          	
                    }
                    bat 'docker tag user-service pes2ug21cs270maitreyikar/user-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-credentials')
                    {
                        bat 'docker push pes2ug21cs270maitreyikar/user-service:version1.0'          	
                    }
                    bat 'docker tag order-service pes2ug21cs270maitreyikar/order-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-credentials')
                    {
                        bat 'docker push pes2ug21cs270maitreyikar/order-service:version1.0'          	
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script{
                    bat 'minikube start'
                    bat 'kubectl apply -f mysql-secret.yaml'
                    bat 'kubectl apply -f mysql-storage.yaml'
                    bat 'kubectl apply -f mysql-deployment.yaml'
                    bat 'kubectl apply -f product-service\product-service-deployment.yaml'
                    bat 'kubectl apply -f user-service\user-service-deployment.yaml'
                    bat 'kubectl apply -f order-service\order-service-deployment.yaml'
                }
            }
        } 
        stage('Access services') {
            steps {
                script{
                    bat 'powershell.exe sleep 80'
                    bat 'minikube service product-service'
                    bat 'minikube service user-service'
                    bat 'minikube service order-service'
                }
            }
        } 
    }

    post{
        failure{
            error 'Pipeline failed'
        }
    }
}