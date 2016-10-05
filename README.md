# Foodnet Web Application | [![Build Status](https://travis-ci.org/UNIZAR-30248-2016-FOODNET/FoodnetWebapp.svg?branch=master)](https://travis-ci.org/UNIZAR-30248-2016-FOODNET/FoodnetWebapp)

## Lessons learned & F.A.Q.
1. Squashing multiple commits into one to avoid polluting the branch:
  1. Identify the number of commits to squash using `git log`. 
  1. Afterwards do a `git rebase -i HEAD~N` where N is the number of commits to squash. 
  1. Finally do a `git push -f`

## Info and important resources
1. Installing mongodb: [Ubuntu](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/), [OS X](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/), [Windows](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/)
2. Accesing data with mongodb: [Link](https://spring.io/guides/gs/accessing-data-mongodb/)
3. Project example running with Thymeleaf: [Link](http://krams915.blogspot.com.es/2012/12/spring-and-thymeleaf-with-javaconfig_2.html)