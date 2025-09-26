package cloud.renderlabs.ftc.Devices;

import cloud.renderlabs.robotics.Device;
import cloud.renderlabs.robotics.Environment;
import cloud.renderlabs.robotics.Packet;
import cloud.renderlabs.std.Promise;
import java.util.Optional;

public class DcMotor extends Device {
	public static class Configuration extends Device.Configuration {
		
	};

	public static final Device.Factory<DcMotor> FACTORY = (config) -> new DcMotor((DcMotor.Configuration) config);

	public DcMotor(DcMotor.Configuration config) {
		super(config);
	};

	public Promise<Object> rotate(Double power, Environment.RotaryDirection direction, Optional<Long> duration) {
		return new Promise<Object>(() -> {
			Packet packet = new Packet();
			packet.config.location.port = this.config.location.port;
			packet.deviceType = "motor";
			packet.data.put("type", "rotate");
			packet.data.put("debug", this.config.debug);
			packet.data.put("power", power);

			this.emit(Device.Events.OUT, packet);

			if (duration.isPresent()) {
				// Wait for duration
				try {
					Thread.sleep(duration.get());
				} catch (InterruptedException e) {
					// This should never happen
					e.printStackTrace();
				};

				this.stop();
			};

			return null;
		});
	};

	public Promise<Object> rotate(Double power, Environment.RotaryDirection direction) {
		return this.rotate(power, direction, Optional.empty());
	};

	public Promise<Object> stop() {
		return new Promise<Object>(() -> {
			Packet packet = new Packet();
			packet.config = this.config;
			packet.deviceType = "motor";
			packet.data.put("type", "rotate");
			packet.data.put("power", 0);

			this.emit(Device.Events.OUT, packet);

			return null;
		});
	};
};