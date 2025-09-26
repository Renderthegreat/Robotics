package cloud.renderlabs.robotics;

import cloud.renderlabs.std.EventEmitter;
import java.util.ArrayList;
import java.util.List;

public class Manager extends EventEmitter<Manager.Events, Object> {
	/**
	 * TODO:
	 * - Manage devices
	 */

	public static enum Events {
		REGISTER,
		LOCK,
		CLOSE,
	};

	public static class DeviceReinitializationException extends Exception {
		public DeviceReinitializationException(Device device) {
			super("Device " + device.config.location + " is already registered.");
		};
	};

	public static class DeviceNotReadyException extends Exception {
		public DeviceNotReadyException(Device device) {
			super("Device " + device.config.location + " is not ready.");
		};
	};

	public static class DeviceNotFoundException extends Exception {
		public DeviceNotFoundException(String label) {
			super("Device \"" + label + "\" was not found.");
		};
	};

	public static class ImmutableExcetion extends Exception {
		public ImmutableExcetion() {
			super("Manager is immutable.");
		};
	};

	public static class MutableException extends Exception {
		public MutableException() {
			super("Manager is still mutable.");
		};
	};

	public List<Device> devices = new ArrayList<>();
	
	public Link link = null;

	private Boolean mutable = true;

	public Manager() {		
		this.on(Manager.Events.REGISTER, (Object data) -> {
			Device device = (Device) data;

			device.on(Device.Events.OUT, (Packet packet) -> {
				this.link.emit(Link.Events.OUT, packet);
			});

			Packet packet = new Packet() {{
				this.config.location.port = device.config.location.port;
				this.config.location.lane = device.config.location.lane;
			}};

			this.devices.add((Device) device);

			this.link.emit(Link.Events.REGISTER, packet);
		});
		
		this.on(Manager.Events.LOCK, (Object data) -> {
			this.mutable = false;
		});

		this.on(Manager.Events.CLOSE, (Object) -> {
			this.link.emit(Link.Events.CLOSE, null);
		});
	};

	public Boolean getMutable() {
		return this.mutable;
	};
};
