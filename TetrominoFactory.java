/**
 *
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import javax.lang.model.element.UnknownElementException;

import tetromino.Tetromino;
import tetromino.Tetrominos;
import block.Block;

/**
 * Generate pieces according to randomization per each 7-piece bag
 *
 * @author David Xu
 */
public class TetrominoFactory {

	private final Deque<Tetrominos> bag;
	private final Tetrominos[] model;
	// private final Block[][][] pool;
	Block[][] grid;

	public TetrominoFactory(Block[][] g) {
		grid = g;
		bag = new ArrayDeque<Tetrominos>();
		model = Tetrominos.values();
		// pool = new Block[model.length][][];
		// pool[0] = new I(null).getSprite(0);
		// pool[1] = new J(null).getSprite(0);
		// pool[2] = new L(null).getSprite(0);
		// pool[3] = new O(null).getSprite(0);
		// pool[4] = new S(null).getSprite(0);
		// pool[5] = new T(null).getSprite(0);
		// pool[6] = new Z(null).getSprite(0);

		boolean[] isSeen = new boolean[7];
		int numSeen = 0;
		int i = (int) (model.length * Math.random());
		while (!isSeen[i]) {
			bag.add(model[i]);
			isSeen[i] = true;
			i = (int) (model.length * Math.random());
			numSeen++;
			while (isSeen[i] && numSeen < 7)
				i = (int) (model.length * Math.random());
		}
	}

	/**
	 * @param t
	 *            Enum of the tetromino to generate (i.e. Tetrominos.T) in spawn
	 *            position
	 * @return Spawn sprite of given tetromino
	 */
	// public Block[][] getSprite(Tetrominos t) {
	// Block[][] src = pool[t.ordinal()];
	// Block[][] tgt = src.clone();
	// for (int i = 0; i < src[0].length; i++)
	// tgt[i] = src[i].clone();
	// return tgt;
	// }

	/**
	 * @param n
	 *            Number of elements to retrieve
	 * @return n The first n elements.
	 */
	public Tetrominos[] getInQueue(int n) {
		if (bag.size() < n)
			bag.addAll(generateBag());
		Iterator<Tetrominos> i = bag.iterator();
		Tetrominos[] q = new Tetrominos[n];
		for (int k = 0; k < n; k++)
			q[k] = i.next();
		return q;
	}

	public Tetromino next() {
		if (bag.size() == 0)
			bag.addAll(generateBag());
		return makeTetromino();
	}

	private Tetromino makeTetromino() {
		if (bag.size() == 0)
			bag.addAll(generateBag());
		Tetrominos t = bag.remove();
		try {
			Class<?> c = Class.forName("tetromino." + t.toString());
			Constructor<?> ctor = c.getConstructor(Block[][].class);
			Object[] initargs = { grid };
			return (Tetromino) ctor.newInstance(initargs);
		} catch (ClassNotFoundException e) {
			System.out.println("Internal error: Could not generate tetromino "
					+ t.toString() + ". " + e.getMessage());
		} catch (NoSuchMethodException e) {
			System.out.println("Internal error: Could not generate tetromino "
					+ t.toString() + ". " + e.getMessage());
		} catch (SecurityException e) {
			System.out.println("Internal error: Could not generate tetromino "
					+ t.toString() + ". " + e.getMessage());
		} catch (InstantiationException e) {
			System.out.println("Internal error: Could not generate tetromino "
					+ t.toString() + ". " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("Internal error: Could not generate tetromino "
					+ t.toString() + ". " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Internal error: Could not generate tetromino "
					+ t.toString() + ". " + e.getMessage());
		} catch (InvocationTargetException e) {
			System.out.println("Internal error: Could not generate tetromino "
					+ t.toString() + ". " + e.getMessage());
		}
		throw new UnknownElementException(null, t);
	}

	public void reset() {
		bag.removeAll(bag);
		bag.addAll(generateBag());
	}

	private Deque<Tetrominos> generateBag() {
		Deque<Tetrominos> b = new ArrayDeque<>();
		boolean[] isSeen = new boolean[7];
		int numSeen = 0;
		int i = (int) (model.length * Math.random());
		while (!isSeen[i]) {
			b.add(model[i]);
			isSeen[i] = true;
			i = (int) (model.length * Math.random());
			numSeen++;
			while (isSeen[i] && numSeen < 7)
				i = (int) (model.length * Math.random());
		}
		return b;
	}
}
