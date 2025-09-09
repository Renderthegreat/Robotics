package cloud.renderlabs.robotics;

public class Robot {
	private final Manager manager = new Manager();
	public Robot() {
		
	};

	/**
	 * Adds a device to this robot.
	 * 
	 * @param device The device to add
	 * @return This robot
	 * @throws Manager.DeviceReinitializationException if a device with the given id is already registered
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
	 * @throws Manager.DeviceReinitializationException if a device with the given id is already registered
	 */
	public <T extends Device> Robot addConfig(T.Configuration config, Device.Factory<T> factory) throws Manager.DeviceReinitializationException {
		Device device = factory.create(config);
		
		return this.addDevice(device);
	};

	/**
	 * Link the robot to interfaces.
	 * 
	 * @param link The link to use
	 * @return This robot
	 */
	public Robot link(Link link) throws Manager.ImmutableExcetion {
		if (this.manager.getMutable() == false) {
			throw new Manager.ImmutableExcetion();
		};

		this.manager.link = link;

		return this;
	};

	/**
	 * Lock the robot's configuration and link.
	 * 
	 */
	public void build() {
		this.manager.emit(Manager.Events.LOCK, null);
	};

	/**
	 * Retrieves a device from the manager by id.
	 * 
	 * @param id The id of the device to retrieve
	 * @param type The class of the device to retrieve
	 * @return The device with the given id and type, or null if no such device exists
	 * @throws Manager.MutableException if the manager is still mutable
	 */
	public <T extends Device> T getDevice(Integer id, Class<T> type) throws Manager.MutableException {
		if (this.manager.getMutable() == true) {
			throw new Manager.MutableException();
		};

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
	 * @throws Manager.MutableException if the manager is still mutable
	 */
	public <T extends Device> T getDevice(String label, Class<T> type) throws Manager.MutableException {
		if (this.manager.getMutable() == true) {
			throw new Manager.MutableException();
		};
	
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
	 * @throws Manager.DeviceNotFoundException if no device with the given label exists
	 * @throws Manager.DeviceNotReadyException if the device with the given label is not ready
	 * @throws Manager.MutableException if the manager is still mutable
	 */
	public <T extends Device> T select(String label, Class<T> type) throws Manager.DeviceNotFoundException, Manager.DeviceNotReadyException, Manager.MutableException {
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
