package tests.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import events.eventObjects.GameEvent.EventType;
import scoring.Ruleset;

class RulesetTest {

	@Test
	final void testStarter() {
		Ruleset ruleSet = new Ruleset();
		ruleSet.parseConfig();

		assertEquals(1, ruleSet.getEnabled(EventType.STARTER).size());
	}

	@Test
	final void testPlay() {
		Ruleset ruleSet = new Ruleset();
		ruleSet.parseConfig();

		assertEquals(3, ruleSet.getEnabled(EventType.PLAY).size());
	}

	@Test
	final void testShow() {
		Ruleset ruleSet = new Ruleset();
		ruleSet.parseConfig();

		assertEquals(4, ruleSet.getEnabled(EventType.SHOW).size());
	}

}
