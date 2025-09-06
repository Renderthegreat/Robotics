package cloud.renderlabs.robotlib;
import java.util.ArrayList;
import java.util.List;

public class Robot {
	private final List<Device> devices = new ArrayList<>();

	public Robot() {
		
	};

	public Robot addDevice(Device device) {
		this.devices.add(device);

		return this;
	};
};
