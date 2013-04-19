/**
 *
 */
package tetromino;

/**
 * @author David Xu
 *
 */
public interface Playable {
	public boolean moveLeft();

	public boolean moveRight();

	public boolean moveDown();

	public boolean hardDrop();

	public boolean softDrop();

	public boolean rotateRight();

	public boolean rotateLeft();
}
