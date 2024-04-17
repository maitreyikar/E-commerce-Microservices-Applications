pipeline {
    agent any
    stages {
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
                    bat 'powershell.exe docker tag mysql pes2ug21cs270maitreyikar/mysql:1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-auth')
                    {
                        bat 'powershell.exe docker push pes2ug21cs270maitreyikar/mysql:1.0'          	
                    }
                    bat 'powershell.exe docker tag product-service pes2ug21cs270maitreyikar/product-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-auth')
                    {
                        bat 'powershell.exe docker push pes2ug21cs270maitreyikar/product-service:version1.0'          	
                    }
                    bat 'powershell.exe docker tag user-service pes2ug21cs270maitreyikar/user-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-auth')
                    {
                        bat 'powershell.exe docker push pes2ug21cs270maitreyikar/user-service:version1.0'          	
                    }
                    bat 'powershell.exe docker tag order-service pes2ug21cs270maitreyikar/order-service:version1.0'
                    docker.withRegistry('https://registry.hub.docker.com','docker-registry-auth')
                    {
                        bat 'powershell.exe docker push pes2ug21cs270maitreyikar/order-service:version1.0'          	
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script{
                    bat 'powershell.exe kubectl apply -f mysql-secret.yaml'
                    bat 'powershell.exe kubectl apply -f mysql-storage.yaml'
                    bat 'powershell.exe kubectl apply -f mysql-deployment.yaml'
                }
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