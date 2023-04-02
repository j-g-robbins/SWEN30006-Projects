package events;

import java.util.ArrayList;

import events.eventObjects.GameEvent;
import listeners.CribbageEventListener;

public class EventManager {
	private ArrayList<CribbageEventListener> listeners = new ArrayList<CribbageEventListener>();

	public void addListener(CribbageEventListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(CribbageEventListener listener) {
		this.listeners.remove(listener);
	}

	public void notify(GameEvent data) {
		for (CribbageEventListener l : listeners) {
			l.update(data);
		}
	}
}
