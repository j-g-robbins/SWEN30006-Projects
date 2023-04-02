package scoring.ruleStrategies.base;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage.Segment;
import cribbage.Cribbage.Suit;
import events.EventFactory;
import events.eventObjects.GameEvent;
import events.eventObjects.ScoreEvent;
import scoring.ruleStrategies.IRuleStrategy;

public class FlushRuleStrategy implements IRuleStrategy {
	private static final int LOW_FLUSH = 4;
	private static final int HIGH_FLUSH = 5;
	private ArrayList<Integer> pointValues;

	public FlushRuleStrategy(ArrayList<Integer> pointValues) {
		this.pointValues = pointValues;
	}

	public GameEvent validateRule(Segment s) {

		Hand hand = IRuleStrategy.cloneHandInsertStarter(s.getSegment(), s.getStarter());

		// check for flush of each suit
		for (Suit suit : Suit.class.getEnumConstants()) {
			Hand flush = s.getSegment().extractCardsWithSuit(suit);

			// check for flushLength
			int flushLength = flush.getNumberOfCards();
			if (flushLength == LOW_FLUSH || flushLength == HIGH_FLUSH) {
				// create event
				GameEvent newEvent = EventFactory.getInstance().createScoreEvent(flush, s.getLastPlayer(),
						calcPointValue(flushLength), ScoreEvent.RuleType.FLUSH);

				return newEvent;
			}
		}

		// no flush
		return null;
	}

	private int calcPointValue(int flushLength) {
		return pointValues.get(0) * flushLength;
	}
}
