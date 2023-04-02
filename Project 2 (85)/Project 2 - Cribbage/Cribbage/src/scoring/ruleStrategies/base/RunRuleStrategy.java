package scoring.ruleStrategies.base;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import events.EventFactory;
import events.eventObjects.CompositeScoreEvent;
import events.eventObjects.GameEvent;
import events.eventObjects.IReportScores;
import events.eventObjects.ScoreEvent;
import scoring.ruleStrategies.IRuleStrategy;

public class RunRuleStrategy implements IRuleStrategy {
	private int MAX_SEQUENCE_PLAY = 7;
	private int MAX_SEQUENCE_SHOW = 5;
	private ArrayList<Integer> pointValues;

	public RunRuleStrategy(ArrayList<Integer> pointValues) {
		this.pointValues = pointValues;
	}

	// segment lastPlayer should be set to -1 if gameState is SHOW
	public GameEvent validateRule(Segment s) {
		Hand segment = s.getSegment();

		// duplicate hand
		Hand hand = new Hand(Cribbage.getDeck());

		for (Card card : segment.getCardList()) {
			hand.insert(card.clone(), false);
		}

		if (s.isPlay()) {

			// iteratively check for sequences at the end of the hand
			for (int i = MAX_SEQUENCE_PLAY; i > 1; i--) {
				if (i <= hand.getNumberOfCards()) {
					Hand temp = new Hand(Cribbage.getDeck());
					// copy the last i cards to a temp hand
					for (int j = i; j > 0; j--) {

						temp.insert(hand.get(hand.getNumberOfCards() - j), false);
					}

					Hand[] sequences = temp.extractSequences(i);

					// if there are sequence it is at tail end because only the last cards were
					// checked
					if (sequences.length > 0) {

						// create event
						GameEvent newEvent = EventFactory.getInstance().createScoreEvent(sequences[0],
								s.getLastPlayer(), getPointValue(sequences[0]), ScoreEvent.RuleType.RUN);

						return newEvent;
					}
				}
			}
		}
		// else it is a show round, score all sequences
		else {
			Hand clone = IRuleStrategy.cloneHandInsertStarter(s.getSegment(), s.getStarter());

			GameEvent newCompositeEvent = EventFactory.getInstance().createCompositeEvent();
			boolean match = false;

			// check for all sequence lengths
			for (int i = MAX_SEQUENCE_SHOW; i > 0; i--) {

				Hand[] sequences = clone.extractSequences(i);

				// create events and add to composite
				if (sequences.length > 0) {
					for (Hand seq : sequences) {
						GameEvent newEvent = EventFactory.getInstance().createScoreEvent(seq, s.getLastPlayer(),
								getPointValue(seq), ScoreEvent.RuleType.RUN);

						((CompositeScoreEvent) newCompositeEvent).add((IReportScores) newEvent);
						match = true;
					}
				}
				if (match) {
					return newCompositeEvent;
				}
			}
		}

		// no sequences in either case
		return null;
	}

	private int getPointValue(Hand sequence) {
		return sequence.getNumberOfCards() * pointValues.get(0);
	}
}
