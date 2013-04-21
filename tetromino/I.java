/**
 *
 */
package tetromino;

import mino.Block;
import mino.blocks.Empty;

/**
 * @author David Xu
 *
 */
public class I extends Tetromino {
	private final static int[][][] rightWallKickData = {
			{ { 0, 0 }, { -2, 0 }, { +1, 0 }, { -2, -1 }, { +1, +2 } },
			{ { 0, 0 }, { -1, 0 }, { +2, 0 }, { -1, +2 }, { +2, -1 } },
			{ { 0, 0 }, { +2, 0 }, { -1, 0 }, { +2, +1 }, { -1, -2 } },
			{ { 0, 0 }, { +1, 0 }, { -2, 0 }, { +1, -2 }, { -2, +1 } } };

	private final static int[][][] leftWallKickData = {
			{ { 0, 0 }, { -1, 0 }, { +2, 0 }, { -1, +2 }, { +2, -1 } },
			{ { 0, 0 }, { +2, 0 }, { -1, 0 }, { +2, +1 }, { -1, -2 } },
			{ { 0, 0 }, { +1, 0 }, { -2, 0 }, { +1, -2 }, { -2, +1 } },
			{ { 0, 0 }, { -2, 0 }, { +1, 0 }, { -2, -1 }, { +1, +2 } } };

	// As enumerated by clockwise rotation [0..3]
	private final static Block e = new Empty();
	private final static Block i = new mino.blocks.I();
	private final static Block[][] sprite0 = { { e, e, e, e },
				 						       { i, i, i, i },
				 						       { e, e, e, e },
				 						       { e, e, e, e } };

	private final static Block[][] sprite1 = { { e, e, i, e },
											   { e, e, i, e },
											   { e, e, i, e },
											   { e, e, i, e } };

	private final static Block[][] sprite2 = { { e, e, e, e },
											   { e, e, e, e },
											   { i, i, i, i },
											   { e, e, e, e } };

	private final static Block[][] sprite3 = { { e, i, e, e },
											   { e, i, e, e },
											   { e, i, e, e },
											   { e, i, e, e } };

	/**
	 * Put the I tetromino in the grid in default rotation
	 *
	 * @param g
	 *            The grid in which to place the tetromino
	 */
	public I(Block[][] g) {
		super(g, sprite0, sprite1, sprite2, sprite3, rightWallKickData,
				leftWallKickData);
	}

	@Override
	public Tetrominos type() {
		return Tetrominos.I;
	}

}
