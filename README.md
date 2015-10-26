TwittMap
-----------------

This web application is for showing the current posted twitters on the Google map in near real-time, with user defined settings and category differentiated by colors.

Please see our TwiitMap Application at http://cloudyyyyy-3mamuu3rzh.elasticbeanstalk.com

##Team Member
-----------------
Ao Hong                        (ah3209)
Siyuan Zhang                   (sz2476)

##Backend
-----------------
1. Implemented with Java Web Development based on Tomcat Server.
2. Utilized MySQL database to manage Twitter data and JDBC to access from Java programs.
3. RESTful API is provided by Java Servlet.
4. Real-Time Twitter Data is grabbed to Database with Twitter API and Database size is monitored by server.
5. Twitter content is categorized into five categories: Tech, Sports, Movie, Music and Food. Each category is backed with dozens of keywords related.

##Frontend
-----------------
1. Designed and implemented based on HTML/CSS, JavaScript and Bootstrap.
2. User is allowed to set Time Range and Category from webpage.
3. Each category is marker with different color and an InfoWindow will show up with Twitter content when user click on the marker.
4. Front-end JavaScript periodically made HTTP Get request to get selected data and update the markers.

##Deployemnt
-----------------
1. Create an Ubuntu 64-bit EC2 instance.
2. Create an application on Elastic Beanstalk.
3. Upload the war file of the application and deploy it on the EC2 server with load balancing.
4. Set up the security group of the RDS instance and EC2 so that the webpage could be visited by public.

##Use The Source code
-----------------
1. Git clone source code
2. Link to AWS account with access key and secret key and connect with current development environment (All usernames and passwords should be written in Credentials.java).
3. Create application and environment on AWS Elastic Beanstalk with Tomcat 8.
4. Add Jars to build path and EC2 Tomcat lib file.
5. Create Cassandra Database on AWS EC2.
6. Create account in Twitter API and get access keys.
7. Deploy project in AWS Elastic Beanstalk.
