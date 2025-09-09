package cloud.renderlabs.robotics;

import cloud.renderlabs.std.*;

public class Link extends EventEmitter<Link.Events, Packet> {
	public class LoggerBuilder {
		public String name;

		public LoggerBuilder(String name) {
			this.name = name;
		};

		public void log(String format, Object ...args) {
			System.out.printf("[" + this.name + "] " + format + "\n", args);
		};
	};

	public Link.LoggerBuilder logger;

	public static enum Events {
		IN,
		OUT,
	};

	public Link() {
		String name = this.getClass().getName();

		this.logger = new LoggerBuilder(name);
	};
};
