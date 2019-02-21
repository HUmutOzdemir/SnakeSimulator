package snakesimulator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import elements.Food;
import elements.Segment;
import elements.Snake;
import ability.Direction;
import ai.Action;
import ai.Information;

/**
 * Class that implements the game logic for Snake Simulator.
 * 
 * @author Halil Umut Özdemir
 *
 */
public class SnakeSimulator extends GridGame{
	
	private List<Snake> snakes;
	private Segment[][] segmentMap;
	/**
     * Creates a new Snake Simulator game instance
     * @param gridWidth number of grid squares along the width
     * @param gridHeight number of grid squares along the height
     * @param gridSquareSize size of a grid square in pixels
     * @param frameRate frame rate (number of timer ticks per second)
     */
	public SnakeSimulator(int gridWidth, int gridHeight, int gridSquareSize, int frameRate) {
		super(gridWidth, gridHeight, gridSquareSize, frameRate);
		snakes = new ArrayList<Snake>();
		segmentMap = new Segment[gridWidth][gridHeight];
	}
	/**
	 * Determine and execute actions for all snakes.
	 */
	@Override
	protected void timerTick() {
		ArrayList<Snake> snakesCopy = new ArrayList<Snake>(snakes);
		Point foodLocation = null;
		for(Snake snake : snakesCopy) {
			// Copy the segmentMap
			Segment[][] segmentMapCopy = new Segment[getGridWidth()][getGridHeight()];
			for(int i=0;i<segmentMap.length;i++) {
				for(int j=0;j<segmentMap[i].length;j++) {
					if(segmentMap[i][j] instanceof Food) {
						foodLocation = new Point(i,j);
					}
					segmentMapCopy[i][j]=segmentMap[i][j];
				}
			}
			// Reset current snake's map position (its position will be marked again, if it still lives)
			removeSnakeFromMap(snake);
			// Choose action
			Action selectedAction = snake.chooseAction(createInformationForSnake(snake, foodLocation, segmentMapCopy));
			// Execute action
			if(selectedAction!=null) {
				if(selectedAction.getType()==Action.Type.STAY) {
					// Stay
				}else if(selectedAction.getType()==Action.Type.MOVE) {
					// Move
					snake.move(selectedAction.getDirection());
				}else if(selectedAction.getType()==Action.Type.REPRODUCE) {
					// Reproduce
					Snake newSnake = snake.reproduce();
					addSnakeToMap(newSnake);
					addElement(newSnake);
				}else if(selectedAction.getType()==Action.Type.EAT) {
					// Eat
					if(getSegmentAtDirection(snake.getHead().getX(), snake.getHead().getY(), selectedAction.getDirection(),segmentMapCopy) instanceof Food) {
						Food eatedFood = (Food)getSegmentAtDirection(snake.getHead().getX(), snake.getHead().getY(), selectedAction.getDirection(),segmentMapCopy);
						addDrawable(snake.eat(eatedFood));
						removeDrawable(eatedFood);
						Food newFood = Food.generateFood(segmentMapCopy);
						addElement(newFood);
						for(Snake current : snakes) {
							current.setNewPathCheck(true);
						}
					}
				}
			}
			// Add current snake to the map
			addSnakeToMap(snake);
			
		}
	}
	/**
	 * Getter for segment map
	 * @return the segmentMap
	 */
	public Segment[][] getSegmentMap() {
		return segmentMap;
	}
	/**
	 * Creates local information for a snake.
	 * @param snake current snake
	 * @param foodLocation location of food
	 * @param segmentMapCopy copy of segment map
	 * @return the Information object for current snake
	 */
	private Information createInformationForSnake(Snake snake,Point foodLocation,Segment[][]segmentMapCopy) {
		int x = snake.getHead().getX();
		int y = snake.getHead().getY();

		HashMap<Direction, Segment> segments = new HashMap<>();
		segments.put(Direction.UP, getSegmentAtPosition(x, y - 1,segmentMapCopy));
		segments.put(Direction.DOWN, getSegmentAtPosition(x, y + 1,segmentMapCopy));
		segments.put(Direction.LEFT, getSegmentAtPosition(x - 1, y,segmentMapCopy));
		segments.put(Direction.RIGHT, getSegmentAtPosition(x + 1, y,segmentMapCopy));

		ArrayList<Direction> freeDirections = new ArrayList<>();
		if (segments.get(Direction.UP) == null && isPositionInsideGrid(x, y - 1)) {
			freeDirections.add(Direction.UP);
		}
		if (segments.get(Direction.DOWN) == null && isPositionInsideGrid(x, y + 1)) {
			freeDirections.add(Direction.DOWN);
		}
		if (segments.get(Direction.LEFT) == null && isPositionInsideGrid(x - 1, y)) {
			freeDirections.add(Direction.LEFT);
		}
		if (segments.get(Direction.RIGHT) == null && isPositionInsideGrid(x + 1, y)) {
			freeDirections.add(Direction.RIGHT);
		}
		return new Information(getGridWidth(), getGridHeight(), segments, freeDirections,snake.getHead(),foodLocation,segmentMapCopy);
	}
	/**
	 * Add new food to the game.
	 * @param food new Food
	 */
	public void addElement(Food food) {
		updateSegmentMap(food.getX(), food.getY(), food);
		addDrawable(food);
	}
	/**
	 * Add new snake to the game.
	 * @param snake new Snake
	 */
	public boolean addElement(Snake snake) {
		addSnakeToMap(snake);
		snakes.add(snake);
		addDrawable(snake);
		return true;
	}
	/**
	 * Remove snake from segmenMap.
	 * @param snake the snake will be removed
	 */
	private void removeSnakeFromMap(Snake snake) {
		Segment current = snake.getHead();
		while(current!=null) {
			updateSegmentMap(current.getX(), current.getY(), null);
			current=current.next;
		}
	}
	/**
	 * Add snake to the segmentMap.
	 * @param snake the snake will be added
	 */
	private void addSnakeToMap(Snake snake) {
		Segment current = snake.getHead();
		while(current!=null) {
			updateSegmentMap(current.getX(), current.getY(), current);
			current=current.next;
		}
	}
	/**
	 * Updates the segment map
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param segment segment will be put at location
	 */
	private void updateSegmentMap(int x,int y, Segment segment) {
		if (isPositionInsideGrid(x, y)) {
			segmentMap[x][y] = segment;
		}
	}
	/**
	 * Returns a segment at a position.
	 * @param x x coordinate of location
	 * @param y y coordinate of location
	 * @param segmentMapCopy copy of segmentMap
	 * @return the segment at that location
	 */
	private Segment getSegmentAtPosition(int x, int y,Segment[][]segmentMapCopy) {
		if (!isPositionInsideGrid(x, y)) {
			return null;
		}
		return segmentMapCopy[x][y];
	}
	/**
	 * Returns a segment at a direction.
	 * @param x x coordinate of location
	 * @param y y coordinate of location
	 * @param direction the direction will be looked
	 * @param segmentMapCopy copy of segmentMap
	 * @return the segment at that direction
	 */
	private Segment getSegmentAtDirection(int x, int y, Direction direction,Segment[][]segmentMapCopy) {
		if (direction == null) {
			return null;
		}
		int xTarget = x;
		int yTarget = y;
		if (direction == Direction.UP) {
			yTarget--;
		} else if (direction == Direction.DOWN) {
			yTarget++;
		} else if (direction == Direction.LEFT) {
			xTarget--;
		} else if (direction == Direction.RIGHT) {
			xTarget++;
		}
		return getSegmentAtPosition(xTarget, yTarget,segmentMapCopy);
	}
	/**
	 * Checks is the position inside the gird.
	 * @param x x coordinate of position
	 * @param y y coordinate of position
	 * @return is the position inside the grid or not
	 */
	private boolean isPositionInsideGrid(int x, int y) {
		return (x >= 0 && x < getGridWidth()) && (y >= 0 && y < getGridHeight());
	}
}
