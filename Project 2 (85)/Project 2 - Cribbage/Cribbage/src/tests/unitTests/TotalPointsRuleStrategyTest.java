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
import scoring.ruleStrategies.base.TotalPointsRuleStrategy;

class TotalPointsRuleStrategyTest {

	@Test
	final void testNonePlay() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new TotalPointsRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, true));
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
		IRuleStrategy strat = new TotalPointsRuleStrategy(pointValues);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals(null, event);
	}

	@Test
	final void testFifteen() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new TotalPointsRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.HEARTS, Rank.FIVE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("FIFTEEN1\n", event.toString());
	}

	@Test
	final void testThirtyone() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		IRuleStrategy strat = new TotalPointsRuleStrategy(pointValues);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.HEARTS, Rank.KING, false);
		hand.insert(Suit.HEARTS, Rank.JACK, false);
		hand.insert(Suit.HEARTS, Rank.ACE, false);

		GameEvent event = strat.validateRule(cribbage.createSegment(hand, null, false, true));
		// should have zero combinations\
//		assertEquals("FLUSH 0", event.toString());
		assertEquals("THIRTYONE2\n", event.toString());
	}
}