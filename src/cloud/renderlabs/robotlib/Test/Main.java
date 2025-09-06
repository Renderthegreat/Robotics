package cloud.renderlabs.robotlib.Test;


import cloud.renderlabs.robotlib.Robot;
import cloud.renderlabs.robotlib.Device;
import cloud.renderlabs.robotlib.Devices.*;

public class Main {
	private Robot virtual;

	public static void main(String args[]) {
		new Main();
	};

	public Main() {
		this.virtual = new Robot();

		Motor.Configuration motorConfig = new Motor.Configuration();
		Device motor = new Motor(motorConfig);

		this.virtual
			.addDevice(motor)
		;
	};
};
