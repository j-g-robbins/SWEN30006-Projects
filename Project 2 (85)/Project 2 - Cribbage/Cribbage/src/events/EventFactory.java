package events;

import ch.aplu.jcardgame.Hand;
import events.eventObjects.CompositeScoreEvent;
import events.eventObjects.GameEvent;
import events.eventObjects.GameEvent.EventType;
import events.eventObjects.GameEvent.PlayerType;
import events.eventObjects.ScoreEvent;
import events.eventObjects.ScoreEvent.RuleType;
import events.eventObjects.StatusEvent;

public class EventFactory {
	private static EventFactory instance = null;

	private int p0Score = 0;
	private int p1Score = 0;

	public static EventFactory getInstance() {
		if (instance == null) {
			instance = new EventFactory();
		}
		return instance;
	}

	public GameEvent createEvent(Hand hand, int player, EventType eventType, int cardTotal) {
		if (player == -1) {
			GameEvent newEvent = new StatusEvent(eventType, null, hand, cardTotal);

			return newEvent;
		}
		if (player == 0) {
			GameEvent newEvent = new StatusEvent(eventType, PlayerType.P0, hand, cardTotal);

			return newEvent;
		} else {
			GameEvent newEvent = new StatusEvent(eventType, PlayerType.P1, hand, cardTotal);

			return newEvent;
		}
	}

	// all data populated other than playerScore
	public GameEvent createScoreEvent(Hand hand, int player, int eventScore, RuleType ruleType) {

		if (player == 0) {
			p0Score += eventScore;
			GameEvent newEvent = new ScoreEvent(EventType.SCORE, PlayerType.P0, hand, p0Score, eventScore, ruleType);

			return newEvent;
		} else {
			p1Score += eventScore;
			GameEvent newEvent = new ScoreEvent(EventType.SCORE, PlayerType.P1, hand, p1Score, eventScore, ruleType);
			return newEvent;
		}
	}

	public GameEvent createCompositeEvent() {
		GameEvent newEvent = new CompositeScoreEvent(EventType.SCORE, null, null);

		return newEvent;
	}
}