package cloud.renderlabs.robotics;

import java.util.ArrayList;
import java.util.List;

import cloud.renderlabs.std.EventEmitter;

public class Manager extends EventEmitter<Manager.Events, Object> {
	/**
	 * TODO:
	 * - Manage devices
	 */

	public static enum Events {
		REGISTER,
		LOCK,
	};

	public static class DeviceReinitializationException extends Exception {
		public DeviceReinitializationException(Device device) {
			super("Device #" + device.config.id + " (" + device.config.label + ") is already registered.");
		};
	};

	public static class DeviceNotReadyException extends Exception {
		public DeviceNotReadyException(Device device) {
			super("Device #" + device.config.id + " (" + device.config.label + ") is not ready.");
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
			
			/**
			 * TODO:
			 * - Add proper initialization logic
			 */

			device.emit(Device.Events.OPEN, new Packet());

			device.on(Device.Events.IN, (Packet packet) -> {
				this.link.emit(Link.Events.IN, packet);
			});

			this.devices.add((Device) device);
		});
		
		this.on(Manager.Events.LOCK, (Object data) -> {
			this.mutable = false;
		});
	};

	public Boolean getMutable() {
		return this.mutable;
	};
};
