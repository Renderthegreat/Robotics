package cloud.renderlabs.robotlib.Devices;

import cloud.renderlabs.robotlib.Device;

public class Motor extends Device {
	public static class Configuration extends Device.Configuration {

	};

	public Motor.Configuration config;

	public Motor(Motor.Configuration configuration) {
		super(configuration);
		this.config = configuration;
	};
};