package cloud.renderlabs.ftc;

import cloud.renderlabs.robotics.Environment;

public class Conversion {
	public interface HasDirection {
		enum Direction {
			FORWARD,
			REVERSE,
		};
	};
	public static <T extends Enum<T>> T direction(Environment.RotaryDirection direction, Class<T> type) {
		T[] enumConstants = type.getEnumConstants();
		if (direction == Environment.RotaryDirection.CLOCKWISE) {
			return enumConstants[0];
		} else if (direction == Environment.RotaryDirection.COUNTER_CLOCKWISE) {
			return enumConstants[1];
		};
		return null;
	};
};
