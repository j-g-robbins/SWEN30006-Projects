package cribbage;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

// Cribbage.java
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import events.EventFactory;
import events.EventManager;
import events.eventObjects.GameEvent;
import events.eventObjects.GameEvent.EventType;
import events.eventObjects.IReportScores;
import listeners.CribbageEventListener;
import listeners.LoggingListener;
import scoring.ScoringFacade;

public class Cribbage extends CardGame {

	public static Cribbage cribbage; // Provide access to singleton

	public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES
	}

	public enum Rank {
		// Order of cards is tied to card images
		ACE(1, 1), KING(13, 10), QUEEN(12, 10), JACK(11, 10), TEN(10, 10), NINE(9, 9), EIGHT(8, 8), SEVEN(7, 7),
		SIX(6, 6), FIVE(5, 5), FOUR(4, 4), THREE(3, 3), TWO(2, 2);

		public final int order;
		public final int value;

		Rank(int order, int value) {
			this.order = order;
			this.value = value;
		}
	}

	static int cardValue(Card c) {
		return ((Cribbage.Rank) c.getRank()).value;
	}

	/*
	 * Canonical String representations of Suit, Rank, Card, and Hand
	 */
	public static String canonical(Suit s) {
		return s.toString().substring(0, 1);
	}

	public static String canonical(Rank r) {
		switch (r) {
		case ACE:
		case KING:
		case QUEEN:
		case JACK:
		case TEN:
			return r.toString().substring(0, 1);
		default:
			return String.valueOf(r.value);
		}
	}

	public static String canonical(Card c) {
		return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit());
	}

	public static String canonical(Hand h) {
		Hand h1 = new Hand(deck); // Clone to sort without changing the original hand
		for (Card C : h.getCardList())
			h1.insert(C.getSuit(), C.getRank(), false);
		h1.sort(Hand.SortType.POINTPRIORITY, false);
		return "[" + h1.getCardList().stream().map(Cribbage::canonical).collect(Collectors.joining(",")) + "]";
	}

	static Random random;

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	static boolean ANIMATE;

	void transfer(Card c, Hand h) {
		if (ANIMATE) {
			c.transfer(h, true);
		} else {
			c.removeFromHand(true);
			h.insert(c, true);
		}
	}

	private void dealingOut(Hand pack, Hand[] hands) {
		for (int i = 0; i < nStartCards; i++) {
			for (int j = 0; j < nPlayers; j++) {
				Card dealt = randomCard(pack);
				dealt.setVerso(false); // Show the face
				transfer(dealt, hands[j]);
			}
		}
	}

	static int SEED;

	public static Card randomCard(Hand hand) {
		int x = random.nextInt(hand.getNumberOfCards());
		return hand.get(x);
	}

	private static final int NUM_PLAYERS = 2;
	private final String version = "0.1";
	static public final int nPlayers = 2;
	public final int nStartCards = 6;
	public final int nDiscards = 2;
	private final int handWidth = 400;
	private final int cribWidth = 150;
	private final int segmentWidth = 180;
	private final static Deck deck = new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues());
	private final Location[] handLocations = { new Location(360, 75), new Location(360, 625) };
	private final Location[] scoreLocations = { new Location(590, 25), new Location(590, 675) };
	private final Location[] segmentLocations = { // need at most three as 3x31=93 > 2x4x10=80
			new Location(150, 350), new Location(400, 350), new Location(650, 350) };
	private final Location starterLocation = new Location(50, 625);
	private final Location cribLocation = new Location(700, 625);
	private final Location seedLocation = new Location(5, 25);
	// private final TargetArea cribTarget = new TargetArea(cribLocation,
	// CardOrientation.NORTH, 1, true);
	private final Actor[] scoreActors = { null, null }; // , null, null };
	private final Location textLocation = new Location(350, 450);
	private final Hand[] hands = new Hand[nPlayers];
	private final Hand[] intialHands = new Hand[nPlayers];
	private final ScoringFacade scoreSystem = new ScoringFacade();
	private final EventManager eventManager = new EventManager();
	private final EventFactory eventFactory = new EventFactory();
	private Hand starter;
	private Hand crib;

	public static void setStatus(String string) {
		cribbage.setStatusText(string);
	}

	public static Deck getDeck() {
		return deck;
	}

	static private final IPlayer[] players = new IPlayer[nPlayers];
	private final int[] scores = new int[nPlayers];

	final Font normalFont = new Font("Serif", Font.BOLD, 24);
	final Font bigFont = new Font("Serif", Font.BOLD, 36);

	private void initScore() {
		for (int i = 0; i < nPlayers; i++) {
			scores[i] = 0;
			scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
			addActor(scoreActors[i], scoreLocations[i]);
		}
	}

	private void updateScore(int player) {
		removeActor(scoreActors[player]);
		scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[player], scoreLocations[player]);
	}

	private void deal(Hand pack, Hand[] hands) {
		for (int i = 0; i < nPlayers; i++) {
			hands[i] = new Hand(deck);
			// players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
			players[i].setId(i);
			players[i].startSegment(deck, hands[i]);
		}
		RowLayout[] layouts = new RowLayout[nPlayers];
		for (int i = 0; i < nPlayers; i++) {
			layouts[i] = new RowLayout(handLocations[i], handWidth);
			layouts[i].setRotationAngle(0);
			// layouts[i].setStepDelay(10);
			hands[i].setView(this, layouts[i]);
			hands[i].draw();
		}
		layouts[0].setStepDelay(0);

		dealingOut(pack, hands);
		for (int i = 0; i < nPlayers; i++) {
			hands[i].sort(Hand.SortType.POINTPRIORITY, true);
		}
		// log dealt cards
		for (int i = 0; i < nPlayers; i++) {
			GameEvent gameEvent = eventFactory.createEvent(hands[i], i, EventType.DEAL, 0);
			eventManager.notify(gameEvent);
		}
		layouts[0].setStepDelay(0);
	}

	private void discardToCrib() {
		crib = new Hand(deck);
		RowLayout layout = new RowLayout(cribLocation, cribWidth);
		layout.setRotationAngle(0);
		crib.setView(this, layout);
		// crib.setTargetArea(cribTarget);
		crib.draw();

		for (int i = 0; i < nPlayers; i++) {
			IPlayer player = players[i];
			Hand playerDiscards = new Hand(deck);

			for (int j = 0; j < nDiscards; j++) {
				Card discard = player.discard();
				Card discardClone = discard.clone();
				playerDiscards.insert(discardClone, false);
				transfer(discard, crib);
			}
			// log cards discarded to the crib
			GameEvent discardEvent = eventFactory.createEvent(playerDiscards, i, EventType.DISCARD, 0);
			eventManager.notify(discardEvent);

			crib.sort(Hand.SortType.POINTPRIORITY, true);
		}
	}

	private void starter(Hand pack) {
		starter = new Hand(deck); // if starter is a Jack, the dealer gets 2 points
		RowLayout layout = new RowLayout(starterLocation, 0);
		layout.setRotationAngle(0);
		starter.setView(this, layout);
		starter.draw();
		Card dealt = randomCard(pack);
		dealt.setVerso(false);
		transfer(dealt, starter);

		// log the starter card
		GameEvent starterEvent = eventFactory.createEvent(starter, -1, EventType.STARTER, 0);
		eventManager.notify(starterEvent);

		// validate stater
		int playerNum = 1; // player 1 is dealer
		Segment s = new Segment(starter, starter, playerNum);
		GameEvent newEvent = scoreSystem.score(EventType.STARTER, s);

		if (newEvent != null) {
			int scoreDelta = ((IReportScores) newEvent).getScore();
			scores[s.lastPlayer] += scoreDelta;

			updateScore(s.lastPlayer);
			eventManager.notify(newEvent);
		}
	}

	int total(Hand hand) {
		int total = 0;
		for (Card c : hand.getCardList())
			total += cardValue(c);
		return total;
	}

	public class Segment {
		Hand segment;
		private Hand starter;
		boolean go;
		int lastPlayer;
		boolean newSegment;
		boolean isPlay = false;

		// use during play
		public Segment() {
		}

		// use during start and show
		public Segment(Hand segment, Hand starter, int lastPlayer) {
			super();
			this.segment = segment;
			this.starter = starter;
			this.lastPlayer = lastPlayer;
		}

		public void reset(final List<Hand> segments) {
			segment = new Hand(deck);
			segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
			segment.draw();
			isPlay = true;
			go = false; // No-one has said "go" yet
			lastPlayer = -1; // No-one has played a card yet in this segment
			newSegment = false; // Not ready for new segment yet
		}

		public Hand getSegment() {
			return segment;
		}

		public int getLastPlayer() {
			if (lastPlayer > -1) {
				return lastPlayer;
			} else {
				return -1;
			}
		}

		public boolean isNewSegment() {
			return newSegment;
		}

		public Hand getStarter() {
			return starter;
		}

		public boolean isPlay() {
			return isPlay;
		}

	}

	private void play() {
		// clone hands for scoring the show
		intialHands[0] = cloneHand(hands[0]);
		intialHands[1] = cloneHand(hands[1]);

		final int thirtyone = 31;
		List<Hand> segments = new ArrayList<>();
		int currentPlayer = 0; // Player 1 is dealer
		Segment s = new Segment();
		s.reset(segments);
		while (!(players[0].emptyHand() && players[1].emptyHand())) {
			// System.out.println("segments.size() = " + segments.size());
			Card nextCard = players[currentPlayer].lay(thirtyone - total(s.segment));
			if (nextCard == null) {
				if (s.go) {
					// Another "go" after previous one with no intervening cards
					// lastPlayer gets 1 point for a "go"
					s.newSegment = true;
				} else {
					// currentPlayer says "go"
					s.go = true;
				}
				currentPlayer = (currentPlayer + 1) % 2;
			} else {
				s.lastPlayer = currentPlayer; // last Player to play a card in this segment
				transfer(nextCard, s.segment);

				// log the next card to be played
				Hand tmp = new Hand(deck);
				tmp.insert(s.getSegment().getLast().clone(), false);
				GameEvent playEvent = eventFactory.createEvent(tmp, s.lastPlayer, EventType.PLAY, total(s.segment));
				eventManager.notify(playEvent);

				if (total(s.segment) == thirtyone) {
					// lastPlayer gets 2 points for a 31
					s.newSegment = true;
					currentPlayer = (currentPlayer + 1) % 2;
				} else {
					// if total(segment) == 15, lastPlayer gets 2 points for a 15
					if (!s.go) { // if it is "go" then same player gets another turn
						currentPlayer = (currentPlayer + 1) % 2;
					}
				}
			}
			// set s.newSegment true if both players hands are now empty
			if (players[0].emptyHand() && players[1].emptyHand()) {
				s.newSegment = true;
			}

			// validateRules during play
			GameEvent newEvent = scoreSystem.score(EventType.PLAY, s);
			if (newEvent != null) {
				int scoreDelta = ((IReportScores) newEvent).getScore();
				scores[s.lastPlayer] += scoreDelta;

				updateScore(s.lastPlayer);
				eventManager.notify(newEvent);
			}

			if (s.newSegment) {
				segments.add(s.segment);
				s.reset(segments);
			}
		}
	}

	void showHandsCrib() {
		// validate show for player 0 and 1
		for (int playerNum = 0; playerNum < NUM_PLAYERS; playerNum++) {
			// log Status event
			Hand clone = cloneHand(intialHands[playerNum]);
			clone.insert(starter.getFirst().clone(), false);
			GameEvent showEvent = eventFactory.createEvent(clone, playerNum, EventType.SHOW, 0);
			eventManager.notify(showEvent);

			// Score the show
			Segment s = new Segment(intialHands[playerNum], starter, playerNum);
			GameEvent newEvent = scoreSystem.score(EventType.SHOW, s);

			if (newEvent != null) {
				int scoreDelta = ((IReportScores) newEvent).getScore();
				scores[s.lastPlayer] += scoreDelta;

				updateScore(s.lastPlayer);
				eventManager.notify(newEvent);
			}
		}

		int playerNum = 1;
		// log Status event
		Hand clone = cloneHand(crib);
		clone.insert(starter.getFirst().clone(), false);
		GameEvent showEvent = eventFactory.createEvent(clone, playerNum, EventType.SHOW, 0);
		eventManager.notify(showEvent);

		// score crib (for dealer)
		Segment s = new Segment(crib, starter, playerNum);
		GameEvent newEvent = scoreSystem.score(EventType.SHOW, s);

		if (newEvent != null) {
			int scoreDelta = ((IReportScores) newEvent).getScore();
			scores[s.lastPlayer] += scoreDelta;

			updateScore(s.lastPlayer);
			eventManager.notify(newEvent);
		}
	}

	public Cribbage() {
		super(850, 700, 30);
		cribbage = this;
		setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");
		initScore();
		eventManager.addListener((CribbageEventListener) new LoggingListener());

		// comment out below while testing strategies
		Hand pack = deck.toHand(false);
		RowLayout layout = new RowLayout(starterLocation, 0);
		layout.setRotationAngle(0);
		pack.setView(this, layout);
		pack.setVerso(true);
		pack.draw();
		addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);

		/* Play the round */
		deal(pack, hands);
		discardToCrib();
		starter(pack);
		play();
		showHandsCrib();

		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText("Game over.");
		refresh();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		/* Handle Properties */
		// System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Properties cribbageProperties = new Properties();
		// Default properties

		cribbageProperties.setProperty("Animate", "true");
		cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
		cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

		// Read properties
		try (FileReader inStream = new FileReader("cribbage.properties")) {
			cribbageProperties.load(inStream);
		}

		// Control Graphics
		ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

		// Control Randomisation
		/* Read the first argument and save it as a seed if it exists */
		if (args.length > 0) { // Use arg seed - overrides property
			SEED = Integer.parseInt(args[0]);
		} else { // No arg
			String seedProp = cribbageProperties.getProperty("Seed"); // Seed property
			if (seedProp != null) { // Use property seed
				SEED = Integer.parseInt(seedProp);
			} else { // and no property
				SEED = new Random().nextInt(); // so randomise
			}
		}
		random = new Random(SEED);

		// Control Player Types
		Class<?> clazz;
		clazz = Class.forName(cribbageProperties.getProperty("Player0"));
		players[0] = (IPlayer) clazz.getConstructor().newInstance();
		clazz = Class.forName(cribbageProperties.getProperty("Player1"));
		players[1] = (IPlayer) clazz.getConstructor().newInstance();

		// log Cribbage configurations
		try (PrintWriter pw = new PrintWriter(new FileWriter("cribbage.log", false))) {
			pw.printf("seed," + SEED + "\n");
			pw.printf(cribbageProperties.getProperty("Player0") + ",P0\n");
			pw.printf(cribbageProperties.getProperty("Player1") + ",P1\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// End properties

		new Cribbage();
	}

	private Hand cloneHand(Hand hand) {
		Hand cloned = new Hand(Cribbage.getDeck());

		for (Card card : hand.getCardList()) {
			cloned.insert(card.clone(), false);
		}

		return cloned;
	}

}
