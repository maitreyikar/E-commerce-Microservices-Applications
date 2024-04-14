# PES2UG21CS263_270_271_272_E-commerce-Microservices-Application-using-Docker-Kubernetes-Jenkins-Git
The aim of this project is to develop an e-commerce microservices application that can be deployed on the cloud using Docker, Kubernetes, Jenkins, and Git. The application will consist of several microservices that will be deployed as Docker containers on a Kubernetes cluster.



## Using Kubernetes:

### First run:

	NOTE :Tag all 4 images and push them to docker hub first.
	1. minikube start (if an error is encountered, do minikube delete followed by minikube start)

  	MySQL:
		2. Make sure you are in the root directory (where READ.md is)
		3. kubectl apply -f .\mysql-secret.yaml
		4. kubectl apply -f .\mysql-storage.yaml
		5. kubectl apply -f .\mysql-deployment.yaml
	 	6. kubectl get pods
	  	7. If the mysql pod status is running, do kubectl exec --stdin --tty mysql-859b4c878b-hn58h -- /bin/bash
	   	8. In the bash prompt, mysql -p
	    	9. In the password prompt, root
	     	10. In the mysql prompt, create database mymart;
		11. In mysql prompt, exit;
	 	12. exit (to exit from bash)

   	Services (strictly in the given order, can use these commands for subsequent runs too):
		13. kubectl apply -f .\product-service\product-service-deployment.yaml
		14. kubectl apply -f .\user-service\user-service-deployment.yaml
		15. kubectl apply -f .\order-service\order-service-deployment.yaml

### Accessing the services:
	16. Check pod status using kubectl get pods, wait till they are all running.
	17. minikube service product-service (will open browser and connect to the service on default path "/", change path to access the right webpages)
	18. Repeat step 17 for user-service and order-service (you need to open a new terminal for each to execute the command in step 17)

### Stopping the services:
	1. kubectl delete service product-service user-service order-service
 	2. kubectl delete deployment product-service user-service order-service
  	3. kubectl scale --replicas=0 deployment/mysql
  	NOTE: DO NOT DELETE THE MYSQL SERVICE OR DEPLOYMENT, THAT WILL ERASE ALL DATABASES AND TABLES CREATED.
 
### Subsequent runs:
	1. kubectl scale --replicas=1 deployment/mysql
 	2. For the services, use the same commands under "First run: Services"
 



## Using Docker:

### For deploying in Docker containers for the first time:
	1. MySQL:
		1. docker pull mysql
		2. docker run -p 3307:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=mymart mysql:latest
		3. In a separate terminal, "docker network create mymart-mysql-net"
		4. "docker network connect mymart-mysql-net mysqldb" (can close the terminal now)

	2. Microservices:
		NOTE: Do it in the given order only, that is, for product service, then user service, then order service.
		1. In product-service/,  execute "mvn clean package"
		2. While staying in the same directory, execute "docker build -t product-service ."
		3. docker run -p 8080:8080 --name product-service --network=mymart-mysql-net -e MYSQL_HOST=mysqldb -e MYSQL_PASSWORD=root product-service"
		4. Repeat for 
			a.user-service (port numbers should be 8081:8081)
			b.order-service (port numbers should be 8082:8082)
		Execute the commands in the corresponding directories and replace "product" with "user" or "order" wherever required.

### Accessing the services: 
	1. product-service will be available on localhost:8080/products/ for product-related CRUD operations
	2. user-service will be available on localhost:8081/user/signup for user-related features

### Stopping the containers: 
 	docker container stop <containerID>

### For subsequent runs in Docker (applicable to MySQL as well as all three microservices):
	Note: Always start the MySQL container first.
	1. docker container ls -a
	2. Note down the container ID of the container you want to run
	3. docker container start <containerID_from_previous_step>





