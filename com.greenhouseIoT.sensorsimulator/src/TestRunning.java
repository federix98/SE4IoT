import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.greenhouseIoT.sensorsimulator.sensors.AirHumiditySensor;
import com.greenhouseIoT.sensorsimulator.sensors.SoilHumiditySensor;
import com.greenhouseIoT.sensorsimulator.sensors.SoilPHSensor;
import com.greenhouseIoT.sensorsimulator.sensors.TemperatureSensor;
import com.greenhouseIoT.sensorsimulator.sensors.LightSensor;
import com.greenhouseIoT.sensorsimulator.sensors.Co2Sensor;

public class TestRunning {

	public static void main(String[] args) throws MqttException, InterruptedException {
		
		createCultivation("tomatocultivation");
		createCultivation("potatocultivation");
		createCultivation("fruitcultivation");
		// TODO Auto-generated method stub
		/*
		SoilHumiditySensor soilHum = new SoilHumiditySensor();
		AirHumiditySensor airHum = new AirHumiditySensor();
		SoilPHSensor soilPH = new SoilPHSensor();
		TemperatureSensor tempCels = new TemperatureSensor();
		LightSensor lightBright = new LightSensor();
		
		new Thread(soilHum).start();
		new Thread(airHum).start();
		new Thread(soilPH).start();
		new Thread(tempCels).start();
		new Thread(lightBright).start();
		
		Thread.sleep(1000000);
		
		
		soilHum.stop();
		airHum.stop();
		soilPH.stop();
		tempCels.stop();
		lightBright.stop();
		*/
	}
	
	private static void createCultivation(String type) throws MqttException {
		SoilHumiditySensor soilHum = new SoilHumiditySensor(type + "_SoilHumiditySensor", "greenhouse/" + type + "/soilhumidity");
		AirHumiditySensor airHum = new AirHumiditySensor(type + "_AirHumiditySensor", "greenhouse/" + type + "/airhumidity");
		SoilPHSensor soilPH = new SoilPHSensor(type + "_SoilPHSensor", "greenhouse/" + type + "/soilph");
		TemperatureSensor tempCels = new TemperatureSensor(type + "_SoilTemperatureSensor", "greenhouse/" + type + "/temperature");
		LightSensor lightBright = new LightSensor(type + "_LightSensor", "greenhouse/" + type + "/light");
		Co2Sensor co2Sensor = new Co2Sensor(type + "_Co2Sensor","greenhouse/"+ type + "/co2");
 		
		new Thread(soilHum).start();
		new Thread(airHum).start();
		new Thread(soilPH).start();
		new Thread(tempCels).start();
		new Thread(lightBright).start();
		new Thread(co2Sensor).start();
	}

}
