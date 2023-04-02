package scoring.ruleStrategies;

import java.util.ArrayList;

import events.eventObjects.GameEvent.EventType;
import scoring.RuleInfo;
import scoring.Ruleset;
import scoring.ruleStrategies.base.CombinationRuleStrategy;
import scoring.ruleStrategies.base.FlushRuleStrategy;
import scoring.ruleStrategies.base.JackRuleStrategy;
import scoring.ruleStrategies.base.LastCardRuleStrategy;
import scoring.ruleStrategies.base.PairRuleStrategy;
import scoring.ruleStrategies.base.RunRuleStrategy;
import scoring.ruleStrategies.base.TotalPointsRuleStrategy;

public class RuleStrategyFactory {

	private Ruleset ruleSet;

	public RuleStrategyFactory(Ruleset ruleSet) {
		this.ruleSet = ruleSet;
	}

	public IRuleStrategy getStrategy(EventType eventType) {

		CompositeRuleStrategy composite = new CompositeRuleStrategy();
		ArrayList<RuleInfo> enabled = null;

		switch (eventType) {
		case STARTER:

			// get ruleset
			enabled = ruleSet.getEnabled(EventType.STARTER);

			// for each rule in the enable rule arrayList create a strategy and add to
			// composite
			for (RuleInfo ruleInfo : enabled) {
				composite.add(createMappedStrategy(ruleInfo));
			}

			return (IRuleStrategy) composite;
		case PLAY:
			enabled = ruleSet.getEnabled(EventType.PLAY);

			for (RuleInfo ruleInfo : enabled) {
				composite.add(createMappedStrategy(ruleInfo));
			}

			return (IRuleStrategy) composite;
		case SHOW:
			enabled = ruleSet.getEnabled(EventType.SHOW);

			for (RuleInfo ruleInfo : enabled) {
				composite.add(createMappedStrategy(ruleInfo));
			}

			return (IRuleStrategy) composite;
		default:
			return null;
		}
	}

	private IRuleStrategy createMappedStrategy(RuleInfo ruleInfo) {

		switch (ruleInfo.getRuleType()) {
		case RUN:
			return (IRuleStrategy) new RunRuleStrategy(ruleInfo.getPointValues());
		case PAIR:
			return (IRuleStrategy) new PairRuleStrategy(ruleInfo.getPointValues());
		case JACK:
			return (IRuleStrategy) new JackRuleStrategy(ruleInfo.getPointValues());
		case FLUSH:
			return (IRuleStrategy) new FlushRuleStrategy(ruleInfo.getPointValues());
		case COMBINATION:
			return (IRuleStrategy) new CombinationRuleStrategy(ruleInfo.getPointValues());
		case LASTCARD:
			return (IRuleStrategy) new LastCardRuleStrategy(ruleInfo.getPointValues());
		case TOTALPOINTS:
			return (IRuleStrategy) new TotalPointsRuleStrategy(ruleInfo.getPointValues());
		default:
			return null;
		}
	}
}
