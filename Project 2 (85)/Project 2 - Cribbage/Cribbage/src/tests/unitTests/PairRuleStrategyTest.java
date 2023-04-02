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
import scoring.ruleStrategies.base.PairRuleStrategy;

class PairRuleStrategyTest {

	@Test
	final void testNoneShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testNoCardsShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testPairShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("PAIR1" + "\n", event.toString());
	}

	@Test
	final void testTripShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("PAIR2" + "\n", event.toString());
	}

	@Test
	final void testQuadShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.DIAMONDS, Rank.TWO, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("PAIR3" + "\n", event.toString());
	}

	@Test
	final void testTripAndPairShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.HEARTS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("PAIR1\nPAIR2\n", event.toString());
	}

	@Test
	final void testNoCardsPlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testNonePlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, true));
		// should have zero combinations\
//			assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testOrderPlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//			assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testPairPlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.FOUR, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
//		// should have zero combinations\
////			assertEquals("FLUSH 0", event.toString());
		assertEquals("PAIR1" + "\n", event.toString());
	}

	@Test
	final void testTripPlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//			assertEquals("FLUSH 0", event.toString());
		assertEquals("PAIR2" + "\n", event.toString());
	}

	@Test
	final void testQuadPlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		pointValues.add(2);
		pointValues.add(3);
		IRuleStrategy strat = new PairRuleStrategy(pointValues);
		hand.insert(Suit.DIAMONDS, Rank.TWO, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//			assertEquals("FLUSH 0", event.toString());
		assertEquals("PAIR3" + "\n", event.toString());
	}
}