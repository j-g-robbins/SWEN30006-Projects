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
import scoring.ruleStrategies.base.CombinationRuleStrategy;

class CombinationRuleStrategyTest {

	@Test
	final void testNoneShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new CombinationRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.ACE, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testFifteenShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new CombinationRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FIVE, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("FIFTEEN1" + "\n", event.toString());
	}

	@Test
	final void testFifteenTwoShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new CombinationRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.FIVE, false);
		hand.insert(Suit.SPADES, Rank.FIVE, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("FIFTEEN1\nFIFTEEN1\n", event.toString());
	}

	@Test
	final void testFifteenFOURShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new CombinationRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.FIVE, false);
		hand.insert(Suit.SPADES, Rank.FIVE, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("FIFTEEN1\nFIFTEEN1\nFIFTEEN1\nFIFTEEN1\n", event.toString());
	}
//
//	@Test
//	final void testTripShow() {
//		Cribbage cribbage = new Cribbage();
//		Deck deck = Cribbage.getDeck();
//		Hand hand = new Hand(deck);
//		ArrayList<Integer> pointValues = new ArrayList<Integer>();
//		pointValues.add(1);
//		pointValues.add(2);
//		pointValues.add(3);
//		IRuleStrategy strat = new FifteenRuleStrategy(pointValues);
//		hand.insert(Suit.CLUBS, Rank.JACK, false);
//		hand.insert(Suit.CLUBS, Rank.TWO, false);
//		hand.insert(Suit.SPADES, Rank.TWO, false);
//		hand.insert(Suit.HEARTS, Rank.TWO, false);
//
//		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
//		// should have zero combinations\
////		assertEquals("FLUSH 0", event.toString());
//		assertEquals("Fifteen2" + "\n", event.toString());
//	}
//
//	@Test
//	final void testQuadShow() {
//		Cribbage cribbage = new Cribbage();
//		Deck deck = Cribbage.getDeck();
//		Hand hand = new Hand(deck);
//		ArrayList<Integer> pointValues = new ArrayList<Integer>();
//		pointValues.add(1);
//		pointValues.add(2);
//		pointValues.add(3);
//		IRuleStrategy strat = new FifteenRuleStrategy(pointValues);
//		hand.insert(Suit.DIAMONDS, Rank.TWO, false);
//		hand.insert(Suit.CLUBS, Rank.TWO, false);
//		hand.insert(Suit.SPADES, Rank.TWO, false);
//		hand.insert(Suit.HEARTS, Rank.TWO, false);
//
//		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
//		// should have zero combinations\
////		assertEquals("FLUSH 0", event.toString());
//		assertEquals("Fifteen3" + "\n", event.toString());
//	}
//
//	@Test
//	final void testTripAndFifteenShow() {
//		Cribbage cribbage = new Cribbage();
//		Deck deck = Cribbage.getDeck();
//		Hand hand = new Hand(deck);
//		ArrayList<Integer> pointValues = new ArrayList<Integer>();
//		pointValues.add(1);
//		pointValues.add(2);
//		pointValues.add(3);
//		IRuleStrategy strat = new FifteenRuleStrategy(pointValues);
//		hand.insert(Suit.CLUBS, Rank.JACK, false);
//		hand.insert(Suit.HEARTS, Rank.JACK, false);
//		hand.insert(Suit.CLUBS, Rank.TWO, false);
//		hand.insert(Suit.SPADES, Rank.TWO, false);
//		hand.insert(Suit.HEARTS, Rank.TWO, false);
//
//		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
//		// should have zero combinations\
////		assertEquals("FLUSH 0", event.toString());
//		assertEquals("Fifteen1\nFifteen2\n", event.toString());
//	}
}