/**
 *
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import tetromino.Tetromino;
import tetromino.Tetrominos;
import block.Block;
import block.Empty;

/**
 * The grid is stored in a 2 x 2 array, enumerating rows from bottom-to-top,
 * then left-to-right.
 *
 * @author David Xu
 */
@SuppressWarnings("serial")
public class Grid extends JComponent {
	private final JFrame frame;
	private final JLabel scoreStatus;
	private final JLabel time;
	private final JLabel holdPreview;
	private final JLabel[] queuePreview;
	private final JLabel linesStatus;
	private final JLabel levelStatus;
	private final JLabel goalStatus;

	private int score;
	private Tetrominos[] queue;
	private int lines;
	private int level = 1;
	private int goal = 5;

	private int interval = 1000; // Milliseconds between updates.
	private final Timer timer; // Each time timer fires we animate one step.

	private final Timer elapsed;
	private int minutes = 0;
	private double seconds = 0;

	private final Timer lockdownTimer;

	private final TetrominoFactory factory;
	private Tetromino current;
	private Tetromino hold;

	private final int lockdown = 500;

	private final Block[][] grid;

	private final KeyListener gameListener;

	final int WIDTH = Block.WIDTH * 10; // 240
	final int HEIGHT = Block.HEIGHT * 20 + Block.PARTIAL; // 484 = 24 * 20 + 4

	private final static Block emptyBlock = new block.Empty();

	private int maxHeight = 0;
	private int height = 0;

	private boolean wasLockdownRunning = false;
	private boolean isGameInProgress = false;
	private int comboLength;
	private int comboTotal;
	private int backToBackLength;
	private int comboLines;
	private final int LEVEL_TICK_INTERVAL = 65;

	/**
	 * @param status
	 *            TODO
	 * @param s
	 *            Score
	 * @param q
	 *            Queue of next pieces
	 */
	public Grid(GridParameter status) {
		if (status == null || status.hasNullFields())
			throw new InvalidParameterException(
					"Status parameter fields cannot be null.");
		frame = status.getFrame();
		scoreStatus = status.getScore();
		time = status.getTime();
		holdPreview = status.getHoldPreview();
		queuePreview = status.getQueue();
		linesStatus = status.getLines();
		levelStatus = status.getLevel();
		goalStatus = status.getGoal();

		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFocusable(true);

		timer = new Timer(interval, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.setInitialDelay(0);

		lockdownTimer = new Timer(lockdown, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lockdown();
			}
		});
		lockdownTimer.setRepeats(false);

		elapsed = new Timer(10, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				seconds += 0.01;
				if (seconds == 60) {
					minutes++;
					seconds = 0;
				}
				time.setText(String.format("%02d:%05.2f", minutes, seconds));
			}
		});

		grid = new Block[22][10];
		final Block empty = new Empty();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = empty;
			}
		}

		factory = new TetrominoFactory(grid);
		queue = factory.getInQueue(queuePreview.length);

		gameListener = new KeyAdapter() {

			private boolean isPaused;

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SHIFT:
					if (lockdownTimer.isRunning())
						lockdownTimer.stop();
					current.removeSprite();
					Tetromino temp = hold;
					hold = current;
					holdPreview.setIcon(new Icon() {

						@Override
						public void paintIcon(Component c, Graphics g, int x,
								int y) {
							if (hold == null)
								return;
							try {
								g.drawImage(
										ImageIO.read(new File(hold.type()
												.toString() + "_21.png")), 0,
										0, null);
							} catch (IOException e) {
								System.out
										.println("Could not generate hold preview for "
												+ current.type().toString()
												+ " tetromino.");
							}
						}

						@Override
						public int getIconWidth() {
							return 84;
						}

						@Override
						public int getIconHeight() {
							return 84;
						}
					});
					holdPreview.revalidate();
					holdPreview.repaint();
					if (temp == null) {
						current = factory.next();
						updateQueue();
						break;
					}
					current = temp;
					current.respawnSprite();
					updateQueue();
					break;
				case KeyEvent.VK_Z:
					current.rotateLeft();
					if (lockdownTimer.isRunning())
						lockdownTimer.restart();
					break;
				case KeyEvent.VK_X:
					current.rotateRight();
					if (lockdownTimer.isRunning())
						lockdownTimer.restart();
					break;
				case KeyEvent.VK_UP:
					current.softDrop();
					break;
				case KeyEvent.VK_DOWN:
					current.hardDrop();
					lockdown();
					break;
				case KeyEvent.VK_LEFT:
					current.moveLeft();
					if (lockdownTimer.isRunning())
						lockdownTimer.restart();
					break;
				case KeyEvent.VK_RIGHT:
					current.moveRight();
					if (lockdownTimer.isRunning())
						lockdownTimer.restart();
					break;
				case KeyEvent.VK_ESCAPE:
					isPaused ^= true;
					if (isPaused)
						pause();
					else
						resume();
				default:
					break;
				}
				repaint();
			}
		};
		addKeyListener(gameListener);
	}

	private void lockdown() {
		if (!current.isOnSurface()) {
			return;
		}
		boolean isTSpin = current.isTSpin();
		int linesCleared = clearLines();
		lines += linesCleared;
		linesStatus.setText(Integer.toString(lines));
		score += computeScore(linesCleared, isTSpin);
		scoreStatus.setText(Integer.toString(score));
		goal -= updateGoalLines(linesCleared, isTSpin);
		if (goal <= 0) {
			if (level == 15)
				win();
			level++;
			goal = level * 5;
			interval -= LEVEL_TICK_INTERVAL;
			timer.setDelay(interval);
			levelStatus.setText(Integer.toString(level));
		}
		goalStatus.setText(Integer.toString(goal));
		height = determineHeight();
		if (height > maxHeight)
			maxHeight = height;
		if (height >= 20) {
			repaint();
			lose();
			return;
		}
		repaint();
		current = factory.next();
		updateQueue();
		repaint();
		timer.restart();
		repaint();
	}

	private int computeScore(int linesCleared, boolean isTSpin) {
		int score = 0;
		if (linesCleared == 0) {
			if (isTSpin)
				score = 400 * level;
			comboLength = 0;
			comboLines = 0;
			comboTotal = 0;
			backToBackLength = 0;
			return 0;
		}
		comboLength++;
		if (comboLength > 1)
			comboLines += linesCleared;
		if (isTSpin) {
			switch (linesCleared) {
			case 1:
				score = 800 * level;
				score += comboTotal * backToBackLength / 2;
				break;
			case 2:
				score = 1200 * level;
				comboTotal += score;
				score += comboTotal * backToBackLength / 2;
				break;
			case 3:
				score = 1600 * level;
				comboTotal += score;
				score += comboTotal * backToBackLength / 2;
			}
			backToBackLength++;
		} else {
			if (linesCleared < 4)
				backToBackLength = 0;
			switch (linesCleared) {
			case 0:
				score = 0;
			case 1:
				score = 100 * level;
				break;
			case 2:
				score = 300 * level;
				break;
			case 3:
				score = 500 * level;
				break;
			case 4:
				score = 800 * level;
				comboTotal += score;
				score += comboTotal * backToBackLength / 2;
				backToBackLength++;
				break;
			}
		}
		score += (comboLength - 1) * 50 * (comboLength - 1) * level;
		return score;
	}

	// Call this method after computing score
	private int updateGoalLines(int linesCleared, boolean isTSpin) {
		int goalLines = 0;
		if (isTSpin) {
			switch (linesCleared) {
			case 0:
				goalLines = 4;
				break;
			case 1:
				goalLines = 8;
				break;
			case 2:
				goalLines = 12;
				break;
			case 3:
				goalLines = 16;
				break;
			}
		} else {
			switch (linesCleared) {
			case 0:
				break;
			case 1:
				goalLines = 1;
				break;
			case 2:
				goalLines = 3;
				break;
			case 3:
				goalLines = 5;
				break;
			case 4:
				goalLines = 8;
				break;
			}
		}
		goalLines += comboLines / 2;
		return goalLines;
	}

	private int determineHeight() {
		for (int i = 0; i < grid.length; i++)
			if (isEmptyLine(i))
				return i;
		return 22;
	}

	private boolean isEmptyLine(int y) {
		boolean hasBlock = false;
		for (int j = 0; j < grid[y].length; j++) {
			hasBlock ^= !grid[y][j].isEmpty();
			if (hasBlock)
				break;
		}
		return !hasBlock;
	}

	private int clearLines() {
		int cleared = 0;
		for (int i = 0; i < grid.length; i++) {
			boolean hasHole = false;
			for (int j = 0; j < grid[i].length; j++) {
				hasHole ^= grid[i][j].isEmpty();
				if (hasHole)
					break;
			}
			if (!hasHole) {
				for (int y = i; y < 21; y++)
					for (int j = 0; j < grid[i].length; j++)
						grid[y][j] = grid[y + 1][j];
				i--;
				cleared++;
			}
		}
		return cleared;
	}

	private void tick() {
		current.moveDown();
		if (current.isOnSurface()) {
			lockdownTimer.start();
		}
		repaint();
	}

	private void win() {
		stop();
		JOptionPane.showMessageDialog(frame, "Well you're cool...", "Winner!",
				JOptionPane.PLAIN_MESSAGE);
	}

	private void lose() {
		stop();
		JOptionPane.showMessageDialog(frame, "You lost.", "A loser is you!",
				JOptionPane.PLAIN_MESSAGE);
	}

	private void stop() {
		elapsed.stop();
		timer.stop();
		lockdownTimer.stop();
		removeKeyListener(gameListener);
		isGameInProgress = false;
	}

	public void start() {
		requestFocusInWindow();
		isGameInProgress = true;
		elapsed.start();
		timer.start();
		current = factory.next();
		updateQueue();
	}

	/**
	 *
	 */
	private void updateQueue() {
		class MutableInt {
			int value;

			MutableInt(int i) {
				value = i;
			}
		}

		queue = factory.getInQueue(queuePreview.length);
		for (final MutableInt n = new MutableInt(0); n.value < queue.length; n.value++) {
			queuePreview[n.value].setIcon(new Icon() {
				private final int i = n.value;

				@Override
				public void paintIcon(Component c, Graphics g, int x, int y) {
					try {
						if (i == 0)
							g.drawImage(
									ImageIO.read(new File(queue[i].toString()
											+ "_21.png")), 0, 0, null);
						else
							g.drawImage(
									ImageIO.read(new File(queue[i].toString()
											+ "_18.png")), 0, 0, null);
					} catch (IOException e) {
						System.out.println("Could not generate next queue "
								+ "preview for " + queue[i].toString()
								+ " tetromino.");
					}

				}

				@Override
				public int getIconWidth() {
					if (i == 0)
						return 84;
					return 72;
				}

				@Override
				public int getIconHeight() {
					if (i == 0)
						return 84;
					return 72;
				}
			});
		}
	}

	public void reset() {
		stop();
		reinitialize();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = emptyBlock;
			}
		}
		addKeyListener(gameListener);
		frame.repaint();
		start();
	}

	/**
	 *
	 */
	private void reinitialize() {
		interval = 1000;
		current = null;
		queue = null;
		hold = null;
		lines = 0;
		minutes = 0;
		seconds = 0;
		time.setText("00:00.00");
		factory.reset();
		level = 1;
		levelStatus.setText("1");
		goal = 5;
		goalStatus.setText("5");
	}

	public void pause() {
		elapsed.stop();
		timer.stop();
		wasLockdownRunning = lockdownTimer.isRunning();
		if (wasLockdownRunning)
			lockdownTimer.stop();
	}

	public void resume() {
		if (isGameInProgress) {
			elapsed.start();
			timer.start();
		}
		if (wasLockdownRunning)
			lockdownTimer.start();
		wasLockdownRunning &= false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		try {
			g.drawImage(ImageIO.read(new File("background.png")), 0, 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out
					.println("Internal error: Could not load background image."
							+ e.getMessage());
		}
		// Do not draw row 22
		// Draw partial row 21
		for (int j = 0; j < 10; j++)
			grid[20][j].drawPartial(g, j);
		for (int i = 0; i < 20; i++)
			for (int j = 0; j < 10; j++)
				grid[i][j].draw(g, j, i);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
}