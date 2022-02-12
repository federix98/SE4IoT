package com.greenhouseIoT.sensorsimulator.sensors;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.greenhouseIoT.sensorsimulator.interfaces.Actuator;
import com.greenhouseIoT.sensorsimulator.interfaces.Sensor;

public class AirHumiditySensor implements Sensor, Actuator, Runnable {
	
	MqttClient client = null;
	private Boolean active = false;
	private int interval = 10000; // 1 sec
	private Random rand = null;
	private Double value = 0.0;
	private String basetopic;
	private String name;
	private Integer dehumidifierSpeed = 0;
	private Float maxValue = 100f;
	
	public AirHumiditySensor(String name, String baseTopic) throws MqttException {
		this.name = name;
		client = new MqttClient("tcp://localhost:1883", "pahomqtt airhumiditysensor" + name);
		client.connect();
		this.basetopic = baseTopic;
		this.active = true;
		this.rand = new Random();
		subscribe(client, this.basetopic);
		
		generateData();
	}

	public String getName() {
		return name;
	}

	@Override
	public void subscribe(MqttClient client, String topic) {
		try {
            client.subscribe("/home/" + topic + "/set", (msgTopic, msg) -> {
				String message = new String(msg.getPayload());
				System.out.println("A new message arrived from the topic: \"" + msgTopic + "\". The payload of the message is " + message);
                trigger(Integer.parseInt(message));
            });

            System.out.println("Air humidity sensor subscribed to topic: " + topic);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
		
	}

	@Override
	public void trigger(Object data) {
		this.dehumidifierSpeed = (Integer)data;
        this.maxValue = 100f - dehumidifierSpeed / 5;	
	}

	@Override
	public void generateData() {
		// https://www.drought.gov/topics/soil-moisture
	    Double moisture = rand.nextDouble(maxValue);
	    
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

}
