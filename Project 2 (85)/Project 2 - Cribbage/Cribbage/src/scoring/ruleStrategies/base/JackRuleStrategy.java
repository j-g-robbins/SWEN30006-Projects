package scoring.ruleStrategies.base;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage.Rank;
import cribbage.Cribbage.Segment;
import events.EventFactory;
import events.eventObjects.GameEvent;
import events.eventObjects.ScoreEvent;
import scoring.ruleStrategies.IRuleStrategy;

public class JackRuleStrategy implements IRuleStrategy {
	private ArrayList<Integer> pointValues;

	public JackRuleStrategy(ArrayList<Integer> pointValues) {
		this.pointValues = pointValues;
	}

	public GameEvent validateRule(Segment s) {
		Enum starterSuit = s.getStarter().get(0).getSuit();

		// extract Jacks of same suit
		Hand jack = s.getSegment().extractCardsWithRank(Rank.JACK).extractCardsWithSuit(starterSuit);

		// if lastPlayed
		if (jack.getNumberOfCards() > 0) {
			GameEvent newEvent = EventFactory.getInstance().createScoreEvent(jack, s.getLastPlayer(), calcPointValue(),
					ScoreEvent.RuleType.JACK);

			return newEvent;
		}

		// no jack/suit match
		return null;
	}

	private int calcPointValue() {
		return pointValues.get(0);
	}
}
