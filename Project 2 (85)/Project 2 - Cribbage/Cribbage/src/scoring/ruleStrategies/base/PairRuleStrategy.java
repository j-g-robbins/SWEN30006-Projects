package scoring.ruleStrategies.base;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Rank;
import cribbage.Cribbage.Segment;
import events.EventFactory;
import events.eventObjects.CompositeScoreEvent;
import events.eventObjects.GameEvent;
import events.eventObjects.IReportScores;
import events.eventObjects.ScoreEvent;
import scoring.ruleStrategies.IRuleStrategy;

public class PairRuleStrategy implements IRuleStrategy {
	private static final int QUAD_LENGTH = 4;
	private ArrayList<Integer> pointValues;

	public PairRuleStrategy(ArrayList<Integer> pointValues) {
		this.pointValues = pointValues;
	}

	// segment lastPlayer should be set to -1 if gameState is SHOW
	public GameEvent validateRule(Segment s) {
		Hand segment = s.getSegment();
		Hand hand = new Hand(Cribbage.getDeck());

		if (segment.getNumberOfCards() == 0) {
			return null;
		}

		for (Card card : segment.getCardList()) {
			hand.insert(card.clone(), false);
		}

		// only check last 4 cards
		if (s.isPlay()) {

			// iteratively check the last 4, then 3, then 2 cards for matches
			for (int i = QUAD_LENGTH; i > 1; i--) {

				if (segment.getNumberOfCards() < i) {
					continue;
				}
				Hand matched = new Hand(Cribbage.getDeck());
				boolean isMatch = true;

				Rank lastRank = Cribbage.Rank.valueOf(hand.get(hand.getNumberOfCards() - 1).getRank().toString());
				// check the last i cards for matching
				for (int j = i; j > 0; j--) {
					Rank currentRank = Cribbage.Rank
							.valueOf(hand.get(hand.getNumberOfCards() - j).getRank().toString());

					if (!lastRank.equals(currentRank)) {
						isMatch = false;
					}
				}

				if (isMatch) {
					// copy last i cards to matched hand and then create event
					for (int j = i; j > 0; j--) {
						matched.insert(hand.get(hand.getNumberOfCards() - j).clone(), false);
					}

					GameEvent newEvent = EventFactory.getInstance().createScoreEvent(matched, s.getLastPlayer(),
							getPointValue(matched.getNumberOfCards()), ScoreEvent.RuleType.PAIR);

					return newEvent;
				}
			}

		}
		// else find all pairs
		else {

			Hand clone = IRuleStrategy.cloneHandInsertStarter(s.getSegment(), s.getStarter());

			ArrayList<Card[]> matches;
			boolean match = false;
			GameEvent newCompositeEvent = EventFactory.getInstance().createCompositeEvent();

			// check for pairs, then trips, then quads.
			// Pairs/trips will not be returned if they are part of a larger match
			if ((matches = clone.getPairs()).size() > 0) {
				match = true;
				for (Card[] cards : matches) {
					GameEvent newEvent = createMatchEvent(cards, s);
					((CompositeScoreEvent) newCompositeEvent).add((IReportScores) newEvent);
				}

			}
			if ((matches = clone.getTrips()).size() > 0) {
				match = true;
				for (Card[] cards : matches) {
					GameEvent newEvent = createMatchEvent(cards, s);
					((CompositeScoreEvent) newCompositeEvent).add((IReportScores) newEvent);
				}

			}
			if ((matches = clone.getQuads()).size() > 0) {
				match = true;
				for (Card[] cards : matches) {
					GameEvent newEvent = createMatchEvent(cards, s);
					((CompositeScoreEvent) newCompositeEvent).add((IReportScores) newEvent);
				}
			}
			if (match) {
				return newCompositeEvent;
			}
		}

		// no matches in either case
		return null;
	}

	private GameEvent createMatchEvent(Card[] cards, Segment s) {
		Hand tmpHand = new Hand(Cribbage.getDeck());

		for (Card card : cards) {
			tmpHand.insert(card, false);
		}
		GameEvent newEvent = EventFactory.getInstance().createScoreEvent(tmpHand, s.getLastPlayer(),
				getPointValue(tmpHand.getNumberOfCards()), ScoreEvent.RuleType.PAIR);

		return newEvent;
	}

	// return the corresponding pointValue
	private int getPointValue(int length) {
		return pointValues.get(length - 2);
	}
}