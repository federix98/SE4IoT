package com.greenhouseIoT.sensorsimulator.interfaces;

import org.eclipse.paho.client.mqttv3.MqttClient;

public interface Actuator {
	
	public void subscribe(MqttClient client, String topic);
	
	public void trigger(Object data);

}
