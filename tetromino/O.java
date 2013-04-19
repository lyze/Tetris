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
public class O extends Tetromino {
	private final static Block e = new Empty();
	private final static Block o = new block.O();
	private final static Block[][] sprite0 = { { e, o, o, e },
				 						       { e, o, o, e },
				 						       { e, e, e, e },
                                               { e, e, e, e } };

	private final static Block[][] sprite1 = { { e, o, o, e },
											   { e, o, o, e },
											   { e, e, e, e },
                                               { e, e, e, e } };

	private final static Block[][] sprite2 = { { e, o, o, e },
											   { e, o, o, e },
											   { e, e, e, e },
                                               { e, e, e, e } };

	private final static Block[][] sprite3 = { { e, o, o, e },
											   { e, o, o, e },
											   { e, e, e, e },
                                               { e, e, e, e } };

	/**
	 * Put the O tetromino in the grid in default rotation
	 *
	 * @param g
	 *            The grid in which to place the tetromino
	 */
	public O(Block[][] g) {
		super(g, sprite0, sprite1, sprite2, sprite3);
	}

	@Override
	public Tetrominos type() {
		return Tetrominos.O;
	}
}
