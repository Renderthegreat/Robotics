package cloud.renderlabs.robotics;

public class Environment {
	public static enum RotaryDirection {
		CLOCKWISE(1),
		COUNTER_CLOCKWISE(-1);

		public final Integer value;
		private RotaryDirection(Integer value) {
			this.value = value;
		};
	};

	public static enum State {
		ON,
		OFF,
	};

	public static class Radians {
		public Double value;
		public Radians(Double value) {
			this.value = value;
		};
	};
};
