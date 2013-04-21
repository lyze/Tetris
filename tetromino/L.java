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
public class L extends Tetromino {
	private final static Block e = new Empty();
	private final static Block l = new mino.blocks.L();
	private final static Block[][] sprite0 = { { e, e, l },
				 						       { l, l, l },
				 						       { e, e, e } };

	private final static Block[][] sprite1 = { { e, l, e },
											   { e, l, e },
											   { e, l, l } };

	private final static Block[][] sprite2 = { { e, e, e },
											   { l, l, l },
											   { l, e, e } };

	private final static Block[][] sprite3 = { { l, l, e },
											   { e, l, e },
											   { e, l, e } };

	/**
	 * Put the L tetromino in the grid in default rotation
	 *
	 * @param g
	 *            The grid in which to place the tetromino
	 */
	public L(Block[][] g) {
		super(g, sprite0, sprite1, sprite2, sprite3);
	}

	@Override
	public Tetrominos type() {
		return Tetrominos.L;
	}
}
