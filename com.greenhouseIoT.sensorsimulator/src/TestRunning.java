import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.greenhouseIoT.sensorsimulator.sensors.AirHumiditySensor;
import com.greenhouseIoT.sensorsimulator.sensors.SoilHumiditySensor;
import com.greenhouseIoT.sensorsimulator.sensors.SoilPHSensor;
import com.greenhouseIoT.sensorsimulator.sensors.TemperatureSensor;
import com.greenhouseIoT.sensorsimulator.sensors.LightSensor;

public class TestRunning {

	public static void main(String[] args) throws MqttException, InterruptedException {
		// TODO Auto-generated method stub
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
		
		Thread.sleep(10000);
		
		soilHum.stop();
		airHum.stop();
		soilPH.stop();
		tempCels.stop();
		lightBright.stop();
	}

}
