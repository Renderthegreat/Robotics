package cloud.renderlabs.ftc.Devices;

import static cloud.renderlabs.std.Constants.π;

import cloud.renderlabs.robotics.Device;
import cloud.renderlabs.robotics.Environment;
import cloud.renderlabs.robotics.Packet;
import cloud.renderlabs.std.Promise;


public class Servo extends Device {
	public static class Configuration extends Device.Configuration {

	};

	public static final Device.Factory<Servo> FACTORY = (config) -> new Servo((Servo.Configuration) config);

	public Servo(Servo.Configuration config) {
		super(config);
	};

	public Promise<Object> rotate(Environment.Radians radians, Long speed, Environment.RotaryDirection direction) {
		return new Promise<Object>(() -> {
			Packet start = new Packet();
			start.config = this.config;
			start.deviceType = "servo";
			start.data.put("type", "rotate");
			start.data.put("debug", this.config.debug);
			start.data.put("angle", radians.value * (180 / π));
			start.data.put("speed", speed);
			start.data.put("direction", direction);

			this.emit(Device.Events.OUT, start);

			// TODO: wait for completion

			return null;
		});
	};
};
