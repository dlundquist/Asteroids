import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class HighScores {
	private static final String HIGH_SCORE_FILE = "data/scores.txt";
	private static final int MAX_SCORES = 10;

	private ArrayList<HighScore> score_list;


	/* Open HIGH_SCORE_FILE and read the scores, names into an array */
	public HighScores() {
		String line;
		String[] parts;
		int score;

		score_list = new ArrayList<HighScore>(MAX_SCORES);

		try {
			BufferedReader file = new BufferedReader(new FileReader(HIGH_SCORE_FILE));

			while((line = file.readLine()) != null) {
				parts = line.split("\t", 2);

				score = Integer.parseInt(parts[0]);

				score_list.add(new HighScore(score, parts[1]));
			}

			file.close();
		} catch (IOException e) {
			System.err.println("No high score file found, creating new high scores...");
			//TODO prepopulate high score with dummy scores
		}
	}

	public boolean isNewHighScore(int score) {
		for(int i = score_list.size() - 1; i >= 0; i--) {
			if (score < score_list.get(i).score) {
				return false;
			}
		}

		return true;
	}

	public void displayScoreDialog() {
		new HighScoreDialog(this);
	}

	/*
	 * Open HIGH_SCORE_FILE for writing (not appending), and write the high scores to the file in order 
	 */
	public void writeScoreFile() {
		try{
			FileWriter write = new FileWriter(HIGH_SCORE_FILE);
			PrintWriter out = new PrintWriter(write);

			for(int i = 0; i < score_list.size() && i < 10; i++){
				out.println(score_list.get(i));
			}
			out.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * This prints out the current high scores, it's mostly for debugging
	 */
	public void printScores() {
		System.out.println("High Scores:");
		for(int i=0; i<score_list.size();i++){
			System.out.println(score_list.get(i));
		}
	}
	
	
	public void add(String name, int score) {
		// TODO put the new score the correct spot in the score_list
		score_list.add(new HighScore(score, name));
	}

	public void newScore(int score) {
		if (this.isNewHighScore(score)) {
			String name =  JOptionPane.showInputDialog("New high score, enter your name");

			this.add(name, score);
			// Do we want to write the high score file here?
		}
	}

	// If you have trouble w/ HighScoresDialog - change this to public static class ...
	private static class HighScore {
		public String name;
		public int score;

		public HighScore(int score, String name) {
			this.name = name;
			this.score = score;
		}

		public String toString() {
			return score + "\t" + name;
		}
	}
}
