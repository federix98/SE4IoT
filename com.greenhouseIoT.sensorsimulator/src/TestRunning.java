import org.eclipse.paho.client.mqttv3.MqttException;

import com.greenhouseIoT.sensorsimulator.sensors.SoilHumiditySensor;

public class TestRunning {

	public static void main(String[] args) throws MqttException, InterruptedException {
		// TODO Auto-generated method stub
		SoilHumiditySensor sensor = new SoilHumiditySensor();
		
		Thread t = new Thread(sensor);
		t.start();
		
		Thread.sleep(10000);
		
		sensor.stop();
	}

}
