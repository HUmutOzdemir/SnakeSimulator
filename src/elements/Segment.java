package elements;

import java.awt.Color;

import ability.Drawable;
import ui.GridPanel;
/**
 * Class representing the location and color of a square in my game.
 * Stores next and previous Segment like a Node in doubly linkedlist.
 * @author Halil Umut Özdemir
 *
 */
public class Segment implements Drawable{
	private int x,y;
	private Color color;
	public Segment next=null;
	public Segment prev=null;
	/**
	 * Constructor for a segment which has specific color.
	 * @param x x coordinate of segment
	 * @param y y coordinate of segment
	 * @param color color of segment
	 */
	public Segment(int x,int y, Color color) {
		this.x=x;
		this.y=y;
		this.color=color;
	}
	/**
	 * Constructor for a segment which has no color.
	 * @param x x coordinate of segment
	 * @param y y coordinate of segment
	 */
	public Segment(int x, int y) {
		this.x=x;
		this.y=y;
		this.color=null;
	}
	/**
	 * Setter of location of a segment.
	 * @param x new x coordinate
	 * @param y new y coordinate
	 */
	public void setLocation(int x,int y) {
		this.x=x;
		this.y=y;
	}
	/**
	 * Draws segment to the panel.
	 * @param panel the panel which we draw our creatures
	 */
	@Override
	public void draw(GridPanel panel) {
		panel.drawSquare(x, y, color);
	}
	/**
	 * Setter for color of segment.
	 * @param color new color
	 */
	void setColor(Color color) {
		this.color = color;
	}
	/**
	 * Getter for x coordinate of segment
	 * @return x coordinate of segment
	 */
	public int getX() {
		return x;
	}
	/**
	 * Getter for y coordinate of segment
	 * @return y coordinate of segment
	 */
	public int getY() {
		return y;
	}
}
