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
import scoring.ruleStrategies.base.RunRuleStrategy;

class RunRuleStrategyTest {

	@Test
	final void testNoneShow() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
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
	final void testRun3Show() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("RUN3" + "\n", event.toString());
	}

	@Test
	final void testRun4Show() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("RUN4" + "\n", event.toString());
	}

	@Test
	final void testRun5Show() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.SIX, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, false));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("RUN5" + "\n", event.toString());
	}

	@Test
	final void testNonePlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TEN, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testNoCardsPlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testRun3Play() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("RUN3" + "\n", event.toString());
	}

	@Test
	final void testRun4Play() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("RUN4" + "\n", event.toString());
	}

	@Test
	final void testRun5Play() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.SIX, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("RUN5" + "\n", event.toString());
	}

	@Test
	final void testRun6Play() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.SIX, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);
		hand.insert(Suit.HEARTS, Rank.SEVEN, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("RUN6" + "\n", event.toString());
	}

	@Test
	final void testRun7Play() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.SIX, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);
		hand.insert(Suit.HEARTS, Rank.SEVEN, false);
		hand.insert(Suit.HEARTS, Rank.EIGHT, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("RUN7" + "\n", event.toString());
	}

	@Test
	final void testOutOfOrderPlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new RunRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.SIX, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);
		hand.insert(Suit.HEARTS, Rank.SEVEN, false);
		hand.insert(Suit.HEARTS, Rank.EIGHT, false);
		hand.insert(Suit.CLUBS, Rank.JACK, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, true, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}
}