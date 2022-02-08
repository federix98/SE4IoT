package com.greenhouseIoT.sensorsimulator.sensors;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.greenhouseIoT.sensorsimulator.interfaces.Actuator;
import com.greenhouseIoT.sensorsimulator.interfaces.Sensor;

public class Co2Sensor implements Sensor, Actuator, Runnable{

    MqttClient client = null;
	private Boolean active = false;
	private int interval = 1000; // 1 sec
	private Random rand = null;
	private Integer value = 0;
    private String name;
	private String baseTopic;
    private Integer fanSpeed = 0;
    private Integer maxValue = 100;

    public Co2Sensor(String name, String baseTopic) throws MqttException{
        client = new MqttClient("tcp://localhost:1883", "pahomqtt co2sensor" + name);
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
    public void run() {
        while(active) {
            generateData();
			publish(this.client, this.baseTopic, Integer.toString(this.value));
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        
    }

    @Override
    public void subscribe(MqttClient client, String topic) {
        try {
            client.subscribe("/home/" + topic + "/set", (msgTopic, msg) -> {
                String message = new String(msg.getPayload());
                // handle message
                Integer value = 0; // TODO: this value will be retrieved from message
                trigger(value);
            });
            System.out.println("Co2Sensor subscribed to topic: " + topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void trigger(Object data) {
        this.fanSpeed = (Integer)data;
        this.maxValue = 100 - fanSpeed;
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
    public void generateData() {   
        this.value = rand.nextInt(maxValue);
    }

    public void stop() {
        this.active = false;
    }
    
}
