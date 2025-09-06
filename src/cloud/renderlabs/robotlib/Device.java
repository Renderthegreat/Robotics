package cloud.renderlabs.robotlib;

import cloud.renderlabs.std.EventEmitter;

public class Device extends EventEmitter<Device.Events, Packet> {
	public static class Configuration {
		public Integer id;
		public String label;
	};

	public static class Status {
		public boolean ready;
	};

	public enum Events {
		OPEN,
		CLOSE,
		IN,
		OUT,
	};

	public Device.Configuration config;
	public Device.Status status;

	public Device(Device.Configuration config) {
		this.config = config;
		this.on(Device.Events.OPEN, (Packet data) -> {
			System.out.printf("Device #%s is ready!", this.config.id.toString());
		});
	};
};

