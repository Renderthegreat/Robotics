package cloud.renderlabs.ftc;

import cloud.renderlabs.robotics.Link;
import cloud.renderlabs.robotics.Packet;

/**
 * This code links the cloud.renderlabs.robotlib SDK to the robot.
 */

public class FTCLink extends Link {
	// TODO: Add FTC cache

	public FTCLink() {
		this.on(Link.Events.IN, (Packet packet) -> {
			switch (packet.data.get("device").toString()) {
				case "motor": {
					this.logger.log("Motor #%s received packet: %s", packet.data.get("id"), packet.data);
					break;
				}//; :( Why can't I put a semicolon here!

				default: {
					this.logger.log("Unknown device: %s", packet.data.get("device"));
				};
			};
		});
	};
};
