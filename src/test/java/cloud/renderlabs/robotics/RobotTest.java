package cloud.renderlabs.robotics;

import com.qualcomm.robotcore.hardware.HardwareMap;

import cloud.renderlabs.ftc.Devices.Servo;

import cloud.renderlabs.ftc.REVRoboticsLink;

import static cloud.renderlabs.std.Constants.π;
import cloud.renderlabs.std.Promise;

import org.mockito.Mockito;

public class RobotTest {
	private Robot virtual;
	public static Link link = new Link();

	public static void main(String[] args) throws Exception {
		final RobotTest main = new RobotTest();
		main.initalizeRobot();
		try {
			main.begin();
		} catch (Exception err) {
			RobotTest.link.logger.error("Encountered error: %s", err.getMessage());
		} finally {
			RobotTest.link.logger.log("Exiting...");
			main.close();
		}//;
	};

	public void initalizeRobot() throws Exception {
		this.virtual = new Robot();
		HardwareMap hardwareMapMock = new HardwareMap();

		// TODO: Register a mock device.
		com.qualcomm.robotcore.hardware.Servo servo1 = Mockito.mock(com.qualcomm.robotcore.hardware.Servo.class);
		Mockito.when(servo1.getPortNumber()).thenReturn(1);

		hardwareMapMock.servo.put("Servo1", servo1);

		this.virtual
			.link(new REVRoboticsLink(hardwareMapMock))
			.addConfig(new Servo.Configuration() {{
				this.location = new Device.Location() {{
					this.port = 1;
					this.lane = "I2C";
				}};
				this.label = "Servo1";
				this.debug = true;
			}}, Servo.FACTORY)
			.build()
		;
	};

	public void begin() throws Exception {
		Servo servo1 = this.virtual.select("Servo1", Servo.class);
		Promise.await(servo1.rotate(new Environment.Radians(2 * π), 10L, Environment.RotaryDirection.CLOCKWISE));
	};

	public void close() throws Exception {
		this.virtual.close();
	};
};
