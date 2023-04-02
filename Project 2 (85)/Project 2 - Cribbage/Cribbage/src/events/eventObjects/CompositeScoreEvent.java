package events.eventObjects;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;

public class CompositeScoreEvent extends GameEvent implements IReportScores {

	private ArrayList<IReportScores> scoreEvents;

	public CompositeScoreEvent(EventType eventType, PlayerType playerType, Hand hand) {
		super(eventType, playerType, hand);
		scoreEvents = new ArrayList<>();

	}

	public int getScore() {
		int score = 0;
		for (IReportScores event : scoreEvents) {
			score += event.getScore();
		}
		return score;
	}

	public void add(IReportScores event) {
		scoreEvents.add(event);
	}

	public void remove(IReportScores event) {
		scoreEvents.remove(event);
	}

	public String toString() {
		String output = "";
		for (IReportScores event : scoreEvents) {
			output += event.toString();
		}
		return output;
	}

	public int getNumChildren() {
		return scoreEvents.size();
	}
}
