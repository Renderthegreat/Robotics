package cloud.renderlabs.robotics;

import java.util.Map;
import java.util.HashMap;

public class Packet {
	public Map<String, Object> data = new HashMap<>();
	public Device.Configuration config = new Device.Configuration() {{
		
	}};
	public String deviceType;

	public Packet() {
		
	};
};
