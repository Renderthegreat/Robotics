package cloud.renderlabs.ftc;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController.RunMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import cloud.renderlabs.robotics.Device;
import cloud.renderlabs.robotics.Environment;
import cloud.renderlabs.robotics.Link;
import cloud.renderlabs.robotics.Packet;

/**
 * # This code links the cloud.renderlabs.robotics API to the REV Robotics API.
 * @author Brendan Lucas
 * @see cloud.renderlabs.robotics
 * 
 * To add custom device support,
 * @see REVRoboticsLink.customDevices
 */

public class REVRoboticsLink extends Link {
	/**
	 * Add custom devices here.
	 */
	public Map<String, Consumer<Packet>> customDevices = new HashMap<>();
	public Map<Device.Location, Object> devices = new HashMap<>();

	public HardwareMap hardwareMap;

	public REVRoboticsLink(HardwareMap hardwareMap) {
		this.hardwareMap = hardwareMap;


		final HardwareMap.DeviceMapping<?>[] deviceMappings = {
			this.hardwareMap.accelerationSensor,
			this.hardwareMap.compassSensor,
			this.hardwareMap.dcMotor,
			this.hardwareMap.dcMotorController,
			this.hardwareMap.gyroSensor,
			this.hardwareMap.irSeekerSensor,
			this.hardwareMap.legacyModule,
			this.hardwareMap.lightSensor,
			this.hardwareMap.servo,
			this.hardwareMap.servoController,
			this.hardwareMap.ultrasonicSensor,
			this.hardwareMap.voltageSensor,
		};

		this.on(Link.Events.BUILD, (Packet) -> {
			for (HardwareMap.DeviceMapping<?> deviceMapping : deviceMappings) {					
				for (Map.Entry<String, ?> deviceEntry : deviceMapping.entrySet()) {
					Object device = deviceEntry.getValue();

					if (device == null) {
						continue;
					} else if (device instanceof AccelerationSensor) {
						// ...

					} else if (device instanceof Servo) {
						this.devices.put(new Device.Location() {{
							this.lane = "I2C";
							this.port = ((Servo)device).getPortNumber();
						}}, device);

						continue;
					} else {
						// TODO: Check custom devices

						this.logger.error("Unknown device type: '%s'", device.getClass().getName());
					};

					break;
				};
			};
		});

		this.on(Link.Events.OUT, (Packet packet) -> {
			// ! Make sure to keep them in order, Brendan.
			switch (packet.deviceType) {
				case "motor" -> {
					DcMotor motor = (DcMotor) this.devices.get(
						packet.config.location
					);

					if ((Boolean) packet.data.get("debug") == true) {
						this.logger.log("Motor %s received packet: %s", packet.config.location, packet.data);
					};

					if (packet.data.get("type") == "rotate") {
						motor.setChannelMode(RunMode.RUN_USING_ENCODERS);
						motor.setDirection(
							Conversion.direction((Environment.RotaryDirection) packet.data.get("direction"), DcMotor.Direction.class)
						);
						motor.setPower((Double) packet.data.get("power"));
					};
					
					break;
				}//;

				case "servo" -> {
					Servo servo = (Servo) this.devices.get(
						packet.config.location
					);

					if ((Boolean) packet.data.get("debug") == true) {
						this.logger.log("Servo %s received packet: %s", packet.config.location, packet.data);
					};

					if (packet.data.get("type") == "rotate") {
						servo.setDirection(
							Conversion.<Servo.Direction>direction((Environment.RotaryDirection) packet.data.get("direction"), Servo.Direction.class)
						);
						servo.setPosition((Double) packet.data.get("angle"));
					};

					break;
				}//;

				default -> {
					Consumer<Packet> customDeviceConsumer = this.customDevices.get(packet.deviceType);
					if (customDeviceConsumer != null) {
						customDeviceConsumer.accept(packet);
					};

					this.logger.warn("Unknown device: %s", packet.config.location);
				}//;
			};
		});
		
		this.on(Link.Events.REGISTER, (Packet info) -> {
			Boolean foundDevice = this.devices.keySet().contains(info.config.location);

			if (!foundDevice) {
				this.logger.error("Device not found: %s!", info.config.location);
				this.logger.warn("Device will not be listed.");
				this.logger.debug("%s", this.devices.keySet().toArray());
				return;
			};

			// Announce the new device for adoption.
			this.announce.accept(Device.Events.OPEN, new Packet() {{
				this.config.location = new Device.Location() {{
					this.lane = info.config.location.lane;
					this.port = info.config.location.port;
				}};
			}});
		});

		this.logger.log("Event handlers ready!");
	};

	//public <T> T getDevice(Integer port, Class<T> type) {
		
	//};
};
