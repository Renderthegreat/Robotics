package cloud.renderlabs.robotlib;

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
	};

	public static class DeviceReinitializationException extends Exception {
		public DeviceReinitializationException(Device device) {
			super("Device #" + device.config.id + " (" + device.config.label + ") is already registered");
		};
	};

	public static class DeviceNotReadyException extends Exception {
		public DeviceNotReadyException(Device device) {
			super("Device #" + device.config.id + " (" + device.config.label + ") is not ready");
		};
	};


	public static class DeviceNotFoundException extends Exception {
		public DeviceNotFoundException(String label) {
			super("Device \"" + label + "\" was not found");
		};
	};

	public List<Device> devices = new ArrayList<>();

	public Manager() {
		this.on(Manager.Events.REGISTER, (Object data) -> {
			Device device = (Device) data;
			
			/**
			 * TODO:
			 * - Add proper initialization logic
			 */
			device.emit(Device.Events.OPEN, new Packet());

			this.devices.add((Device) device);
		});	
	};
};
