package com.greenhouseIoT.sensorsimulator.sensors;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.greenhouseIoT.sensorsimulator.interfaces.Actuator;
import com.greenhouseIoT.sensorsimulator.interfaces.Sensor;

public class AirHumiditySensor implements Sensor, Actuator, Runnable, MqttCallback {
	
	MqttClient client = null;
	private Boolean active = false;
	private int interval = 4000; // 1 sec
	private Random rand = null;
	private Double value = 0.0;
	private String basetopic;
	private String name;
	
	public AirHumiditySensor(String name, String baseTopic) throws MqttException {
		this.name = name;
		client = new MqttClient("tcp://localhost:1883", "pahomqtt airhumiditysensor" + name);
		client.connect();
		//this.basetopic = "home/greenhouse/airhumidity";
		this.basetopic = baseTopic;
		this.active = true;
		this.rand = new Random();
		client.subscribe(this.basetopic + "/set");
		client.setCallback(this);
		
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
	    Double moisture = rand.nextDouble(100);
	    
	    this.value = moisture;
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
			publish(this.client, this.basetopic, Double.toString(this.value));
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

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String arg0, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("A new message arrived from the topic: \"" + arg0 + "\". The payload of the message is " + message.toString());
	}

}
