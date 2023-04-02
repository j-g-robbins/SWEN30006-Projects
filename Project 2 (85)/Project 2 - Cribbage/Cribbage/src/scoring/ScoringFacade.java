package scoring;

import cribbage.Cribbage.Segment;
import events.eventObjects.GameEvent;
import events.eventObjects.GameEvent.EventType;
import scoring.ruleStrategies.IRuleStrategy;
import scoring.ruleStrategies.RuleStrategyFactory;

public class ScoringFacade {
	private IRuleStrategy starterRuleStrategy;
	private IRuleStrategy playRuleStrategy;
	private IRuleStrategy showRuleStrategy;
	private RuleStrategyFactory strategyFactory;
	private Ruleset ruleSet;

	public ScoringFacade() {
		this.ruleSet = new Ruleset();
		ruleSet.parseConfig();

		this.strategyFactory = new RuleStrategyFactory(ruleSet);

		this.starterRuleStrategy = strategyFactory.getStrategy(EventType.STARTER);
		this.playRuleStrategy = strategyFactory.getStrategy(EventType.PLAY);
		this.showRuleStrategy = strategyFactory.getStrategy(EventType.SHOW);
	}

	public IRuleStrategy getStarterRuleStrategy() {
		return starterRuleStrategy;
	}

	public IRuleStrategy getShowRuleStrategy() {
		return showRuleStrategy;
	}

	public IRuleStrategy getPlayRuleStrategy() {
		return playRuleStrategy;
	}

	public GameEvent score(EventType eventType, Segment segment) {

		GameEvent gameEvent;

		switch (eventType) {
		case STARTER:
			gameEvent = starterRuleStrategy.validateRule(segment);
			break;
		case PLAY:
			gameEvent = playRuleStrategy.validateRule(segment);
			break;
		default:
			gameEvent = showRuleStrategy.validateRule(segment);
			break;
		}
		return gameEvent;
	}

}
