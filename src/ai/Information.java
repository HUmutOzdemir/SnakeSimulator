package ai;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


import ability.Direction;
import elements.Segment;
/**
 * Class representing the information for any snake in the game loop.
 * Class including information about snakes surroundings and generates the shortest path for any snake.
 * Automatically created and passed by the game to each snake at each timer tick.
 *
 */

public class Information {
	private static int gridWidth;
	private static int gridHeight;

	private HashMap<Direction, Segment> segments;
	private List<Direction> freeDirections;
	private Segment snakeHead;
	private static Point foodLocation;
	private Segment[][] currentMap;

	/**
	 * Constructs the information for a snake.
	 * @param gridWidth width of the grid world
	 * @param gridHeight height of the grid world
	 * @param segments mapping of directions to neighbor creatures
	 * @param freeDirections list of free directions
	 * @param snakeHead current location of snake's head
	 * @param foodLocation current location of food
	 * @param currentMap copy of current game map
	 */
	public Information(int gridWidth, int gridHeight,
			HashMap<Direction, Segment> segments, List<Direction> freeDirections,Segment snakeHead,Point foodLocation,Segment[][]currentMap) {
		Information.gridWidth = gridWidth;
		Information.gridHeight = gridHeight;
		this.segments = segments;
		this.freeDirections = freeDirections;
		this.snakeHead=snakeHead;
		Information.foodLocation=foodLocation;
		this.currentMap=currentMap;
	}

	/**
	 * Getter for the width of the grid world.
	 * Can be used to assess the boundaries of the world.
	 * @return number of grid squares along the width
	 */
	public int getGridWidth() {
		return gridWidth;
	}

	/**
	 * Getter for the height of the grid world.
	 * Can be used to assess the boundaries of the world.
	 * @return number of grid squares along the height
	 */
	public int getGridHeight() {
		return gridHeight;
	}

	/**
	 * Returns the neighbor creature one square up
	 * @return creature or null if no creature exists
	 */
	public Segment getSegmentUp() {
		return segments.get(Direction.UP);
	}

	/**
	 * Returns the neighbor creature one square down
	 * @return creature or null if no creature exists
	 */
	public Segment getSegmentDown() {
		return segments.get(Direction.DOWN);
	}

	/**
	 * Returns the neighbor creature one square left
	 * @return creature or null if no creature exists
	 */
	public Segment getSegmentLeft() {
		return segments.get(Direction.LEFT);
	}

	/**
	 * Returns the neighbor creature one square right
	 * @return creature or null if no creature exists
	 */
	public Segment getSegmentRight() {
		return segments.get(Direction.RIGHT);
	}

	/**
	 * Returns the list of free directions around the current position.
	 * The list does not contain directions out of bounds or containing a creature.
	 * Can be used to determine the directions available to move or reproduce.
	 * @return creature or null if no creature exists
	 */
	public List<Direction> getFreeDirections() {
		return freeDirections;
	}
	/**
	 * Returns the list of free points around a coordinate.
	 * Location of food is accepted free(Snake can make move through the food)
	 * In path matrix -1 is location of food, 0 is free point.
	 * @param x x coordinate of a point
	 * @param y y coordinate of a point
	 * @param pathMatrix the model of current map to find the shortest path
	 * @return list of free points around a point
	 */
	private List<Point> getFreePointsAround(int x,int y,int[][]pathMatrix){
		LinkedList<Point> freePointsAround = new LinkedList<Point>();
		if(isPositionInsideGrid(x-1, y)) {
			if((currentMap[x-1][y]==null&&pathMatrix[x-1][y]==0)||pathMatrix[x-1][y]==-1) {
				freePointsAround.add(new Point(x-1,y));
			}
		}
		if(isPositionInsideGrid(x+1, y)) {
			if((currentMap[x+1][y]==null&&pathMatrix[x+1][y]==0)||pathMatrix[x+1][y]==-1) {
				freePointsAround.add(new Point(x+1,y));
			}
		}
		if(isPositionInsideGrid(x, y+1)) {
			if((currentMap[x][y+1]==null&&pathMatrix[x][y+1]==0)||pathMatrix[x][y+1]==-1) {
				freePointsAround.add(new Point(x,y+1));
			}
		}
		if(isPositionInsideGrid(x, y-1)) {
			if((currentMap[x][y-1]==null&&pathMatrix[x][y-1]==0)||pathMatrix[x][y-1]==-1) {
				freePointsAround.add(new Point(x,y-1));
			}
		}
		return freePointsAround;
	}
	/**
	 * Utility function to get a randomly selected direction among multiple directions.
	 * The selection is uniform random: All directions in the list have an equal chance to be chosen.
	 * @param possibleDirections list of possible directions
	 * @return direction randomly selected from the list of possible directions
	 */
	public static Direction getRandomDirection(List<Direction> possibleDirections) {
		if (possibleDirections.isEmpty()) {
			return null;
		}
		int randomIndex = (int)(Math.random() * possibleDirections.size());
		return possibleDirections.get(randomIndex);
	}
	/**
	 * Checks a point inside the grid or not.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true is point is inside grid, false is point outside grid
	 */
	private boolean isPositionInsideGrid(int x, int y) {
		return (x >= 0 && x < getGridWidth()) && (y >= 0 && y < getGridHeight());
	}
	/**
	 * Generates the shortest path for food
	 * More detailed information will be in the project report.
	 * @return the direction list of shortest path to the food for a snake
	 */
	public Queue<Direction> generatePath(){
		int[][] pathMatrix = new int[getGridWidth()][getGridHeight()];
		pathMatrix[foodLocation.x][foodLocation.y]=-1;
		pathMatrix[snakeHead.getX()][snakeHead.getY()]=1;
		Queue<Point> queue = new LinkedList<Point>();
		queue.add(new Point(snakeHead.getX(),snakeHead.getY()));
		boolean check = false;
		while(!queue.isEmpty()) {
			Point current = queue.poll();
			int pathLevel = pathMatrix[current.x][current.y];
			List<Point> points = getFreePointsAround(current.x, current.y, pathMatrix);
			for(Point p : points){
				if(pathMatrix[p.x][p.y]==-1) {
					check=true;
					break;
				}else {
					queue.add(p);
					pathMatrix[p.x][p.y]=pathLevel+1;
				}
			}
			if(check) {
				break;
			}
		}
		if(!check) {
			return new LinkedList<Direction>();
		}
		LinkedList<Direction> path = new LinkedList<Direction>();
		Point current = foodLocation;
		int pathLevel = findSmallestLevelForFood(pathMatrix);
		while(pathMatrix[current.x][current.y]!=1) {
			List<Point> points = getPointsAround(current.x, current.y);
			for(Point next : points) {
				if(pathMatrix[next.x][next.y]==pathLevel) {
					Direction direction = getDirection(current, next);
					if(direction!=null) {
						path.add(0, direction);
						pathLevel--;
						current=next;
						break;
					}
				}
			}
		}
		return path;
	}
	/**
	 * Finds the direction of next point due to current point.
	 * @param current current point
	 * @param next next point 
	 * @return the direction of next point due to current point
	 */
	private static Direction getDirection(Point current,Point next) {
		if(next.x-current.x==1) {
			return Direction.LEFT;
		}
		if(next.x-current.x==-1) {
			return Direction.RIGHT;
		}
		if(next.y-current.y==1) {
			return Direction.UP;
		}
		if(next.y-current.y==-1) {
			return Direction.DOWN;
		}
		return null;
	}
	/**
	 * Generates a list of all points around a point.
	 * @param x x coordinate of a point
	 * @param y y coordinate of a point
	 * @return the list of all points around a point
	 */
	private List<Point> getPointsAround(int x,int y){
		LinkedList<Point> points = new LinkedList<Point>();
		if(isPositionInsideGrid(x, y)) {
			if(isPositionInsideGrid(x+1, y)) {
				points.add(new Point(x+1,y));
			}
			if(isPositionInsideGrid(x-1, y)) {
				points.add(new Point(x-1,y));
			}
			if(isPositionInsideGrid(x, y+1)) {
				points.add(new Point(x,y+1));
			}
			if(isPositionInsideGrid(x, y-1)) {
				points.add(new Point(x,y-1));
			}
		}
		return points;
	}
	/**
	 * Finds the smallest level around the food.
	 * More detailed information will be in the project report.
	 * @param matrix the pathMatrix for current path
	 * @return return the smallest level around food
	 */
	private int findSmallestLevelForFood(int[][]matrix) {
		int min = Integer.MAX_VALUE;
		if(isPositionInsideGrid(foodLocation.x+1, foodLocation.y)) {
			if(matrix[foodLocation.x+1][foodLocation.y]>0) {
				min=Math.min(min,matrix[foodLocation.x+1][foodLocation.y]);
			}
		}
		if(isPositionInsideGrid(foodLocation.x-1, foodLocation.y)) {
			if(matrix[foodLocation.x-1][foodLocation.y]>0) {
				min=Math.min(min,matrix[foodLocation.x-1][foodLocation.y]);
			}
		}
		if(isPositionInsideGrid(foodLocation.x, foodLocation.y-1)) {
			if(matrix[foodLocation.x][foodLocation.y-1]>0) {
				min=Math.min(min,matrix[foodLocation.x][foodLocation.y-1]);
			}
		}
		if(isPositionInsideGrid(foodLocation.x, foodLocation.y+1)) {
			if(matrix[foodLocation.x][foodLocation.y+1]>0) {
				min=Math.min(min,matrix[foodLocation.x][foodLocation.y+1]);
			}
		}
		return min;
	}
}
