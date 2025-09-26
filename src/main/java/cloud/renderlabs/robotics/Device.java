package cloud.renderlabs.robotics;

import java.util.Objects;

import cloud.renderlabs.std.EventEmitter;

public abstract class Device extends EventEmitter<Device.Events, Packet> {
	public static abstract class Configuration {
		public Device.Location location = new Device.Location();
		public String label;
		public Boolean debug = false;
	};

	public static class Location {
		public String lane;
		public Integer port;

		@Override
		public String toString() {
			return "{lane='" + this.lane + "', port='" + this.port + "'}";
		};

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;

			if (o == null/* || Device.Location.class != o.getClass()*/) {
				return false;
			};

			Device.Location that = (Device.Location) o;

			return port == that.port && Objects.equals(lane, that.lane);
		};

		@Override
    	public int hashCode() {
        	return Objects.hash(lane, port);
    	};
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
		});
	};
};

