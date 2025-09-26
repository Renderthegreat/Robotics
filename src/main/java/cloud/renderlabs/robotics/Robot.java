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
	 * @throws Manager.DeviceReinitializationException if a device with the given port is already registered
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
	 * @throws Manager.MutableException if the manager is still mutable
	 */
	public Robot link(Link link) throws Manager.ImmutableExcetion {
		if (this.manager.getMutable() == false) {
			throw new Manager.ImmutableExcetion();
		};

		link.emit(Link.Events.BUILD, null);
		
		link.announce = (Device.Events event, Packet packet) -> {
			Device device = this.manager.devices.stream()
				.filter(d -> d.config.location.lane.equals(packet.config.location.lane) && d.config.location.port.equals(packet.config.location.port))
				.findFirst()
				.orElse(null)
			;

			if (device != null) {
				device.emit(event, packet);		
			};
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
	 * Cleanly close the robot's link
	 */
	public void close() {
		this.manager.emit(Manager.Events.CLOSE, this);
	};

	/**
	 * Retrieves a device from the manager by port.
	 * 
	 * @param port The port of the device to retrieve
	 * @param type The class of the device to retrieve
	 * @return The device with the given id and type, or null if no such device exists
	 * @throws Manager.MutableException if the manager is still mutable
	 */
	public <T extends Device> T getDevice(Integer port, Class<T> type) throws Manager.MutableException {
		if (this.manager.getMutable() == true) {
			throw new Manager.MutableException();
		};

		return (T) this.manager.devices
			.stream()
			.filter(device -> device.config.location.port == port)
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
	 * Retrieves a device from the manager by label.
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
