package cloud.renderlabs.ftc.Devices;

import java.util.Optional;

import cloud.renderlabs.std.Promise;

import cloud.renderlabs.robotics.Device;
import cloud.renderlabs.robotics.Environment;
import cloud.renderlabs.robotics.Packet;

public class Motor extends Device {
	public static class Configuration extends Device.Configuration {
		
	};

	public static final Device.Factory<Motor> FACTORY = (config) -> new Motor((Motor.Configuration) config);

	public Motor(Motor.Configuration config) {
		super(config);
	};

	public Promise<Object> rotate(Double speed, Environment.RotaryDirection direction, Optional<Long> duration) {
		return new Promise<Object>(() -> {
			Packet start = new Packet();
			start.data.put("device", "motor");
			start.data.put("id", this.config.id);
			start.data.put("speed", speed);

			if (this.config.debug) System.out.printf("Motor #%s is starting to rotate at %s rpm\n", this.config.id, speed);
			this.emit(Device.Events.IN, start);

			if (duration.isPresent()) {
				Packet stop = start;
				stop.data.put("speed", 0);

				// Wait for duration
				try {
					Thread.sleep(duration.get());
				} catch (InterruptedException e) {
					// This should never happen
					e.printStackTrace();
				};

				if (this.config.debug) System.out.printf("Motor #%s is stopping...\n", this.config.id);

				this.emit(Device.Events.IN, stop);
			};

			return null;
		});
	};

	public Promise<Object> rotate(Double speed, Environment.RotaryDirection direction) {
		return this.rotate(speed, direction, Optional.empty());
	};

	public Promise<Object> stop() {
		return new Promise<Object>(() -> {
			Packet stop = new Packet();
			stop.data.put("device", "motor");
			stop.data.put("id", this.config.id);
			stop.data.put("speed", 0);

			if (this.config.debug) System.out.printf("Motor #%s is stopping...\n", this.config.id);

			this.emit(Device.Events.IN, stop);

			return null;
		});
	};
};