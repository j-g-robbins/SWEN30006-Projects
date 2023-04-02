package scoring;

import java.util.ArrayList;

import events.eventObjects.ScoreEvent.RuleType;

public class RuleInfo {
	private RuleType ruleType;
	private ArrayList<Integer> pointValue;

	public RuleInfo(RuleType ruleType, ArrayList<Integer> pointValue) {
		this.ruleType = ruleType;
		this.pointValue = pointValue;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public ArrayList<Integer> getPointValues() {
		return pointValue;
	}
}
