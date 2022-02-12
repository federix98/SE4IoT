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
	private int interval = 10000; // 1 sec
	private Random rand = null;
	private Integer value = 0;
    private String name;
	private String baseTopic;
    private Boolean fanOn = false;
    private Integer minValue = 5;
    private Integer maxValue = 15;

    public Co2Sensor(String name, String baseTopic) throws MqttException{
        client = new MqttClient("tcp://localhost:1883", "pahomqtt co2sensor" + name);
		client.connect();
		this.active = true;
		this.rand = new Random();
        this.name = name;
        this.baseTopic = baseTopic;
		
        subscribe(client, this.baseTopic);

		generateData();
    }

    public String getName() {
        return name;
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while(active) {
            if ((System.currentTimeMillis() - startTime) / 1000 > 20) {
                Integer delta = 0;
                if (fanOn) {
                    delta = rand.nextInt(-2, 2);
                } else {
                    delta = rand.nextInt(-5, 5);
                }
                this.maxValue += delta;
                this.minValue += delta;
            }
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
				System.out.println("A new message arrived from the topic: \"" + msgTopic + "\". The payload of the message is " + message);
                trigger(Integer.parseInt(message));
            });

            System.out.println("Co2Sensor subscribed to topic: " + topic);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void trigger(Object data) {
        if((String) data == "ON") {
            this.fanOn = true;
            this.minValue /= 2;
            this.maxValue /= 2;
        } else {
            this.fanOn = false;
            this.minValue *= 2;
            this.maxValue += 2;
        }
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
