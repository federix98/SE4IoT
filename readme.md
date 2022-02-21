# Smart Greenhouse IoT System
> Project developed for the Software Engineering for Internet of Things course - University of L'Aquila

This repository contains the implementation of ***Smart Greenhouse IoT System**: Project developed for the Software Engineering for Internet of Things course - University of L'Aquila

## Installation Requirements
1. Install the latest version of Docker - https://docs.docker.com/get-docker/
2. Install the latest version of Docker Compose (if not already installed) - https://docs.docker.com/compose/install/

## Project Description

This project consists in a set of containers running in Docker and automatically configured using the file *docker-compose.yml*. In the following the description of them:

 - **Environment**
	 - *web*: The environment has been simulated using node js. The exposed API are written in HTTP. The implementation is stored in the */environment/* subfolder.
 - **Sensors and Actuators**
	 - *node-red*: sensors and actuators have been simulated using node red flows. The local volume is */node-red-data/*.
 - **Historical Values**:
     - *telegraf*: This is used to read the data from all the topics and store it on influx db automatically.
	 - *influxdb*: In order to maintain the knowledge of the system, influxdb has been used to store all the data of the system.
     - *grafana*: Grafana is used to display and analyze all the real time and historical data of the system in a dashboard. The system contains some alert rules to notify the users through telegram if any unwanted event occurs.
 - **MQTT Broker**
	 - *mosquitto*: The <a href="https://mosquitto.org">Mosquitto</a> MQTT Broker which manages all the MQTT messages exchanged in the system.
 - **Greenhouse Management**   
     - *openhab*: Openhab has been used to display real time data and to send messages to the actuators in order to modify the parameters of the greenhouse.

*More informations on the documentation in this repo (**Documentation.pdf**)*.

## Configuration
The configuration of the system is mainly contained in the *docker-compose.yml* file. Be sure that all the exposed mapped ports are free on your environment:
- **8081** for the Planner
- **1883** and **9001** for Mosquitto MQTT Broker
- **8086** for InfluxDB
- **3000** for the Grafana dashboard
- **1880** for the Node-Red
- **9876** for the OpenHab
- **8000** for the Environment Simulator

If you are not able to free all the listed ports, you can change the mapping in the compose file.

In addition, in this repo there are some files for configuration purposes which docker will map in the containers as volumes:
- *mosquitto.conf* file is the configuration file for the Mosquitto Broker container
- *telegraf.conf* file is the configuration file for Telegraf

## Running 
First of all you have to open your terminal on this folder and build all the images running the following command:

    docker-compose build
Then, you can run all the containers with the following command:

    docker-compose up -d
If you want to stop all, you can use this:

    docker-compose down

## Authors
This project has been realized by:
- Pietro Ciammaricone
- Gianluca Rea
- Federico Di Menna