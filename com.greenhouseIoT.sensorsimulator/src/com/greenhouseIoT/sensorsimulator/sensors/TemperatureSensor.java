package com.greenhouseIoT.sensorsimulator.sensors;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.greenhouseIoT.sensorsimulator.interfaces.Actuator;
import com.greenhouseIoT.sensorsimulator.interfaces.Sensor;

public class TemperatureSensor implements Sensor, Actuator, Runnable {
	
	MqttClient client = null;
	private Boolean active = false;
	private int interval = 4000; // 1 sec
	private Random rand = null;
	private float value = 0;
	private String name;
	private String baseTopic;
	
	public TemperatureSensor(String name, String baseTopic) throws MqttException {
		client = new MqttClient("tcp://localhost:1883", "pahomqtt temperaturesensor" + name);
		client.connect();
		this.active = true;
		this.rand = new Random();
		this.name = name;
		this.baseTopic = baseTopic;
		
		generateData();
	}

	public String getName() {
		return name;
	}

	@Override
	public void subscribe(MqttClient client, String topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trigger(Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateData() {
		// https://www.drought.gov/topics/soil-moisture
	    
	    this.value = rand.nextFloat(40.0f);;
	}

	@Override
	public void publish(MqttClient client, String topic, String message) {
		try {
			MqttMessage data = new MqttMessage();
			data.setPayload(message.getBytes());
			client.publish("home/" + topic, data);
			System.out.println("Published \"" + data + "\" to topic " + topic);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(active) {
			generateData();
			publish(this.client, this.baseTopic, Float.toString(this.value));
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		this.active = false;
	}

}
