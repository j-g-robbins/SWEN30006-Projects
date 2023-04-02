package scoring.ruleStrategies.base;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import events.EventFactory;
import events.eventObjects.CompositeScoreEvent;
import events.eventObjects.GameEvent;
import events.eventObjects.IReportScores;
import events.eventObjects.ScoreEvent;
import scoring.ruleStrategies.IRuleStrategy;

public class CombinationRuleStrategy implements IRuleStrategy {
	private static final int TOTAL_SUM = 15;
	private ArrayList<Integer> pointValues;

	public CombinationRuleStrategy(ArrayList<Integer> pointValues) {
		this.pointValues = pointValues;
	}

	public GameEvent validateRule(Segment s) {

		// copy starter into hand
		Hand hand = IRuleStrategy.cloneHandInsertStarter(s.getSegment(), s.getStarter());

		// check for combinations that total 15
		ArrayList<Hand> combinations = combinationSum(hand, TOTAL_SUM);
		// if total of 15 create scoreEvent

		if (combinations.size() > 0) {
			GameEvent newCompositeEvent = EventFactory.getInstance().createCompositeEvent();
			for (Hand combo : combinations) {
				GameEvent newEvent = EventFactory.getInstance().createScoreEvent(combo, s.getLastPlayer(),
						calcPointValue(), ScoreEvent.RuleType.COMBINATION);
				((CompositeScoreEvent) newCompositeEvent).add((IReportScores) newEvent);
			}
			return newCompositeEvent;
		}

		return null;
	}

	// Combinational sum algorithm sourced from
	// https://www.geeksforgeeks.org/combinational-sum/
	public static ArrayList<Hand> combinationSum(Hand hand, int sum) {
		ArrayList<Hand> ans = new ArrayList<Hand>();
		ArrayList<Card> temp = new ArrayList<Card>();

		// sort the hand
		hand.sort(Hand.SortType.RANKPRIORITY, false);
		ArrayList<Card> arr = hand.getCardList();

		// recursively solve
		findNumbers(ans, arr, sum, 0, temp);

		return ans;
	}

	public static void findNumbers(ArrayList<Hand> ans, ArrayList<Card> arr, int sum, int index, ArrayList<Card> temp) {
		if (sum == 0) {
			// Adding deep copy of list to ans
			Hand hand = new Hand(Cribbage.getDeck());
			temp.forEach((card) -> hand.insert(card.clone(), false));
			ans.add(hand);
			return;
		}

		for (int i = index; i < arr.size(); i++) {
			// checking that sum does not become negative
			if ((sum - Cribbage.Rank.valueOf(arr.get(i).getRank().toString()).value) >= 0) {
				// adding element which can contribute to
				// sum
				if (temp.size() > 0 && temp.contains(arr.get(i))) {
					continue;
				}
				temp.add(arr.get(i));
				findNumbers(ans, arr, sum - Cribbage.Rank.valueOf(arr.get(i).getRank().toString()).value, i, temp);

				temp.remove(arr.get(i));
			}
		}
	}

	private int calcPointValue() {
		return pointValues.get(0);
	}

}
