package elements;

import java.awt.Color;
import java.util.Random;

import ui.GridPanel;
/**
 * Class representing the food of snake in the game.
 * Has a specific color which is a final field of class.
 * @author Halil Umut Özdemir
 *
 */
public class Food extends Segment{
	private static final Color FOOD_COLOR = Color.GREEN;
	/**
	 * Constructor for food.
	 * @param x x coordinate of food
	 * @param y y coordinate of food
	 */
	public Food(int x, int y) {
		super(x, y, FOOD_COLOR);
	}
	/**
	 * A static method creates random food to an empty place on map.
	 * @param segmentMap current map in game
	 * @return new Food object
	 */
	public static Food generateFood(Segment[][] segmentMap) {
		Random rand = new Random();
		int x = rand.nextInt(segmentMap.length);
		int y = rand.nextInt(segmentMap[x].length);
		while(segmentMap[x][y]!=null){
			x = rand.nextInt(segmentMap.length);
			y = rand.nextInt(segmentMap[x].length);
		}
		return new Food(x,y);
	}
	/**
	 * Draws food to the panel.
	 * Food is drawn to panel as a small square because of this method.
	 */
	@Override
	public void draw(GridPanel panel) {
		panel.drawSmallSquare(this.getX(), this.getY(), FOOD_COLOR);
	}
}
