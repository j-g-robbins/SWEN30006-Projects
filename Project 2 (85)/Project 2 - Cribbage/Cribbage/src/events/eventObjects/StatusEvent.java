package events.eventObjects;

import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;

public class StatusEvent extends GameEvent {

	private int cardTotal;

	public StatusEvent(EventType eventType, PlayerType playerType, Hand hand, int cardTotal) {
		super(eventType, playerType, hand);
		this.cardTotal = cardTotal;
	}

	public int getCardTotal() {
		return cardTotal;
	}

	public String toString() {
		String eventString = this.getEventType().label;
		String playerNumber = "";
		String cardTotalString = "";
		String handString;

		if (this.getPlayerType() != null) {
			playerNumber = "," + this.getPlayerType().label;
		}

		if (this.getHand().getNumberOfCards() == 1) {
			handString = "," + Cribbage.canonical(this.getHand().getFirst());
		} else {
			handString = "," + Cribbage.canonical(this.getHand());
		}

		if (cardTotal > 0 && (this.getEventType() != EventType.STARTER)) {
			cardTotalString = "," + cardTotal;
		}

		// format hand to show the starter
		if (this.getEventType() == EventType.SHOW) {
			String starterString = Cribbage.canonical(this.getHand().getLast()) + "+";
			this.getHand().removeLast(false);
			handString = "," + starterString + Cribbage.canonical(this.getHand());
		}

		return "" + eventString + playerNumber + cardTotalString + handString + "\n";
	}
}
