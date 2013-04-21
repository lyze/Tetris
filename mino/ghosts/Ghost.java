package mino.ghosts;

import mino.Block;

public class Ghost extends Block {

	public Ghost(String spriteFilename) {
		super(spriteFilename);
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

}