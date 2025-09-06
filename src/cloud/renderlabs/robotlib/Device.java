package cloud.renderlabs.robotlib;

import cloud.renderlabs.std.EventEmitter;

public abstract class Device extends EventEmitter<Device.Events, Packet> {
	public static abstract class Configuration {
		public Integer id;
		public String label;
		public Boolean debug = false;
	};

	public static interface Factory<T extends Device> {
		public T create(Device.Configuration config);	
	};

	public static class Status {
		public Boolean ready = false;
	};

	public static enum Events {
		OPEN,
		CLOSE,
		IN,
		OUT,
	};

	public Device.Configuration config;
	public Device.Status status = new Device.Status();

	public Device(Device.Configuration config) {
		this.config = config;
		this.on(Device.Events.OPEN, (Packet data) -> {
			this.status.ready = true;
			if (this.config.debug) System.out.printf("Device #%s (%s) is ready!\n", this.config.id, this.config.label);
		});
	};
};

