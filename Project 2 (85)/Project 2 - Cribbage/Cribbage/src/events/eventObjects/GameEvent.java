package events.eventObjects;

import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;

public class GameEvent {

	public enum EventType {
		PLAY("play"), SCORE("score"), SHOW("show"), DEAL("deal"), DISCARD("discard"), STARTER("starter");

		public final String label;

		private EventType(String label) {
			this.label = label;
		}
	}

	public enum PlayerType {
		P0("P0"), P1("P1");

		public final String label;

		private PlayerType(String label) {
			this.label = label;
		}
	}

	private EventType eventType;
	private PlayerType playerType;
	private Hand hand;

	public GameEvent(EventType eventType, PlayerType playerType, Hand hand) {
		this.eventType = eventType;
		this.playerType = playerType;
		this.hand = hand;
	}

	public EventType getEventType() {
		return eventType;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public Hand getHand() {
		return hand;
	}

	public String toString() {
		return "" + eventType.label + "," + playerType.label + "," + Cribbage.canonical(hand) + "\n";
	}
}
