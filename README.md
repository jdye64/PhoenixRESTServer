# RealHbaseFakeStore

![RealHbaseFakeStore](/screenshots/homepage.jpg?raw=true "RealHbaseFakeStore Homepage")

## Introduction
RealHbaseFakeStore is a sample application that aims to demo some real world Hbase examples in a consumable manner around
a small fictional online store (Jeremy's Bait Shop). It aims to show how Hbase can be used in combination with Phoenix 
to provide online and robust solutions. For now the application demonstrates Hbase can be used to store receipts
as images. There are plans of course to enhance this and add a lot more examples around the complete Hbase ecosystem. This 
example is just the start. In the interest of keeping things simple this project does not strictly follow best 
practices or use the most performant practices either.

## Installation & Running
```
git clone https://github.com/jdye64/RealHbaseFakeStore.git
cd RealHbaseFakeStore
mvn clean install package
# First lets load some examples for the application. This requres the Hortonworks Sandbox already be running on your local machine.
# You can download the Hortonworks Sandbox from here. http://hortonworks.com/hdp/downloads/
java -jar ./target/RealHbaseFakeStore-1.0-SNAPSHOT.jar LoadExamples RealHbaseFakeStore.yml
# After successfully loading the examples start the server.
java -jar ./target/RealHbaseFakeStore-1.0-SNAPSHOT.jar server RealHbaseFakeStore.yml
# Once the server is running navigate to http://localhost:9000 or http://{host_server_running_on}:9000 to view the application.
```

## TODO
1.) Still need to add some actual receipt images instead of just random object images.
2.) Create a dynamic Catalog table and REST service allowing for new objects to be added to the store.
3.) Add Phoenix/Hbase security to demonstrate Hbase security principals.
4.) Add OCR capabilities to parse receipt metadata from the handwritten receipt images and place metadata into Hbase alongside receipt image.
5.) Ability to click the thumbnail receipt image and download the receipt directly to computer.