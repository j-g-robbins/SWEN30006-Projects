package scoring.ruleStrategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import events.eventObjects.GameEvent;

public interface IRuleStrategy {
	public GameEvent validateRule(Segment s);

	static Hand cloneHandInsertStarter(Hand hand, Hand starter) {
		// clone hand and starter
		Hand cloned = new Hand(Cribbage.getDeck());
		for (Card card : hand.getCardList()) {
			cloned.insert(card.clone(), false);
		}
		cloned.insert(starter.get(0).clone(), false);
		return cloned;
	}
}
