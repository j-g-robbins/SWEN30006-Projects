package scoring.ruleStrategies.base;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import events.EventFactory;
import events.eventObjects.GameEvent;
import events.eventObjects.ScoreEvent.RuleType;
import scoring.ruleStrategies.IRuleStrategy;

public class TotalPointsRuleStrategy implements IRuleStrategy {
	private ArrayList<Integer> pointValues;

	private static final int FIFTEEN = 15;
	private static final int THIRTYONE = 31;
	private static final int THIRTYONE_MODIFIER = 2;

	public TotalPointsRuleStrategy(ArrayList<Integer> pointValues) {
		this.pointValues = pointValues;
	}

	public GameEvent validateRule(Segment s) {
		Hand hand = s.getSegment();

		int totalPoints = 0;

		for (Card card : hand.getCardList()) {
			totalPoints += Cribbage.Rank.valueOf(card.getRank().toString()).value;
		}

		if (totalPoints == FIFTEEN) {
			GameEvent newEvent = EventFactory.getInstance().createScoreEvent(null, s.getLastPlayer(),
					getPointValue(totalPoints), RuleType.FIFTEEN);

			return newEvent;

		} else if (totalPoints == THIRTYONE) {
			GameEvent newEvent = EventFactory.getInstance().createScoreEvent(null, s.getLastPlayer(),
					getPointValue(totalPoints), RuleType.THIRTYONE);

			return newEvent;
		}
		return null;
	}

	private int getPointValue(int score) {
		if (score == FIFTEEN) {
			return pointValues.get(0);
		} else {
			return pointValues.get(0) * THIRTYONE_MODIFIER;
		}
	}
}
