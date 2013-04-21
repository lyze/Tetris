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
public class T extends Tetromino {
	private final static Block e = new Empty();
	private final static Block t = new mino.blocks.T();
	private final static Block[][] sprite0 = { { e, t, e },
				 						       { t, t, t },
				 						       { e, e, e } };

	private final static Block[][] sprite1 = { { e, t, e },
											   { e, t, t },
											   { e, t, e } };

	private final static Block[][] sprite2 = { { e, e, e },
											   { t, t, t },
											   { e, t, e } };

	private final static Block[][] sprite3 = { { e, t, e },
											   { t, t, e },
											   { e, t, e } };

	/**
	 * Put the T tetromino in the grid in default rotation
	 *
	 * @param g
	 *            The grid in which to place the tetromino
	 */
	public T(Block[][] g) {
		super(g, sprite0, sprite1, sprite2, sprite3);
	}

	@Override
	public Tetrominos type() {
		return Tetrominos.T;
	}
}
