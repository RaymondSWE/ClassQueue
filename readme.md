## getting started
you need to have the following installed in order to run this project
- java 17 or later
- python3.12 or later
- a mavon environment
###server
in order to run the server open a terminal  and navigate to  the server directory which can be found at the root of the project.
before  you can run the server run the following command to install all the dependencies:
`mvn install`
after the dependencies have been installed successfully use the following command to run the server:
`mvn spring-boot:run`
###client
the client directory is located at the root of the project. Open a terminal and navigate to `python-client/src` and run the following command to install the required librarys:
`pip install pyzmq ttkthemes tk`
the student client can be run using the following command:
``