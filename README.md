TwittMap 
-----------------

This web application is for showing the current posted twitters on the Google map in near real-time, with user defined settings and category differentiated by colors

##Backend
-----------------
1. Implemented with Java Web Development based on Tomcat Server.
2. Utilized MySQL database to manage Twitter data and JDBC to access from Java programs.
3. RESTful API is provided by Java Servlet.

##Frontend
-----------------
Designed and implemented based on HTML/CSS, JavaScript and Bootstrap

##Deployemnt
-----------------
1. Create an Ubuntu 64-bit EC2 instance
2. Create an application on Elastic Beanstalk
3. Upload the war file of the application and deploy it on the EC2 server with load balancing
4. Set up the security group of the RDS instance and EC2 so that the webpage could be visited by public

