/**
 *
 */
package tetromino;

import block.Block;
import block.Empty;

/**
 * @author David Xu
 *
 */
public class J extends Tetromino {
	private final static Block e = new Empty();
	private final static Block j = new block.J();
	private final static Block[][] sprite0 = { { j, e, e },
				 						       { j, j, j },
				 						       { e, e, e } };

	private final static Block[][] sprite1 = { { e, j, j },
											   { e, j, e },
											   { e, j, e } };

	private final static Block[][] sprite2 = { { e, e, e },
											   { j, j, j },
											   { e, e, j } };

	private final static Block[][] sprite3 = { { e, j, e },
											   { e, j, e },
											   { j, j, e } };

	/**
	 * Put the J tetromino in the grid in default rotation
	 *
	 * @param g
	 *            The grid in which to place the tetromino
	 */
	public J(Block[][] g) {
		super(g, sprite0, sprite1, sprite2, sprite3);
	}

	@Override
	public Tetrominos type() {
		return Tetrominos.J;
	}
}
