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
import scoring.ruleStrategies.base.FlushRuleStrategy;

class FlushRuleStrategyTest {

	@Test
	final void testNone() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new FlushRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testFlushLowExtraCards() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new FlushRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.CLUBS, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.EIGHT, false);
		hand.insert(Suit.CLUBS, Rank.EIGHT, false);
		hand.insert(Suit.SPADES, Rank.EIGHT, false);

//		System.out.println(hand.toString());
		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, false));
		// should have zero combinations\
//		System.out.println(event.toString());
		assertEquals("FLUSH4\n", event.toString());
	}

	@Test
	final void testFlushLow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new FlushRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.CLUBS, Rank.FOUR, false);
		hand.insert(Suit.CLUBS, Rank.EIGHT, false);
//		System.out.println(hand.toString());
		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, false));
		// should have zero combinations\
//		System.out.println(event.toString());
		assertEquals("FLUSH4\n", event.toString());
	}

	@Test
	final void testFlushHigh() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new FlushRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.CLUBS, Rank.FOUR, false);
		hand.insert(Suit.CLUBS, Rank.EIGHT, false);
		hand.insert(Suit.CLUBS, Rank.TEN, false);
//		System.out.println(hand.toString());
		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, false));
		// should have zero combinations\
//		System.out.println(event.toString());
		assertEquals("FLUSH5\n", event.toString());
	}
}
