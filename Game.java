/**
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * @author David
 *
 */
public class Game implements Runnable {

	protected static final String VERSION_NUMBER = "1.0";

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void run() {
		Font f = new Font(null, Font.PLAIN, 24);
		Font fBig = null;
		final Font ff;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File(
					"Pipe_Dream_by_TRIFORCE89.ttf"));
			f = f.deriveFont(Font.PLAIN, 24);
			fBig = f.deriveFont(Font.PLAIN, 36);
		} catch (FontFormatException e1) {
			System.out.println("Unable to load custom font." + e1.getMessage());
		} catch (IOException e1) {
			System.out.println("Unable to load custom font." + e1.getMessage());
		}
		// For use in inner class
		ff = f;

		final GridParameter gp = new GridParameter();

		// Top-level frame
		final JFrame frame = new JFrame("Tetris");
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setFont(f);
		frame.getContentPane().setFont(f);
		frame.setResizable(false);
		frame.setLocation(690, 200);

		JPanel holdPanel = new JPanel();
		holdPanel.setOpaque(false);
		frame.getContentPane().add(holdPanel, BorderLayout.WEST);
		holdPanel.setPreferredSize(new Dimension(100, 10));
		holdPanel.setBounds(new Rectangle(0, 0, 50, 50));
		holdPanel.setSize(new Dimension(50, 50));
		holdPanel.setLayout(new BoxLayout(holdPanel, BoxLayout.Y_AXIS));

		JLabel holdLabel = new JLabel("Hold:");
		holdLabel.setFont(f);
		holdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		holdPanel.add(holdLabel);

		JLabel hold = new JLabel("");
		hold.setFont(f);
		hold.setAlignmentX(Component.CENTER_ALIGNMENT);
		hold.setPreferredSize(new Dimension(84, 84));
		hold.setMinimumSize(new Dimension(84, 84));
		holdLabel.setLabelFor(hold);
		holdPanel.add(hold);
		gp.setHoldPreview(hold);

		JLabel levelLabel = new JLabel("Level:");
		levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		holdPanel.add(levelLabel);
		levelLabel.setFont(f);

		JLabel level = new JLabel("1");
		level.setAlignmentX(Component.CENTER_ALIGNMENT);
		levelLabel.setLabelFor(level);
		level.setForeground(Color.RED);
		holdPanel.add(level);
		level.setFont(fBig);
		gp.setLevel(level);

		JLabel goalLabel = new JLabel("Goal:");
		goalLabel.setFont(f);
		goalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		holdPanel.add(goalLabel);

		JLabel goal = new JLabel("5");
		goal.setForeground(Color.RED);
		goal.setFont(fBig);
		goal.setAlignmentX(Component.CENTER_ALIGNMENT);
		holdPanel.add(goal);

		// Game status
		JPanel scorePanel = new JPanel();
		scorePanel.setOpaque(false);
		frame.getContentPane().add(scorePanel, BorderLayout.NORTH);

		JLabel scoreLabel = new JLabel("Score");
		scoreLabel.setFont(f);
		scorePanel.add(scoreLabel);

		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setMinimumSize(new Dimension(100, 100));
		scorePanel.add(horizontalGlue);

		JLabel score = new JLabel("0");
		score.setForeground(Color.RED);
		score.setFont(f);
		scorePanel.add(score);
		gp.setScore(score);

		JPanel status = new JPanel();
		status.setOpaque(false);
		frame.getContentPane().add(status, BorderLayout.EAST);
		status.setPreferredSize(new Dimension(200, 10));
		status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));

		JPanel queuePanel = new JPanel();
		queuePanel.setOpaque(false);
		status.add(queuePanel);
		queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));

		JLabel queueFirstLabel = new JLabel("Next:");
		queueFirstLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		queuePanel.add(queueFirstLabel);
		queueFirstLabel.setFont(f);

		JLabel queue0 = new JLabel("");
		queueFirstLabel.setLabelFor(queue0);

		queuePanel.add(queue0);
		queue0.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel queue1 = new JLabel("");
		queuePanel.add(queue1);
		queue1.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel queue2 = new JLabel("");
		queuePanel.add(queue2);
		queue2.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel queue3 = new JLabel("");
		queuePanel.add(queue3);
		queue3.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel queue4 = new JLabel("");
		queuePanel.add(queue4);
		queue4.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel timePanel = new JPanel();
		status.add(timePanel);
		timePanel.setOpaque(false);
		timePanel.setMinimumSize(new Dimension(100, 10));
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));

		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setFont(f);
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timePanel.add(timeLabel);

		Component horizontalGlueTime = Box.createHorizontalGlue();
		timePanel.add(horizontalGlueTime);

		JLabel time = new JLabel("00:00.00");
		time.setForeground(Color.BLUE);
		time.setFont(f);
		time.setAlignmentX(Component.CENTER_ALIGNMENT);
		timePanel.add(time);
		gp.setTime(time);

		JPanel linesPanel = new JPanel();
		linesPanel.setOpaque(false);
		status.add(linesPanel);
		linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.X_AXIS));

		JLabel linesLabel = new JLabel("Lines:");
		linesLabel.setFont(f);
		linesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		linesPanel.add(linesLabel);

		Component horizontalGlueLines = Box.createHorizontalGlue();
		linesPanel.add(horizontalGlueLines);

		JLabel lines = new JLabel("0");
		lines.setForeground(Color.BLUE);
		lines.setFont(f);
		lines.setAlignmentX(Component.CENTER_ALIGNMENT);
		linesLabel.setLabelFor(lines);
		linesPanel.add(lines);
		gp.setLines(lines);

		JPanel actionPanel = new JPanel();
		actionPanel.setOpaque(false);
		status.add(actionPanel);

		JLabel action = new JLabel("");
		action.setFont(f);
		actionPanel.add(action);

		final JLabel[] queue = { queue0, queue1, queue2, queue3, queue4 };
		gp.setFrame(frame);
		gp.setQueue(queue);
		gp.setAction(action);
		gp.setGoal(goal);

		// Make the game grid
		final Grid game = new Grid(gp);
		frame.getContentPane().add(game, BorderLayout.CENTER);
		game.setForeground(Color.BLACK);
		game.setBackground(Color.LIGHT_GRAY);
		game.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		game.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				game.pause();
			}

			@Override
			public void focusGained(FocusEvent e) {
				game.resume();
			}
		});

		game.setForeground(Color.BLACK);
		game.setBackground(Color.LIGHT_GRAY);
		game.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,
				null, null));

		game.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				game.pause();
			}

			@Override
			public void focusGained(FocusEvent e) {
				game.resume();
			}
		});

		// Menu bar
		final JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		bar.add(file);

		JMenuItem newGame = new JMenuItem("New game", KeyEvent.VK_N);
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				game.reset();
			}
		});
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		file.add(newGame);
		file.addSeparator();
		JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		file.add(exit);

		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		bar.add(help);

		JMenuItem instructions = new JMenuItem("Instructions", KeyEvent.VK_I);
		instructions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		instructions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Icon icon = new Icon() {

					@Override
					public void paintIcon(Component arg0, Graphics g, int x,
							int y) {
						try {
							g.drawImage(
									ImageIO.read(new File("tetris_love.jpg")),
									0, 0, null);
						} catch (IOException e) {
							System.out.println("Internal error: "
									+ e.getMessage());
						}

					}

					@Override
					public int getIconWidth() {
						return 120;
					}

					@Override
					public int getIconHeight() {
						return 80;
					}
				};

				JLabel text = new JLabel();
				text.setText("<html><table>"
						+ "<tr><td>[Z], [X]</td><td>Rotation</td></tr>"
						+ "<tr><td>[DOWN_ARROW]</td><td>Soft drop</td></tr>"
						+ "<tr><td>[UP_ARROW]</td><td>Hard drop</td></tr>"
						+ "<tr><td>[SHIFT]</td><td>Hold</td></tr>"
						+ "<tr><td>[ESC]</td><td>(Un-)Pause</td></tr>"
						+ "</table>");
				JOptionPane.showMessageDialog(frame, text, "tetris help...",
						JOptionPane.INFORMATION_MESSAGE, icon);
			}
		});
		help.add(instructions);
		help.addSeparator();

		JMenuItem about = new JMenuItem("About...", KeyEvent.VK_A);
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame,
						"Tetris\n\n"
						+ "Version: " + VERSION_NUMBER + "\n"
						+ "Author: David Xu\n\n"
						+ "\u00a9 2013",
						"About...", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		help.add(about);

		frame.setJMenuBar(bar);

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		// Start the game running
		game.start();

	}

	/**
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

}
