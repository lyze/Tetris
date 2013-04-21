/**
 *
 */
package mino;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author David Xu
 *
 */
public class Block {
	public static int WIDTH = 24;
	public static int HEIGHT = 24;
	public static int PARTIAL = 4; // Height of the partially obscured 'top' row
	private BufferedImage sprite;

	/**
	 * @param filename
	 *            If null, then this is an empty block.
	 * @throws IOException
	 */
	public Block(String filename) {
		if (filename != null)
			try {
				sprite = ImageIO.read(new File(filename));
			} catch (IOException e) {
				System.out.println("Cannot process tetromino sprite \""
						+ filename + "\". " + e.getMessage());
			}
	}

	public boolean isEmpty() {
		return sprite == null;
	}

	/**
	 * @param g
	 * @param x
	 *            zero-indexed
	 * @param y
	 */
	public void draw(Graphics g, int x, int y) {
		g.drawImage(sprite, WIDTH * x, HEIGHT * (19 - y) + Block.PARTIAL, null);
	}

	/**
	 * Draw the truncated hidden row
	 *
	 * @param g
	 * @param x
	 *            zero-indexed
	 */
	public void drawPartial(Graphics g, int x) {
		g.drawImage(sprite, WIDTH * x, 0, WIDTH * (x + 1), PARTIAL, 0, HEIGHT
				- PARTIAL, WIDTH, HEIGHT, null);
	}
}
