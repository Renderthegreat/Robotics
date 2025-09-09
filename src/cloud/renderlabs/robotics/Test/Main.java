package cloud.renderlabs.robotics.Test;

import cloud.renderlabs.std.Promise;
import static cloud.renderlabs.std.Constants.π;

import cloud.renderlabs.ftc.FTCLink;
import cloud.renderlabs.ftc.Devices.Servo;

import cloud.renderlabs.robotics.Environment;
import cloud.renderlabs.robotics.Robot;

public class Main {
	private Robot virtual;

	public static void main(String args[]) throws Exception {
		new Main();
	};

	public Main() throws Exception {
		this.virtual = new Robot();

		this.virtual
			.addConfig(new Servo.Configuration() {{
				this.id = 1;
				this.label = "Servo1";
				this.debug = true;
			}}, Servo.FACTORY)
			.link(new FTCLink())
			.build()
		;

		this.begin();

		this.close();
	};

	public void begin() throws Exception {
		Servo servo1 = this.virtual.select("Servo1", Servo.class);

		Promise.await(servo1.rotate(new Environment.Radians(2 * π), 10L, Environment.RotaryDirection.CLOCKWISE));
	};

	public void close() throws Exception {
		// TODO:
		// this.virtual.close();
	};
};
