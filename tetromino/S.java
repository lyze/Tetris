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
public class S extends Tetromino {
	private final static Block e = new Empty();
	private final static Block s = new block.S();
	private final static Block[][] sprite0 = { { e, s, s },
				 						       { s, s, e },
				 						       { e, e, e } };

	private final static Block[][] sprite1 = { { e, s, e },
											   { e, s, s },
											   { e, e, s } };

	private final static Block[][] sprite2 = { { e, e, e },
											   { e, s, s },
											   { s, s, e } };

	private final static Block[][] sprite3 = { { s, e, e },
											   { s, s, e },
											   { e, s, e } };

	/**
	 * Put the S tetromino in the grid in default rotation
	 *
	 * @param g
	 *            The grid in which to place the tetromino
	 */
	public S(Block[][] g) {
		super(g, sprite0, sprite1, sprite2, sprite3);
	}

	@Override
	public Tetrominos type() {
		return Tetrominos.S;
	}
}
