# Foodnet Web Application | [![Build Status](https://travis-ci.org/UNIZAR-30248-2016-FOODNET/FoodnetWebapp.svg?branch=master)](https://travis-ci.org/UNIZAR-30248-2016-FOODNET/FoodnetWebapp)

## Lessons learned & F.A.Q.
1. Squashing multiple commits into one to avoid polluting the branch:
  1. Identify the number of commits to squash using `git log`. 
  1. Afterwards do a `git rebase -i HEAD~N` where N is the number of commits to squash. 
  1. Finally do a `git push -f`

## Info and important resources
