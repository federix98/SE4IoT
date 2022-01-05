package com.greenhouseIoT.sensorsimulator.interfaces;

import org.eclipse.paho.client.mqttv3.MqttClient;

public interface Sensor {

	public void publish(MqttClient client, String topic, String message);
	
	public void generateData();

	
	
}
