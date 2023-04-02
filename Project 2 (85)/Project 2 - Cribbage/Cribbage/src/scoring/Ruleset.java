package scoring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import events.eventObjects.GameEvent.EventType;
import events.eventObjects.ScoreEvent.RuleType;

public class Ruleset {
	private ArrayList<RuleInfo> enabledStarter = new ArrayList<RuleInfo>();
	private ArrayList<RuleInfo> enabledPlay = new ArrayList<RuleInfo>();
	private ArrayList<RuleInfo> enabledShow = new ArrayList<RuleInfo>();

	public void parseConfig() {

		try (BufferedReader br = new BufferedReader(new FileReader("./ruleConfig.txt"))) {

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] sections = line.split(" ");

				String points = sections[0];
				String rule = sections[1];
				String events = sections[2];

				RuleType ruleType = RuleType.valueOf(rule);

				ArrayList<Integer> pointsArr = new ArrayList<Integer>();
				for (String s : points.split(",")) {
					pointsArr.add(Integer.parseInt(s));
				}

				RuleInfo ruleInfo = new RuleInfo(ruleType, pointsArr);

				ArrayList<String> eventsArr = new ArrayList<String>();
				for (String s : events.split(",")) {
					eventsArr.add(s);
				}

				for (String event : eventsArr) {
					if (event.equals("STARTER")) {
						this.enabledStarter.add(ruleInfo);

					}
					if (event.equals("PLAY")) {
						this.enabledPlay.add(ruleInfo);

					}
					if (event.equals("SHOW")) {
						this.enabledShow.add(ruleInfo);

					}
				}

			}

		} catch (Exception e) {
			// If no ruleConfig.txt file exists, set up Cribbage with default rule properties
			
			// Default properties
//			2 COMBINATION SHOW
//			1 TOTALPOINTS PLAY
//			1 LASTCARD PLAY
//			1 RUN SHOW,PLAY
//			2,6,12 PAIR SHOW,PLAY
//			1 FLUSH SHOW
//			1 JACK STARTER,SHOW
			
			RuleInfo ruleInfo;
			
			ruleInfo = new RuleInfo(RuleType.COMBINATION, new ArrayList<Integer>(List.of(2)));
			enabledShow.add(ruleInfo);
			
			ruleInfo = new RuleInfo(RuleType.TOTALPOINTS, new ArrayList<Integer>(List.of(1)));
			enabledPlay.add(ruleInfo);
			
			ruleInfo = new RuleInfo(RuleType.LASTCARD, new ArrayList<Integer>(List.of(1)));
			enabledPlay.add(ruleInfo);
			
			ruleInfo = new RuleInfo(RuleType.RUN, new ArrayList<Integer>(List.of(1)));
			enabledPlay.add(ruleInfo);
			enabledShow.add(ruleInfo);
			
			ruleInfo = new RuleInfo(RuleType.PAIR, new ArrayList<Integer>(List.of(2, 6, 12)));
			enabledPlay.add(ruleInfo);
			enabledShow.add(ruleInfo);
			
			ruleInfo = new RuleInfo(RuleType.FLUSH, new ArrayList<Integer>(List.of(1)));
			enabledShow.add(ruleInfo);
			
			ruleInfo = new RuleInfo(RuleType.LASTCARD, new ArrayList<Integer>(List.of(1)));
			enabledStarter.add(ruleInfo);
			enabledShow.add(ruleInfo);
		}
	}

	public ArrayList<RuleInfo> getEnabled(EventType event) {
		if (event == EventType.STARTER) {
			return this.enabledStarter;
		}
		if (event == EventType.PLAY) {
			return this.enabledPlay;
		}
		if (event == EventType.SHOW) {
			return this.enabledShow;
		}
		return null;
	}

}
