package main;

import java.awt.*;

import elements.Food;
import elements.Snake;
import snakesimulator.SnakeSimulator;
import ui.ApplicationWindow;

/**
 * The main class of the project.
 */
public class Main {

	/**
	 * Main entry point for the application.
	 *
	 * @param args application arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				// Create game
				// You can change the world width and height, size of each grid square in pixels or the game speed
				SnakeSimulator game = new SnakeSimulator(40,40,17,100);
				
				// Create and add first snake
				game.addElement(Snake.generateFirstSnake());
				
				
				// Create and add food
				game.addElement(Food.generateFood(game.getSegmentMap()));
				
				
				
				

				// Create application window that contains the game panel
				ApplicationWindow window = new ApplicationWindow(game.getGamePanel());
				window.getFrame().setVisible(true);

				// Start game
				game.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}


