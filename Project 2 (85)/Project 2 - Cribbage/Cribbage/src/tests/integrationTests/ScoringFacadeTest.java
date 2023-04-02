package tests.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Rank;
import cribbage.Cribbage.Segment;
import cribbage.Cribbage.Suit;
import events.eventObjects.GameEvent;
import scoring.ScoringFacade;
import scoring.ruleStrategies.CompositeRuleStrategy;

class ScoringFacadeTest {

	@Test
	final void testStartCreate() {

		ScoringFacade scoreFacade = new ScoringFacade();

		CompositeRuleStrategy start = (CompositeRuleStrategy) scoreFacade.getStarterRuleStrategy();

		assertEquals(1, start.getNumRules());
	}

	@Test
	final void testPlayCreate() {

		ScoringFacade scoreFacade = new ScoringFacade();

		CompositeRuleStrategy start = (CompositeRuleStrategy) scoreFacade.getPlayRuleStrategy();

		assertEquals(4, start.getNumRules());
	}

	@Test
	final void testShowCreate() {

		ScoringFacade scoreFacade = new ScoringFacade();

		CompositeRuleStrategy start = (CompositeRuleStrategy) scoreFacade.getShowRuleStrategy();

		assertEquals(5, start.getNumRules());
	}

	@Test
	final void testStartUse() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		ArrayList<Integer> pointValues = new ArrayList<Integer>();
		pointValues.add(1);
		hand.insert(Suit.CLUBS, Rank.QUEEN, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.TWO, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);
		start.insert(Suit.CLUBS, Rank.JACK, false);

		Segment event = cribbage.createSegment(start, start, false, false);

		ScoringFacade scoreFacade = new ScoringFacade();

		CompositeRuleStrategy startStrategy = (CompositeRuleStrategy) scoreFacade.getStarterRuleStrategy();

		GameEvent gameEvent = startStrategy.validateRule(event);

		System.out.println("Starter");
		System.out.println(gameEvent.toString());

		assertEquals(1, 1);
	}

	@Test
	final void testPlayUse() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		hand.insert(Suit.HEARTS, Rank.TWO, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);

		Segment event = cribbage.createSegment(hand, null, false, true);

		ScoringFacade scoreFacade = new ScoringFacade();

		CompositeRuleStrategy startStrategy = (CompositeRuleStrategy) scoreFacade.getPlayRuleStrategy();

		GameEvent gameEvent = startStrategy.validateRule(event);

		System.out.println("Play");
		System.out.println(gameEvent.toString());

		assertEquals(1, 1);
	}

	@Test
	final void testShowUse() {
		Cribbage cribbage = new Cribbage();
		Deck deck = Cribbage.getDeck();
		Hand hand = new Hand(deck);
		Hand start = new Hand(deck);
		hand.insert(Suit.CLUBS, Rank.JACK, false);
		hand.insert(Suit.CLUBS, Rank.TWO, false);
		hand.insert(Suit.SPADES, Rank.FOUR, false);
		hand.insert(Suit.HEARTS, Rank.THREE, false);
		start.insert(Suit.CLUBS, Rank.JACK, false);

		Segment event = cribbage.createSegment(hand, start, true, false);

		ScoringFacade scoreFacade = new ScoringFacade();

		CompositeRuleStrategy startStrategy = (CompositeRuleStrategy) scoreFacade.getShowRuleStrategy();

		GameEvent gameEvent = startStrategy.validateRule(event);

		System.out.println("Show");
		System.out.println(gameEvent.toString());

		assertEquals(1, 1);
	}
}
