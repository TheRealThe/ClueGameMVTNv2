package tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.ComputerPlayer;

public class gameActionTests {
	// NOTE: I made Board static because I only want to set it up one 
	// time (using @BeforeClass), no need to do setup before each test.
	private static Board board;

	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueBoard.csv", "ClueLegend.txt");
		// set the file names for the player and weapon files
		board.setConfigFiles2("Players.txt", "weapons.txt");
		// Initialize will load BOTH config files 
		board.loadConfigFile();
	}
	
	// This will check that if the comp player is not near a door then chose randomlly
	@Test
	public void selectRandomTarget() {
		ComputerPlayer a = new ComputerPlayer("test", 24, 0, Color.gray);
		board.calcTargets(24, 0, 2);
		
		boolean loc_22_0 = false;
		boolean loc_23_1 = false;
		boolean loc_24_2 = false;
		
		for (int i = 0; i < 100; ++i) {
			BoardCell selected = a.pickLocation(board.getTargets());
			
			if (selected == board.getCellAt(22, 0)) {
				loc_22_0 = true;
			}
			else if (selected == board.getCellAt(23, 1)) {
				loc_23_1 = true;
			}
			else if (selected == board.getCellAt(24, 2)) {
				loc_24_2 = true;
			}
			else {
				fail("Its getting a wrong value");
			}
		}
		
		assertTrue(loc_22_0);
		assertTrue(loc_23_1);
		assertTrue(loc_24_2);
	}
	
	@Test
	public void selectRoomTarget() {
		
	}
	
	@Test
	public void checkAccusation() {
		
	}
	
	@Test
	public void disproveSuggestion() {
		fail("Not yet implemented");
	}
	
	@Test
	public void handleSuggestion() {
		fail("Not yet implemented");
	}
	
	@Test
	public void createSuggestion() {
		fail("Not yet implemented");
	}

}
