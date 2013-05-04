package tetromino;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mino.Block;
import mino.blocks.Empty;
import mino.ghosts.Ghost;

public abstract class Tetromino implements Playable {
	private final static Block e = new Empty();
	private int[][][] rightWallKickData = {
			{ { 0, 0 }, { -1, 0 }, { -1, +1 }, { 0, -2 }, { -1, -2 } },
			{ { 0, 0 }, { +1, 0 }, { +1, -1 }, { 0, +2 }, { +1, +2 } },
			{ { 0, 0 }, { +1, 0 }, { +1, +1 }, { 0, -2 }, { +1, -2 } },
			{ { 0, 0 }, { -1, 0 }, { -1, -1 }, { 0, +2 }, { -1, +2 } } };
	private int[][][] leftWallKickData = {
			{ { 0, 0 }, { +1, 0 }, { +1, +1 }, { 0, -2 }, { +1, -2 } },
			{ { 0, 0 }, { +1, 0 }, { +1, -1 }, { 0, +2 }, { +1, +2 } },
			{ { 0, 0 }, { -1, 0 }, { -1, +1 }, { 0, -2 }, { -1, -2 } },
			{ { 0, 0 }, { -1, 0 }, { -1, -1 }, { 0, +2 }, { -1, +2 } } };
	private final Block[][] grid;
	private final Block[][] sprite0;
	private final Block[][] sprite1;
	private final Block[][] sprite2;
	private final Block[][] sprite3;
	private int x; // Horizontal offset from center
	private int y; // Vertical offset from bottom row (piece spawns
					// in 21st row)
	private final Block[][][] sprites;
	private final Block[][][] ghosts;
	private int rotation;
	protected final Ghost ghost;
	private int ghostY;

	/**
	 * Takes four mino array states obtainable by clockwise rotation
	 *
	 * @param g
	 *            The game grid that this mino should be bound to
	 * @param s0
	 * @param s1
	 * @param s2
	 * @param s3
	 * @param h
	 *            The respective heights of each sprite.
	 */
	protected Tetromino(Block[][] g, Block[][] s0, Block[][] s1, Block[][] s2,
			Block[][] s3) {
		assert g.length == 22 : "Grid should be 22 high";
		assert g[0].length == 10 : "Grid should be 10 wide";
		if (g == null || s0 == null || s1 == null || s2 == null || s3 == null)
			throw new InvalidParameterException(
					"Constructor arguments cannot be null.");
		grid = g;
		sprite0 = s0;
		sprite1 = s1;
		sprite2 = s2;
		sprite3 = s3;
		sprites = new Block[4][sprite0.length][sprite0.length];
		sprites[0] = sprite0;
		sprites[1] = sprite1;
		sprites[2] = sprite2;
		sprites[3] = sprite3;

		for (int i = 0; i < sprite0.length; i++) {
			for (int j = 0; j < sprite0[i].length; j++) {
				grid[21 - i][3 + j] = sprite0[i][j];
			}
		}
		x = 0;
		y = 20;
		rotation = 0;

		Pattern p = Pattern.compile("(?:.*\\.)*(.*)");
		Matcher m = p.matcher(getClass().getName());
		m.find();
		String ghostClassName = "mino.ghosts." + m.group(1);
		Ghost ghostInstance = null;
		try {
			Class<?> c = Class.forName(ghostClassName);
			Constructor<?> ctor = c.getConstructor();
			ghostInstance = (Ghost) ctor.newInstance();
		} catch (ClassNotFoundException e) {
			System.out.println("Cannot load ghost tetromino sprite "
					+ m.group(1) + ". The qualified class " + e.getMessage()
					+ " was not found.");
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Cannot load data for ghost tetromino sprite "
					+ m.group(1) + ". " + e.getMessage());
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Cannot load data for ghost tetromino sprite "
					+ m.group(1) + ". " + e.getMessage());
		}
		assert ghostInstance != null;
		ghost = ghostInstance;
		ghostY = y;
		ghosts = new Block[4][sprite0.length][sprite0.length];
		for (int n = 0; n < 4; n++)
			for (int i = 0; i < sprite0.length; i++)
				for (int j = 0; j < sprite0[i].length; j++) {
					if (sprites[n][i][j].isEmpty())
						ghosts[n][i][j] = e;
					else
						ghosts[n][i][j] = ghost;
				}
	}

	/**
	 * @param g
	 *            The game grid
	 * @param s0
	 * @param s1
	 * @param s2
	 * @param s3
	 * @param r
	 *            Clockwise wall kick data
	 * @param l
	 *            Counterclockwise wall kick data
	 */
	protected Tetromino(Block[][] g, Block[][] s0, Block[][] s1, Block[][] s2,
			Block[][] s3, int[][][] r, int[][][] l) {
		this(g, s0, s1, s2, s3);
		rightWallKickData = r;
		leftWallKickData = l;
	}

	public Block[][] getSprite(int n) {
		n = mod0to3(n);
		Block[][] src = sprites[n];
		Block[][] tgt = new Block[src.length][src[0].length];
		tgt = src.clone();
		for (int i = 0; i < src[0].length; i++)
			tgt[i] = src[i].clone();
		return tgt;
	}

	public Block[][] getSprite() {
		Block[][] src = sprites[rotation];
		Block[][] tgt = new Block[src.length][src[0].length];
		tgt = src.clone();
		for (int i = 0; i < src[0].length; i++)
			tgt[i] = src[i].clone();
		return tgt;
	}

	@Override
	public int hardDrop() {
		removeSprite();
		int distance = computeGhost();
		putSprite(x, ghostY, rotation);
		return distance;
	}

	private int computeGhost() {
		int distance = 0;
		ghostY = y;
		while (isValid(x, ghostY - 1, rotation)) {
			ghostY--;
			distance++;
		}
		if (ghostY != y)
			putGhost();
		return distance;
	}

	@Override
	public boolean moveDown() {
		removeGhost();
		removeSprite();
		if (!isValid(x, y - 1, rotation)) {
			restoreSprite();
			return false;
		}
		y--;
		computeGhost();
		restoreSprite();
		return true;
	}

	@Override
	public boolean moveLeft() {
		removeSprite();
		if (!isValid(x - 1, y, rotation)) {
			restoreSprite();
			return false;
		}
		removeGhost();
		x--;
		computeGhost();
		restoreSprite();
		return true;
	}

	@Override
	public boolean moveRight() {
		removeSprite();
		if (!isValid(x + 1, y, rotation)) {
			restoreSprite();
			return false;
		}
		removeGhost();
		x++;
		computeGhost();
		restoreSprite();
		return true;
	}

	@Override
	public boolean rotateRight() {
		removeGhost();
		removeSprite();
		if (tryRotateRight()) { // success
			computeGhost();
			restoreSprite();
			return true;
		}
		return false;
	}

	@Override
	public boolean rotateLeft() {
		removeGhost();
		removeSprite();
		if (tryRotateLeft()) { // success
			computeGhost();
			restoreSprite();
			return true;
		}
		return false;
	}

	@Override
	public boolean softDrop() {
		removeGhost();
		computeGhost();
		return moveDown();
	}

	/**
	 * @param height
	 *            Vertical offset from the bottom row. A strict upper bound.
	 * @return True if the tetromino is strictly above the height offset.
	 */
	public boolean isAbove(int height) {
		Block[][] sprite = sprites[rotation];
		for (int i = sprite.length - 1; i >= 0; i--) {
			for (int j = 0; j < sprite[i].length; j++) {
				if (!sprite[i][j].isEmpty()) {
					if (y + 1 - i > height)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * If checking if a sprite will be valid, first remove the sprite from the
	 * grid with {@link #removeSprite() removeSprite}, call this method, and
	 * finally return the sprite to its original position with
	 * {@link #restoreSprite() restoreSprite}.
	 *
	 * @param x
	 *            New horizontal offset from center
	 * @param y
	 *            New vertical offset from bottom row
	 * @param sprites
	 *            New rotation state
	 * @return
	 */
	protected boolean isValid(int x, int y, int n) {
		n = mod0to3(n);
		if (!(x >= -5 && x <= 5) || !(y >= 0 && y <= 21))
			return false;
		Block[][] rotated = sprites[n];
		for (int i = 0; i < rotated.length; i++) {
			for (int j = 0; j < rotated[0].length; j++) {
				int gridx = 3 + j + x;
				int gridy = y - i + 1;
				if (!rotated[i][j].isEmpty()
						&& (gridx < 0 || gridx > 9 || gridy < 0 || gridy > 21
								|| !grid[gridy][gridx].isEmpty())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Try to rotate the current tetromino with wall kicks.
	 *
	 * @return True, if a rotation succeeds; false, if none executed.
	 */
	protected boolean tryRotateLeft() {
		for (int i = 0; i < leftWallKickData[rotation].length; i++) {
			int x_offset = leftWallKickData[rotation][i][0];
			int y_offset = leftWallKickData[rotation][i][1];
			if (isValid(x + x_offset, y + y_offset, rotation - 1)) {
				x += x_offset;
				y += y_offset;
				rotation = mod0to3(rotation - 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * Try to rotate the current tetromino clockwise with wall kicks.
	 *
	 * @return True, if a rotation succeeds; false, if none executed.
	 */
	protected boolean tryRotateRight() {
		for (int i = 0; i < rightWallKickData[rotation].length; i++) {
			int x_offset = rightWallKickData[rotation][i][0];
			int y_offset = rightWallKickData[rotation][i][1];
			if (isValid(x + x_offset, y + y_offset, rotation + 1)) {
				x += x_offset;
				y += y_offset;
				rotation = mod0to3(rotation + 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * Put the sprite back where it was (as defined by its instance fields).
	 * Used after calling {@link #removeSprite() removeSprite}.
	 */
	protected void restoreSprite() {
		putSprite(x, y, rotation);
	}

	private void putSprite(int x, int y, int n) {
		n = mod0to3(n);
		Block[][] s = sprites[n];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length; j++) {
				if (!s[i][j].isEmpty())
					grid[y - i + 1][3 + x + j] = s[i][j];
			}
		}
		this.x = x;
		this.y = y;
		rotation = n;
	}

	private void putGhost() {
		Block[][] s = sprites[rotation];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length; j++) {
				if (!s[i][j].isEmpty())
					grid[ghostY - i + 1][3 + x + j] = ghost;
			}
		}
	}

	public void removeGhost() {
		Block[][] s = sprites[rotation];
		for (int i = 0; i < s.length; i++)
			for (int j = 0; j < s[i].length; j++) {
				if (s[i][j].isEmpty())
					continue;
				grid[ghostY - i + 1][3 + j + x] = e;
			}
	}

	/**
	 * @param n
	 * @return
	 */
	private int mod0to3(int n) {
		if (n < 0)
			return 4 + n % 4;
		return n % 4;
	}

	public void removeSprite() {
		Block[][] s = sprites[rotation];
		for (int i = 0; i < s.length; i++)
			for (int j = 0; j < s[i].length; j++) {
				if (s[i][j].isEmpty())
					continue;
				int gridx = 3 + j + x;
				int gridy = y - i + 1;
				grid[gridy][gridx] = e;
			}
	}

	public void respawnSprite() {
		x = 0;
		y = 20;
		rotation = 0;
		restoreSprite();
		computeGhost();
	}

	public boolean isOnSurface() {
		removeSprite();
		boolean val = isValid(x, y - 1, rotation);
		restoreSprite();
		return !val;
	}

	public int getXOffset() {
		return x;
	}

	public int getYOffset() {
		return y;
	}

	public abstract Tetrominos type();

	/**
	 * Call this method before clearing lines.
	 *
	 * @return
	 */
	public boolean isTSpin() {
		if (type() == Tetrominos.T) {
			removeSprite();
			boolean immobile = !((isValid(x, y - 1, rotation)
					|| isValid(x, y + 1, rotation)
					|| isValid(x - 1, y, rotation) || isValid(x + 1, y,
					rotation)));
			restoreSprite();
			if (immobile)
				return true;
		}
		return false;
	}
}
