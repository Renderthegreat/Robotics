package cloud.renderlabs.robotics;

import cloud.renderlabs.std.*;
import java.util.function.BiConsumer;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;

public class Link extends EventEmitter<Link.Events, Packet> {
	public class LoggerBuilder {
		public String name;

		public LoggerBuilder(String name) {
			this.name = name;
		};

		public void debug(String format, Object ...args) {
			System.out.printf("[" + Ansi.colorize(this.name, Attribute.GREEN_TEXT()) + "] " + Ansi.colorize(format, Attribute.DIM()) + "\n", args);
		};

		public void log(String format, Object ...args) {
			System.out.printf("[" + Ansi.colorize(this.name, Attribute.GREEN_TEXT()) + "] " + Ansi.colorize(format, Attribute.WHITE_TEXT()) + "\n", args);
		};

		public void warn(String format, Object ...args) {
			System.err.printf("[" + Ansi.colorize(this.name, Attribute.GREEN_TEXT()) + "] " + Ansi.colorize(format, Attribute.YELLOW_TEXT(), Attribute.BOLD()) + "\n", args);
		};

		public void error(String format, Object ...args) {
			System.err.printf("[" + Ansi.colorize(this.name, Attribute.GREEN_TEXT()) + "] " + Ansi.colorize(format, Attribute.RED_TEXT(), Attribute.BOLD()) + "\n", args);
		};
	};

	public Link.LoggerBuilder logger;

	public BiConsumer<Device.Events, Packet> announce;


	public static enum Events {
		IN,
		OUT,
		REGISTER,
		CLOSE,
		BUILD,
	};

	public Link() {
		String name = this.getClass().getName();

		this.logger = new LoggerBuilder(name);
	};
};
