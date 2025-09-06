package cloud.renderlabs.robotlib.Test;

import cloud.renderlabs.robotlib.Robot;
import cloud.renderlabs.robotlib.Environment;
import cloud.renderlabs.robotlib.Devices.Motor;

import cloud.renderlabs.std.Promise;
import static cloud.renderlabs.std.Constants.π;

public class Main {
	private Robot virtual;

	public static void main(String args[]) throws Exception {
		new Main();
	};

	public Main() throws Exception {
		this.virtual = new Robot();


		this.virtual
			.add(new Motor.Configuration() {{
				this.id = 1;
				this.label = "Motor1";
				this.debug = true;
			}}, Motor.FACTORY)
		;

		this.begin();

		this.close();
	};

	public void begin() throws Exception {
		Motor motor1 = this.virtual.select("Motor1", Motor.class);

		Promise.await(motor1.rotate(new Environment.Radians(2 * π), Environment.RotaryDirection.CLOCKWISE));
	};

	public void close() throws Exception {
		// TODO:
		// this.virtual.close();
	};
};
