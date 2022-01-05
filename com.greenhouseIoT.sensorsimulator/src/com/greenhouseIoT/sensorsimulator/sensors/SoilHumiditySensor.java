package com.greenhouseIoT.sensorsimulator.sensors;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.greenhouseIoT.sensorsimulator.interfaces.Actuator;
import com.greenhouseIoT.sensorsimulator.interfaces.Sensor;

public class SoilHumiditySensor implements Sensor, Actuator, Runnable {
	
	MqttClient client = null;
	private Boolean active = false;
	private int interval = 1000; // 1 sec
	private Random rand = null;
	private int value = 0;
	
	public SoilHumiditySensor() throws MqttException {
		client = new MqttClient("tcp://localhost:1883", "pahomqtt soilhumiditysensor");
		client.connect();
		this.active = true;
		this.rand = new Random();
		
		generateData();
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
		// TODO Auto-generated method stub
		
		// https://www.drought.gov/topics/soil-moisture
	    int moisture = rand.nextInt(100);
	    
	    this.value = moisture;
	}

	@Override
	public void publish(MqttClient client, String topic, String message) {
		// TODO Auto-generated method stub
		try {
			MqttMessage data = new MqttMessage();
			data.setPayload(message.getBytes());
			client.publish("home/" + topic, data);
			System.out.println("Published \"" + data + "\" to topic " + topic);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(active) {
			publish(this.client, "greenhouse/soilhumidity", Integer.toString(this.value));
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		this.active = false;
	}

}
