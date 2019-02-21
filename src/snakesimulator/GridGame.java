package snakesimulator;

import ui.GridPanel;

import javax.swing.*;

import ability.Drawable;
import elements.Food;
import elements.Segment;
import elements.Snake;


import java.util.HashSet;
import java.util.Set;


/**
 * Class representing a generic grid-based game.
 *
 */
public abstract class GridGame {

    private GridPanel gamePanel;
    private Timer gameTimer;

    private Set<Drawable> drawables;

    /**
     * Constructs a generic grid game
     * @param gridWidth width of the grid world
     * @param gridHeight height of the grid world
     * @param gridSquareSize size of a grid square in pixels
     * @param frameRate frame rate denoting the number of ticks per second
     */
    public GridGame(int gridWidth, int gridHeight, int gridSquareSize, int frameRate) {
        gamePanel = new GridPanel(gridWidth, gridHeight, gridSquareSize);
        gameTimer = new Timer(1000/frameRate, event -> {
            timerTick();
            redraw();
        });
        drawables = new HashSet<>();
    } 

    /**
     * Called at each tick to redraw the drawable objects added to this game.
     * Additionally draws gridlines.
     */
    private void redraw() {
        gamePanel.clearCanvas();
        gamePanel.drawGrid();

        for (Drawable drawable : drawables) {
            drawable.draw(gamePanel);
        }

        gamePanel.repaint();
    }

    /**
     * Adds a new drawable object to the game.
     * @param snake the snake which can be drawn to panel
     */
    protected void addDrawable(Snake snake) {
       Segment current = snake.getHead();
       while(current.next!=null) {
    	   drawables.add(current);
    	   current=current.next;
       }
       drawables.add(current);
    }
    /**
     * Adds a new drawable object to the game.
     * The added object will be drawn on the panel at each tick via its draw(...) method.
     * @param segment the segment which can be drawn to panel
     */
    protected void addDrawable(Segment segment) {
    	drawables.add(segment);
    }
    /**
     * Adds a new drawable object to the game.
     * The added object will be drawn on the panel at each tick via its draw(...) method.
     * @param food the food which can be drawn to panel
     */
    protected void addDrawable(Food food) {
    	drawables.add(food);
    }
    /**
     * Removes a previously added drawable object.
     * @param food removes the eaten food
     */
    protected void removeDrawable(Food food) {
    	drawables.remove(food);
    }

    /**
     * Getter for the underlying grid panel
     * @return grid panel to draw on
     */
    public GridPanel getGamePanel() {
        return gamePanel;
    }

    /**
     * Starts the game loop.
     * The game loop periodically calls method timerTick(), at the set frame rate.
     */
    public void start() {
        gameTimer.setInitialDelay(0);
        gameTimer.start();
    }

    /**
     * Stops the game loop
     */
    public void stop() {
        gameTimer.stop();
    }

    /**
     * Getter for the width of the grid world
     * @return number of grid squares along the width
     */
    public int getGridWidth() {
        return gamePanel.getGridWidth();
    }

    /**
     * Getter for the height of the grid world
     * @return number of grid squares along the height
     */
    public int getGridHeight() {
        return gamePanel.getGridHeight();
    }

    /**
     * Periodically executed timer tick method.
     * Every subclass should implement this method to define its game logic.
     */
    protected abstract void timerTick();
	
}
