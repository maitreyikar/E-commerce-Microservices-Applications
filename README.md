# PES2UG21CS263_270_271_272_E-commerce-Microservices-Application-using-Docker-Kubernetes-Jenkins-Git
The aim of this project is to develop an e-commerce microservices application that can be deployed on the cloud using Docker, Kubernetes, Jenkins, and Git. The application will consist of several microservices that will be deployed as Docker containers on a Kubernetes cluster.

For running the services:
	1. Open 3 terminals, one in each of the "<user/product/order>-service" directories.
	2. ./mvnw spring-boot:run (or just mvnw or mvn)
	3. product-service will be available on localhost:8080/
	4. user-service will be available on localhost:8081/user/login
	5. order-service will be available on localhost:8082 (no frontend, no point in opening in browser)

