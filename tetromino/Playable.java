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

	/**
	 * @return Number of lines the tetromino hard-dropped
	 */
	public int hardDrop();

	public boolean softDrop();

	public boolean rotateRight();

	public boolean rotateLeft();
}
