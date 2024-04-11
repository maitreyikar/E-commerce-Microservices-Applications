## PES2UG21CS263_270_271_272_E-commerce-Microservices-Application-using-Docker-Kubernetes-Jenkins-Git
The aim of this project is to develop an e-commerce microservices application that can be deployed on the cloud using Docker, Kubernetes, Jenkins, and Git. The application will consist of several microservices that will be deployed as Docker containers on a Kubernetes cluster.


### For deploying and running in Docker containers for the very first time:
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


### For subsequent runs in Docker (applicable to MySQL as well as all three microservices):
	Note: Always start the MySQL container first.
	1. docker container ls -a
	2. Note down the container ID of the container you want to run
	3. docker container start <containerID_from_previous_step>
	4. To stop the container: docker container stop <containerID>


### Accessing the services: 
	1. product-service will be available on localhost:8080/products/ for product-related CRUD operations
	2. user-service will be available on localhost:8081/user/signup for user-related features

