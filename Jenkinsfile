pipeline {
    agent any
    stages {
        stage('Deploy MySQL to Kubernetes') {
            steps {
                script{
                    bat 'powershell.exe kubectl apply -f mysql-secret.yaml'
                    bat 'powershell.exe kubectl apply -f mysql-storage.yaml'
                    bat 'powershell.exe kubectl apply -f mysql-deployment.yaml'
                }
            }
        }
        stage('Build Jar Files') {
            steps {
                dir('product-service') {
                    bat 'powershell.exe mvn clean package'
                }
                dir('user-service') {
                    bat 'powershell.exe mvn clean package'
                }
                dir('order-service') {
                    bat 'powershell.exe mvn clean package'
                }
            }
        }
    
        stage('Build Docker Images') {
            steps {
                dir('product-service') {
                    bat 'powershell.exe docker build -t product-service .'
                }
                dir('user-service') {
                    bat 'powershell.exe docker build -t user-service .'
                }
                dir('order-service') {
                    bat 'powershell.exe docker build -t order-service .'
                }
            }
        }

        stage('Push Docker Images to Docker Hub') {
            steps {
                script {
                    bat 'powershell.exe docker tag product-service nikitamabel/product-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-auth')
                    {
                        bat 'powershell.exe docker push nikitamabel/product-service:version1.0'          	
                    }
                    bat 'powershell.exe docker tag user-service nikitamabel/user-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-auth')
                    {
                        bat 'powershell.exe docker push nikitamabel/user-service:version1.0'          	
                    }
                    bat 'powershell.exe docker tag order-service nikitamabelr/order-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-auth')
                    {
                        bat 'powershell.exe docker push nikitamabel/order-service:version1.0'          	
                    }
                }
            }
        }

        stage('Deploy Services to Kubernetes') {
            steps {
                dir('product-service') {
                    bat 'powershell.exe kubectl apply -f product-service-deployment.yaml'
                }
                dir('user-service') {
                    bat 'powershell.exe kubectl apply -f user-service-deployment.yaml'
                }
                dir('order-service') {
                    bat 'powershell.exe kubectl apply -f order-service-deployment.yaml'
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
