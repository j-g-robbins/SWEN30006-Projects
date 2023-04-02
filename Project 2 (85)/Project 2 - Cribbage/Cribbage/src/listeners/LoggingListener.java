package listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import events.eventObjects.GameEvent;

public class LoggingListener implements CribbageEventListener {

//	public LoggingListener() {
//		try (PrintWriter pw = new PrintWriter(new FileWriter("cribbage.log", false))) {
//			pw.printf("");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

	// Write the content of the GameEvent to the log
	public void update(GameEvent data) {

		try (PrintWriter pw = new PrintWriter(new FileWriter("cribbage.log", true))) {

			pw.printf("%s", data.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
