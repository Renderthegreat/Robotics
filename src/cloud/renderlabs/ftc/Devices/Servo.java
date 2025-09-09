package cloud.renderlabs.ftc.Devices;

import cloud.renderlabs.std.Promise;
import static cloud.renderlabs.std.Constants.π;

import cloud.renderlabs.robotics.Device;
import cloud.renderlabs.robotics.Environment;
import cloud.renderlabs.robotics.Packet;


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
			start.data.put("device", "servo");
			start.data.put("id", this.config.id);
			start.data.put("angle", radians.value * (180 / π));
			start.data.put("speed", speed);
			start.data.put("direction", direction);

			if (this.config.debug) System.out.printf("Servo #%s is rotating to %s radians at %s rpm\n", this.config.id, radians.value, speed);
			this.emit(Device.Events.IN, start);

			// TODO: wait for completion

			return null;
		});
	};
};
