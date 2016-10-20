package clueGame;

import java.util.*;
import java.io.*;

public class Board {
	private int startCell;
	private int pathLength;

	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	private Map<Character, String> legendMap = new HashMap<Character, String>();
	Set<BoardCell> visited;
	Set<BoardCell> targets;
	private final int MAX_BOARD_SIZE = 100;
	String layoutFile;
	String legendFile;
	String[][] board = new String[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
	private BoardCell[][] grid = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
	int NUM_ROWS;
	int NUM_COLS;
	String[] fullLegend = null;
	
	// This will be our deck of cards, the number is hard coded since we have a set number of things
	Card[] deck = new Card[21];

	// variable used for singleton pattern
	private static Board theInstance = new Board();

	// ctor is private to ensure only one can be created
	private Board() {
	}

	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}

	public Board(int height, int width) {
		super();
		this.grid = new BoardCell[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				grid[i][j] = new BoardCell(i, j, null);

			}
		}
		calcAdjacencies();
	}

	public void calcAdjacencies() {

		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				Set<BoardCell> adjSet = new HashSet<BoardCell>();

				// This checks to see if the one above it is not out of bounds,
				// if adj tiles are walkway or doors.. and it tests if the tile
				// that is being tested is a walk way or a door.
				if (i > 0 && grid[i - 1][j].getWholeValue().equals("W")
						|| i > 0 && grid[i - 1][j].getWholeValue().length() == 2
								&& grid[i - 1][j].getWholeValue().charAt(1) == 'D') {
					if (grid[i][j].getWholeValue().equals("W")) {
						adjSet.add(getCellAt(i - 1, j));
					}
					if (grid[i][j].getWholeValue().length() == 2) {
						if ((grid[i][j].getSecondInitial() == 'U')) {
							adjSet.add(getCellAt(i - 1, j));
						}
					}
				}
				// This checks to see if the one Below it is not out of bounds,
				// if adj tiles are walkway or doors.. and it tests if the tile
				// that is being tested is a walk way or a door.
				if (i < grid.length - 1 && grid[i + 1][j].getWholeValue().equals("W")
						|| i < grid.length - 1 && grid[i + 1][j].getWholeValue().length() == 2
								&& grid[i + 1][j].getWholeValue().charAt(1) == 'U') {
					if (grid[i][j].getWholeValue().equals("W")) {
						adjSet.add(getCellAt(i + 1, j));
					}
					if (grid[i][j].getWholeValue().length() == 2) {
						if ((grid[i][j].getSecondInitial() == 'D')) {
							adjSet.add(getCellAt(i + 1, j));
						}
					}
				}
				// This checks to see if the one left it is not out of bounds,
				// if adj tiles are walkway or doors.. and it tests if the tile
				// that is being tested is a walk way or a door.
				if (j > 0 && grid[i][j - 1].getWholeValue().equals("W")
						|| j > 0 && grid[i][j - 1].getWholeValue().length() == 2
								&& grid[i][j - 1].getWholeValue().charAt(1) == 'R') {
					if (grid[i][j].getWholeValue().equals("W")) {
						adjSet.add(getCellAt(i, j - 1));
					}
					if (grid[i][j].getWholeValue().length() == 2) {
						if ((grid[i][j].getSecondInitial() == 'L')) {
							adjSet.add(getCellAt(i, j - 1));
						}
					}
				}
				// This checks to see if the one right it is not out of bounds,
				// if adj tiles are walkway or doors.. and it tests if the tile
				// that is being tested is a walk way or a door.
				if (j < grid[i].length - 1 && grid[i][j + 1].getWholeValue().equals("W")
						|| j < grid[i].length - 1 && grid[i][j + 1].getWholeValue().length() == 2
								&& grid[i][j + 1].getWholeValue().charAt(1) == 'L') {
					if (grid[i][j].getWholeValue().equals("W")) {
						adjSet.add(getCellAt(i, j + 1));
					}
					if (grid[i][j].getWholeValue().length() == 2) {
						if ((grid[i][j].getSecondInitial() == 'R')) {
							adjSet.add(getCellAt(i, j + 1));
						}
					}
				}
				adjMatrix.put(grid[i][j], adjSet);
			}
		}

	}

	public void calcTargets(BoardCell cell, int pathLength) {
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		visited.add(cell);
		findAllTargets(cell, pathLength);
	}

	//This is the path Algorithm 
	private void findAllTargets(BoardCell cell, int pathLength) {
		for (BoardCell c : this.adjMatrix.get(cell)) {
			if (this.visited.contains(c)) {
				continue;
			}
			if (pathLength == 1 || c.getWholeValue().length() == 2) {
				this.targets.add(c);
			} else {
				this.visited.add(c);
				findAllTargets(c, pathLength - 1);
				this.visited.remove(c);
			}
		}
	}

	//// AutoGenerated might need to be changed
	public void initialize() {
		try {
			loadBoardConfig();
			loadRoomConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loadRoomConfig() throws BadConfigFormatException {
		BufferedReader fileReader = null;

		// This File reader, reads the file and puts it into a map
		final String DELIMITERTWO = ", ";
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(legendFile));

			int location = 0;
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {

				// Get all tokens available in line
				String[] legend = line.split(DELIMITERTWO);
				location++;
				// This grabs all the letters for the keys
				for (int i = 0; i < legend.length; i = i + 3) {
					Character key = legend[i].charAt(0);
					legendMap.put(key, legend[i + 1]);
				}
			}

			checkLegend();
		} catch (Exception e) {
			throw new BadConfigFormatException("Some Exception", e);
		}
		{
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void checkLegend() throws BadConfigFormatException {

		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {

				char letter = grid[i][j].getInitial();

				if (!legendMap.containsKey(letter)) {
					throw new BadConfigFormatException("Does not contain the Legend key: " + letter);
				}
			}
		}

	}
	
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException {
		BufferedReader fileReader = null;
		// Delimiter used in CSV file
		final String DELIMITER = ",";

		try {
			FileReader readerTwo = new FileReader(layoutFile);
			Scanner in = new Scanner(readerTwo);
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(layoutFile));

			// This while loop detects the number of rows.
			int value = 0;
			String word = "";
			while (in.hasNextLine()) {
				word = in.nextLine();
				value++;
			}

			// This grabs the number of columns. and removes Commas so it
			// doesn't count any extras
			for (int i = 0; i < word.length(); i++) {
				word = word.replace(",", "");

			}

			NUM_COLS = word.length();
			NUM_ROWS = value;

			this.grid = new BoardCell[NUM_ROWS][NUM_COLS];

			int i = 0;
			int count = 0;
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] csvFile = line.split(DELIMITER);

				for (int j = 0; j < NUM_COLS; j++) {
					this.grid[i][j] = new BoardCell(i, j, csvFile[j]);
				}
				i++;
				count++;
			}
			calcAdjacencies();
			in.close();
		} catch (Exception e) {
			throw new BadConfigFormatException("error", e);
		}

		try {
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new BadConfigFormatException("Some IOException", e);
		}

	}
	
	// This is were me and the have added code
	// This is were we will load the files to fill our players
	public void loadConfigFiles() {
		
	}
	
	//This will select the answer, 
	//not sure how it works yet but will soon
	public void selectAnswer() {
		
	}
	
	//This will handle suggestions
	public Card handleSuggestion() {
		return null;
	}
	
	// this will check the accusation to see if it is correct
	public boolean checkAccusation(Solution accusation) {
		return false;
	}

	// AutoGenerated might need to be changed
	public Map<Character, String> getLegend() {
		return legendMap;
	}

	// AutoGenerated might need to be changed
	public int getNumRows() {
		int rows = NUM_ROWS;
		return rows;
	}

	// AutoGenerated might need to be changed
	public int getNumColumns() {

		int cols = NUM_COLS;

		return cols;
	}

	public Set<BoardCell> getTargets() {
		return this.targets;
	}

	public Set<BoardCell> getAdjList(BoardCell cell) {
		return adjMatrix.get(cell);
	}

	public BoardCell getCellAt(int height, int width) {
		return grid[height][width];
	}

	//// AutoGenerated might need to be changed
	public void setConfigFiles(String string, String string2) {
		layoutFile = string;
		legendFile = string2;
	}

	public Set<BoardCell> getAdjList(int one, int two) {

		return adjMatrix.get(grid[one][two]);
	}

	// I changed the return type to Int. It was a Void.
	public void calcTargets(int i, int j, int k) {
		// Called CalcTargets
		calcTargets(grid[i][j], k);
	}

}
