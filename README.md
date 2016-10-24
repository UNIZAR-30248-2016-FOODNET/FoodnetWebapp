# Foodnet Web Application | [![Build Status](https://travis-ci.org/UNIZAR-30248-2016-FOODNET/FoodnetWebapp.svg?branch=master)](https://travis-ci.org/UNIZAR-30248-2016-FOODNET/FoodnetWebapp)

## Lessons learned & F.A.Q.
1. Squashing multiple commits into one to avoid polluting the branch:
  1. Identify the number of commits to squash using `git log`. 
  1. Afterwards do a `git rebase -i HEAD~N` where N is the number of commits to squash. 
  1. Finally do a `git push -f`

2. Solve error with MongoDB in MacOS:
  1. If you have the issue: `Unable to create/open lock file: /data/db/mongod.lock errno:13`
  2. Create the folder to store data: `sudo mkdir -p /data/db`
  3. Configure the folder in MongoDB: `mongod --dbpath /data/db`
  4. Give permissons to access: `sudo chown -R ``id -u`` /data/db`
  
## Info and important resources
1. Installing mongodb: [Ubuntu](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/), [OS X](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/), [Windows](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/)
2. Accesing data with mongodb: [Link](https://spring.io/guides/gs/accessing-data-mongodb/)
3. Project example running with Thymeleaf: [Link](http://krams915.blogspot.com.es/2012/12/spring-and-thymeleaf-with-javaconfig_2.html)