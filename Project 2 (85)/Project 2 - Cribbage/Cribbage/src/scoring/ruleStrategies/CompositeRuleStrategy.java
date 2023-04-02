package scoring.ruleStrategies;

import java.util.ArrayList;

import cribbage.Cribbage.Segment;
import events.eventObjects.CompositeScoreEvent;
import events.eventObjects.GameEvent;
import events.eventObjects.IReportScores;

public class CompositeRuleStrategy implements IRuleStrategy {

	private ArrayList<IRuleStrategy> strategies;

	public CompositeRuleStrategy() {
		strategies = new ArrayList<>();
	}

	@Override
	public GameEvent validateRule(Segment s) {
		// create composite event
		CompositeScoreEvent compositeEvent = new CompositeScoreEvent(null, null, null);

		// validate all rules and add events they generate to composite
		for (IRuleStrategy rule : strategies) {
			GameEvent newEvent = rule.validateRule(s);
			if (newEvent != null) {
				compositeEvent.add((IReportScores) newEvent);
			}
		}

		// return if events were generated
		if (compositeEvent.getNumChildren() > 0) {
			return (GameEvent) compositeEvent;
		}

		// no events
		return null;
	}

	public void add(IRuleStrategy strategy) {
		strategies.add(strategy);
	}

	public int getNumRules() {
		return strategies.size();
	}

}
