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
public class Z extends Tetromino {
	private final static Block e = new Empty();
	private final static Block z = new mino.blocks.Z();
	private final static Block[][] sprite0 = { { z, z, e },
				 						       { e, z, z },
				 						       { e, e, e } };

	private final static Block[][] sprite1 = { { e, e, z },
											   { e, z, z },
											   { e, z, e } };

	private final static Block[][] sprite2 = { { e, e, e },
											   { z, z, e },
											   { e, z, z } };

	private final static Block[][] sprite3 = { { e, z, e },
											   { z, z, e },
											   { z, e, e } };

	/**
	 * Put the Z tetromino in the grid in default rotation
	 *
	 * @param g
	 *            The grid in which to place the tetromino
	 */
	public Z(Block[][] g) {
		super(g, sprite0, sprite1, sprite2, sprite3);
	}

	@Override
	public Tetrominos type() {
		return Tetrominos.Z;
	}
}
