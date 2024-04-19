# PES2UG21CS263_270_271_272_E-commerce-Microservices-Application-using-Docker-Kubernetes-Jenkins-Git
The aim of this project is to develop an e-commerce microservices application that can be deployed on the cloud using Docker, Kubernetes, Jenkins, and Git. The application will consist of several microservices that will be deployed as Docker containers on a Kubernetes cluster.


## Jenkin

### Downloading  and Setting Up Jenkins

	1. Go to https://www.jenkins.io/download/
	2. Download the Generic Java Package (.war) for Jenkins 2.440.2 LTS
	3. Open command prompt where the .war file is
	4. java -jar jenkins.war (or java -jar jenkins.war --enable-future-java if there is a version problem)
	5. Jenkins will be available at http://localhost:8080
	6. When running for the first time, password will be displayed in the terminal, store it. Use it to login to Jenkins at localhost:8080
	7. Skip setting up of another user (stick to admin account)
	8. Under plugins, search for GitHub and Docker Pipeline and select both for installation
	9. On subsequent runs, the login credentials will be username: admin, password: the one from terminal

### Setting up GitHub Personal Access Token

	1. Click on your profile pic, in the menu that pops up, choose Settings
	2. In the menu on the left of the Settings, choose the last option: Developer Settings
	3. Select Personal Access Token (choose Tokens (Classic) in the drop down)
	4. Select Generate New Token (classic)
	5. Enter your password if prompted
	6. Under Note, write a brief description of the token for your own reference
	7. Under Select Scopes, check "repo"
	8. Scroll to the bottom, select Generate token
	9. Copy the token and store it
	
### Adding Token to Jenkins:

	1. In the menu on the left side of the dashboard, choose Manage Jenkins
	2. Under Security, select Credentials
	3. Under Stores scoped to Jenkins, click on (global)
	4. Choose Add Credentials
	5. For Username and ID, enter Github-Token
	6. For Password, enter token value copied earlier
	7. Create

### Adding Docker Hub Credentials to Jenkins:
	1. Do the same as above, exept username should be your DockerHub username, password should be DockerHub password, and ID should be docker-registry-auth

### Creating a Pipeline:
	1. On the menu on the right in the Dashboard page, select New Item
	2. Enter a name and choose Pipeline
	3. Under the Pipeline section, for the definition field, choose Pipeline Script from SCM
	4. Choose Git under SCM
	5. Paste the repository URL in the corresponding field
	6. Under Credentials, choose Github-Token
	7. Under Branches to build, select Add Branch
	8. The newly added branch name should be changed from */master to */main (do not remove the already extisting branch */master)
	9. Save

### Deploying:
	1. Open Docker Desktop
	2. In a terminal, minikube start, wait till it successfully starts
	3. Go to Jenkins dashboard, select the pipeline created in above section
	4. In the menu that appears on the left side of the window, choose Build Now
	5. Once the stage Deploy to Kubernetes is successfully completed, go back to terminal
	6. minikube service product-service user-service --url
	7. Two URLs (with port) will be displayed, for product-service and user-service respectively
	8. Open product-service at the path /products/
	9. Open user-service at the path /user/login


## Using Kubernetes:

### First run:

	NOTE : Tag all 4 images and push them to docker hub first.
		docker tag <image-id> <docker-username>/<imagename>:<tag>
		docker push <docker-username>/<imagename>:<tag>

		Also, change the value of the "image" field in the respective deployment.yaml files of product-service, user-service and order-service

	1. minikube start (if an error is encountered, do minikube delete followed by minikube start)

  	MySQL:
		2. Make sure you are in the root directory (where READ.md is)
		3. kubectl apply -f .\mysql-secret.yaml
		4. kubectl apply -f .\mysql-storage.yaml
		5. kubectl apply -f .\mysql-deployment.yaml
	 	6. kubectl get pods
	  	7. If the mysql pod status is running, proceed.

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





