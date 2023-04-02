package scoring.ruleStrategies.base;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import events.EventFactory;
import events.eventObjects.GameEvent;
import events.eventObjects.ScoreEvent;
import scoring.ruleStrategies.IRuleStrategy;

public class LastCardRuleStrategy implements IRuleStrategy {
	private ArrayList<Integer> pointValues;
	private static final int THIRTYONE = 31;

	public LastCardRuleStrategy(ArrayList<Integer> pointValues) {
		this.pointValues = pointValues;
	}

	public GameEvent validateRule(Segment s) {
		if (s.isNewSegment()) {

			// create event
			Hand hand = s.getSegment();
			int totalPoints = 0;

			for (Card card : hand.getCardList()) {
				totalPoints += Cribbage.Rank.valueOf(card.getRank().toString()).value;
			}

			if (totalPoints != THIRTYONE) {
				GameEvent newEvent = EventFactory.getInstance().createScoreEvent(null, s.getLastPlayer(),
						calcPointValue(), ScoreEvent.RuleType.LASTCARD);

				return newEvent;
			}
		}
		return null;
	}

	private int calcPointValue() {
		return pointValues.get(0);
	}
}
