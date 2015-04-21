##############################################################
# Author:   Asad Shahabuddin                                 #
# File:     rasp_util.py                                     #
# Details:  Setup environment to run RaspMR on Ubuntu Linux. #
# Email ID: asad808@ccs.neu.edu                              #
##############################7###############################

'''
PROCESS:
(0)  SSH command - ssh -i raspmr-keypair-1.pem ubuntu@54.148.234.248
(1)  sudo apt-get update
(2)  sudo apt-get install openjdk-7-jdk 
(3)  Java installation directory - /usr/lib/jvm/java-7-openjdk-amd64
(4)  Edit ~/.bashrc and add the following lines:
# Java environment variables
export PATH
export JAVA_HOME="/usr/lib/jvm/java-7-openjdk-amd64"

# Update the PATH environment variable
PATH="$PATH:$JAVA_HOME/bin"
(5)  Reboot
(6)  sudo apt-get install maven
(7)  sudo apt-get install git
(8)  mkdir RaspMR
(9)  cd RaspMR
(10) git init
(11) git remote add origin https://github.com/asadshahabuddin/RaspMR.git
(12) git pull origin master
(13) mvn clean compile package
'''

# Import list
from os import chdir
from subprocess import call

# Setup RaspMR and create executable JAR files
def setup_raspmr():
    print('')
    print('==============')
    print('RaspMR - Setup')
    print('==============')

    # Setup
    print(' [cmd] mkdir RaspMR')
    call('mkdir RaspMR', shell=True)
    print(' [cmd] cd RaspMR')
    chdir('RaspMR')
    print('[echo] Initializing Git')
    call('git init', shell=True)
    print('')
    print('[echo] Cloning remote repository')
    call('git remote add origin https://github.com/asadshahabuddin/RaspMR.git', shell=True)
    call('git pull origin master', shell=True)
    print('')
    print('[echo] Compiling and packaging the project')
    print('')
    call('mvn clean compile package', shell=True)

# Main
setup_raspmr()

# EOF