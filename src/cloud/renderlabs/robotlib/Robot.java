package cloud.renderlabs.robotlib;

public class Robot {
	private final Manager manager = new Manager();

	public Robot() {
		
	};

	/**
	 * Adds a device to this robot.
	 * 
	 * @param device The device to add
	 * @return This robot
	 */
	public Robot addDevice(Device device) throws Manager.DeviceReinitializationException {
		if (this.manager.devices.contains(device)) {
			throw new Manager.DeviceReinitializationException(device);
		};

		this.manager.emit(Manager.Events.REGISTER, device);

		return this;
	};

	/**
	 * Adds a device to this robot.
	 * 
	 * @param device The device configuration to add
	 * @return This robot
	 * @throws Manager.DeviceReinitializationException If a device with the given id is already registered
	 */
	public <T extends Device> Robot add(T.Configuration config, Device.Factory<T> factory) throws Manager.DeviceReinitializationException {
		Device device = factory.create(config);
		
		return this.addDevice(device);
	};

	/**
	 * Retrieves a device from the manager by id.
	 * 
	 * @param id The id of the device to retrieve
	 * @param type The class of the device to retrieve
	 * @return The device with the given id and type, or null if no such device exists
	 */
	public <T extends Device> T getDevice(Integer id, Class<T> type) {
		return (T) this.manager.devices
			.stream()
			.filter(device -> device.config.id == id)
			.map(type::cast)
			.findFirst()
			.orElse(null)
		;
	};

	/**
	 * Retrieves a device from the manager by label.
	 * 
	 * @param label The label of the device to retrieve
	 * @param type The class of the device to retrieve
	 * @return The device with the given label and type, or null if no such device exists
	 */
	public <T extends Device> T getDevice(String label, Class<T> type) {
		return (T) this.manager.devices
			.stream()
			.filter(device -> device.config.label.equals(label))
			.map(type::cast)
			.findFirst()
			.orElse(null)
		;
	};

	/**
	 * Retrieves a device from the manager by label. If no device with the given label exists, an error is thrown.
	 * 
	 * @param label The label of the device to retrieve
	 * @param type The class of the device to retrieve
	 * @return The device with the given label and type
	 * @throws Error if no device with the given label exists
	 */
	public <T extends Device> T select(String label, Class<T> type) throws Manager.DeviceNotFoundException, Manager.DeviceNotReadyException {
		T device = this.getDevice(label, type);
		
		if (device == null) {
			throw new Manager.DeviceNotFoundException(label);
		};

		if (!device.status.ready) {
			throw new Manager.DeviceNotReadyException(device);
		};

		return device;
	};
};
