package experiment;

import java.util.*;

public class IntBoard {
	private int startCell;
	private int pathLength;
	private BoardCell[][] grid;
	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	Set<BoardCell> visited;
	Set<BoardCell> targets;
	
	
	
	

	public IntBoard(int height, int width){
		super();
		this.grid = new BoardCell[height][width];	

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				grid[i][j] = new BoardCell(i, j);
			}
		}
		calcAdjacencies();
	}

	public void calcAdjacencies() {
		
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					Set<BoardCell> adjSet = new HashSet<BoardCell>();
					if (i > 0){
						adjSet.add(getCell(i-1, j));
					}
					if (i < grid.length - 1){
						adjSet.add(getCell(i + 1, j));
					}
					if (j > 0) {
						adjSet.add(getCell(i, j -1));
					}
					if (j < grid[i].length - 1){
						adjSet.add(getCell(i, j + 1));
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
	
	private void findAllTargets(BoardCell cell, int pathLength) {
		for (BoardCell c: this.adjMatrix.get(cell)){
			if (this.visited.contains(c)){
				continue;
			}
			if (pathLength == 1){
				this.targets.add(c);
			}else {
				this.visited.add(c);
				findAllTargets(c, pathLength -1);
				this.visited.remove(c);
			}
		}
	}

	public Set<BoardCell> getTargets(){
		return this.targets;	
	}

	public Set<BoardCell> getAdjList(BoardCell cell){
		return adjMatrix.get(cell);
	}

	public BoardCell getCell(int height, int width) {
		return grid[height][width];
	}

}
