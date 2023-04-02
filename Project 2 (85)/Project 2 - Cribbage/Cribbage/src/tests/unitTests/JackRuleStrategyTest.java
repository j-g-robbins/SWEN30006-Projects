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
import scoring.ruleStrategies.base.JackRuleStrategy;

class JackRuleStrategyTest {

	@Test
	final void testNone() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new JackRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);
		start.insert(Suit.CLUBS, Rank.FOUR, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, start, false, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testJackHand() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new JackRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);
		start.insert(Suit.CLUBS, Rank.FOUR, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, start, false, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("JACK1\n", event.toString());
	}

	@Test
	final void testJackCrib() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new JackRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);
		start.insert(Suit.CLUBS, Rank.JACK, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(start, start, false, false));
		// should have zero combinations\
//			assertEquals("FLUSH 0", event.toString());
		assertEquals("JACK1\n", event.toString());
	}

	@Test
	final void testJackCribNoHand() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new JackRuleStrategy(pointValues);
		start.insert(Suit.CLUBS, Rank.JACK, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(start, start, false, false));
		// should have zero combinations\
//			assertEquals("FLUSH 0", event.toString());
		assertEquals("JACK1\n", event.toString());
	}

	@Test
	final void testNoHand() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new JackRuleStrategy(pointValues);
		start.insert(Suit.CLUBS, Rank.JACK, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, start, false, false));
		// should have zero combinations\
//			assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

}