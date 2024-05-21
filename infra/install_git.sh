#!/bin/bash
sudo yum update -y
sudo yum install git -y
git --version
git config --global user.name “”
git config --global user.email “”