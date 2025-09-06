package cloud.renderlabs.std;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventEmitter<I, O> {
	public interface Listener<O> {
		void handle(O value);
	};

	private Map<I, List<Listener<O>>> events;

	public EventEmitter() {
		this.events = new HashMap<>();
	};

	public void on(I event, Listener<O> listener) {
		this.events.computeIfAbsent(event, k -> new ArrayList<>());

		this.events.get(event).add(listener);
	};

	public void emit(I event, O value) {
		List<Listener<O>> listeners = this.events.getOrDefault(event, new ArrayList<>());

		for (Listener<O> listener : listeners) {
			listener.handle(value);
		};
	};
};