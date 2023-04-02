package tests.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Rank;
import cribbage.Cribbage.Suit;
import events.eventObjects.GameEvent;
import scoring.ruleStrategies.IRuleStrategy;
import scoring.ruleStrategies.base.LastCardRuleStrategy;

class LastCardRuleStrategyTest {

	@Test
	final void testNone() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new LastCardRuleStrategy(pointValues);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, start, false, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testNotLast() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new LastCardRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);
		start.insert(Suit.CLUBS, Rank.FOUR, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, start, false, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testLast() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new LastCardRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);
		start.insert(Suit.CLUBS, Rank.FOUR, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, start, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("LASTCARD1\n", event.toString());
	}
}
