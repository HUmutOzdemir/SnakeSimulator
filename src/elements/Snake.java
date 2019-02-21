package elements;

import java.awt.Color;
import java.util.List;
import java.util.Queue;


import ability.Direction;
import ai.Action;
import ai.Information;
/**
 * Class representing the snakes in the game.
 * Snakes is objects like linked lists. They has head Segment and tail Segment.
 * Has a field(path)that is the shortest path to the food.
 * @author Halil Umut Özdemir
 *
 */
public class Snake {
	private static final int MAX_SIZE = 8;
	private static final Color HEAD_COLOR = Color.BLUE;
	private static final Color TAIL_COLOR=Color.RED;
	Segment head=null;
	Segment tail=null;
	private int size=0;
	private Queue<Direction> path;
	private boolean newPathCheck=true;
	/**
	 * Setter for newPathCheck.
	 * NewPathCheck is true if snake needs a new path, is false if snake doesn't need a new path.
	 * @param newPathCheck new value of newPathCheck
	 */
	public void setNewPathCheck(boolean newPathCheck) {
		this.newPathCheck = newPathCheck;
	}
	/**
	 * A method for adding a segment at the end of a snake.
	 * @param newSegment new Segment which will be added to snake
	 */
	private void add(Segment newSegment) {
		if(head==null) {
			head=newSegment;
			tail=newSegment;
			head.setColor(HEAD_COLOR);
		}else {
			tail.next=newSegment;
			newSegment.prev=tail;
			tail=newSegment;
			tail.setColor(TAIL_COLOR);
		}
		size++;
	}
	/**
	 * A method for adding a segment at the begining of a snake.
	 * @param newSegment new Segment which will be added to snake
	 */
	private void addToBegining(Segment newSegment) {
		if(head==null) {
			head=newSegment;
			tail=newSegment;
			head.setColor(HEAD_COLOR);
		}else {
			head.setColor(TAIL_COLOR);
			newSegment.setColor(HEAD_COLOR);
			head.prev=newSegment;
			newSegment.next=head;
			head=newSegment;
		}
		size++;
	}
	/**
	 * Removes and returns the last segment of snake.
	 * @return the removed segment
	 */
	private Segment removeLast() {
		Segment temp = tail;
		if(temp==null) {
			return null;
		}
		tail=tail.prev;
		tail.next=null;
		temp.prev=null;
		size--;
		return temp;
	}
	/**
	 * Creates the first snake for game.
	 * @return the first snake
	 */
	public static Snake generateFirstSnake() {
		Snake newSnake = new Snake();
		newSnake.add(new Segment(4,1));
		newSnake.add(new Segment(3,1));
		newSnake.add(new Segment(2,1));
		newSnake.add(new Segment(1,1));
		return newSnake;
	}
	/**
	 * MOves to snake to a specific direction.
	 * @param direction direction of movement
	 */
	public void move(Direction direction) {
		int dx=0;
		int dy=0;
		if(direction==Direction.UP) {
			dy--;
		}else if(direction==Direction.DOWN) {
			dy++;
		}else if(direction==Direction.LEFT) {
			dx--;
		}else if(direction==Direction.RIGHT) {
			dx++;
		}
		Segment temp = this.removeLast();
		temp.setLocation(this.head.getX()+dx, this.head.getY()+dy);
		this.addToBegining(temp);
	}
	/**
	 * Creates the new snake if it is necessary.
	 * @return the new snake 
	 */
	public Snake reproduce() {
		Snake newSnake = new Snake();
		for(int i=0;i<MAX_SIZE/2;i++) {
			newSnake.add(this.removeLast());
		}
		return newSnake;
	}
	/**
	 * Getter for head of snake
	 * @return the head of snake
	 */
	public Segment getHead() {
		return head;
	}
	/**
	 * A method for eating food by a snake
	 * @param food the food will be ate by snake
	 * @return
	 */
	public Segment eat(Food food) {
		Segment newSegment =new Segment(food.getX(),food.getY());
		addToBegining(newSegment);
		return newSegment;
	}
	/**
     * An instance method of Snake.
     * Decides the current action of Snake.
     * Creates new path only if it is necessary.
     * @return returns the most logical Action.
     * @param information the local information(creatures around it etc.) of current snake.
     */
	public Action chooseAction(Information info) {
		if(size==MAX_SIZE) {
			return new Action(Action.Type.REPRODUCE);
		}
		if(info.getSegmentDown() instanceof Food) {
			return new Action(Action.Type.EAT,Direction.DOWN);
		}
		if(info.getSegmentUp() instanceof Food) {
			return new Action(Action.Type.EAT,Direction.UP);
		}
		if(info.getSegmentLeft() instanceof Food) {
			return new Action(Action.Type.EAT,Direction.LEFT);
		}
		if(info.getSegmentRight() instanceof Food) {
			return new Action(Action.Type.EAT,Direction.RIGHT);
		}
		List<Direction> freeDirections = info.getFreeDirections();
		if(freeDirections.isEmpty()) {
			return new Action(Action.Type.STAY);
		}
		if(path==null||newPathCheck) {
			path=info.generatePath();
			newPathCheck=false;
		}
		if(!path.isEmpty()) {
			if(freeDirections.contains(path.peek())) {
				return new Action(Action.Type.MOVE,path.poll());
			}else {
				path=info.generatePath();
				if(!path.isEmpty()) {
					if(freeDirections.contains(path.peek())) {
						return new Action(Action.Type.MOVE,path.poll());
					}
				}
			}
		}
		newPathCheck=true;
		return new Action(Action.Type.MOVE,Information.getRandomDirection(freeDirections));
	}
}
