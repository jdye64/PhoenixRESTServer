# PhoenixRESTServer

## Introduction
PhoenixRESTServer was introduced to serve as a stop gap for some features not currently supported by the Phoenix Query Server.
At this time non Java clients cannot directly interact with the Phoenix Query Server. REST functionality is available in 
the Phoenix Query Server but functionality is limited (Array type will not work for example). In order to ease the
adoption pains for non Java shops PhoenixRESTServer was created to fill these gaps. 

## Architecture
PhoenixRESTServer is a Java application written with the lightweight microframework [Dropwizard](http://dropwizard.io).
This application accepts REST requests from any client and translates those REST requests into the correlating Phoenix JDBC
command. With this approach PhoenixRESTServer is capable of performing any of the supported Phoenix JDBC features from non Java languages.
Since the applications are lightweight they can be colocated on your HBase Region Servers. Colocating them on the Region Servers
also saves bandwidth since this eliminates the network traffic that would otherwise be required to transfer the larger 
datasets to the client where client side operations would occur. PhoenixRESTServer could also be deployed behind a HTTP/TCP
load balancer to balance requests traffic. Here is a high level (and ugly) deployment architecture. 
![PhoenixRESTServer Deployment Architecture](https://raw.githubusercontent.com/jdye64/PhoenixRESTServer/master/screenshots/PhoenixRESTServer_DeploymentArchitecture.jpg "PhoenixRESTServer")

## Configuration
PhoenixRESTServer configuration is done via a single YAML file. All Dropwizard configuration values are valid as well. A sample 
is present at PhoenixRESTServer.yml for reference. TODO: add more information around configuration and clean up.

## Running
```
git clone https://github.com/jdye64/PhoenixRESTServer.git
cd ./PhoenixRESTServer
mvn clean install package && java -jar ./target/PhoenixRESTServer-1.0-SNAPSHOT.jar server PhoenixRESTServer.yml
```