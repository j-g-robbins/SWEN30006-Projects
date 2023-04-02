package events.eventObjects;

import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;

public class ScoreEvent extends GameEvent implements IReportScores {

	public enum RuleType {
		RUN("run"), PAIR("pair"), JACK("jack"), FLUSH("flush"), LASTCARD("go"), COMBINATION("fifteen"), TOTALPOINTS(""),
		FIFTEEN("fifteen"), THIRTYONE("thirtyone");

		public final String label;

		private RuleType(String label) {
			this.label = label;
		}
	}

	private int playerScore;
	private int eventScore;
	private RuleType ruleType;

	public ScoreEvent(EventType eventType, PlayerType playerType, Hand hand, int playerScore, int eventScore,
			RuleType ruleType) {
		super(eventType, playerType, hand);
		this.playerScore = playerScore;
		this.eventScore = eventScore;
		this.ruleType = ruleType;
	}

	public int getScore() {
		return eventScore;
	}

	public String toString() {
		String eventString = this.getEventType().label;
		String playerString = this.getPlayerType().label;
		String ruleString = this.ruleType.label;
		String handString = "";

		if (this.getHand() != null) {
			handString = "," + Cribbage.canonical(this.getHand());

			if (this.ruleType == RuleType.PAIR || this.ruleType == RuleType.FLUSH || this.ruleType == RuleType.RUN) {
				ruleString += this.getHand().getNumberOfCards();
			}
		}

		return "" + eventString + "," + playerString + "," + playerScore + "," + eventScore + "," + ruleString
				+ handString + "\n";
	}
}
