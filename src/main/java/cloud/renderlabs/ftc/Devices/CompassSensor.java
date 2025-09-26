package cloud.renderlabs.ftc.Devices;

import cloud.renderlabs.robotics.Device;
import cloud.renderlabs.robotics.Environment;
import cloud.renderlabs.robotics.Packet;
import cloud.renderlabs.std.Promise;

public class CompassSensor extends Device {
	public static class Configuration extends Device.Configuration {

	};

	public static final Device.Factory<CompassSensor> FACTORY = (config) -> new CompassSensor((CompassSensor.Configuration) config);

	public CompassSensor(CompassSensor.Configuration config) {
		super(config);

		this.on(CompassSensor.Events.IN, (Packet packet) -> {
			
		});
	};
};
