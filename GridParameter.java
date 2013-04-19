import javax.swing.JFrame;
import javax.swing.JLabel;

public class GridParameter {
	private JFrame frame;
	private JLabel score;
	private JLabel time;
	private JLabel holdPreview;
	private JLabel[] queue;
	private JLabel lines;
	private JLabel level;
	private JLabel action;
	private JLabel goal;

	public GridParameter() {
	}

	public boolean hasNullFields() {
		return frame == null || score == null || time == null
				|| holdPreview == null || queue == null || lines == null
				|| level == null || action == null || goal == null;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JLabel getScore() {
		return score;
	}

	public void setScore(JLabel score) {
		this.score = score;
	}

	public JLabel getTime() {
		return time;
	}

	public void setTime(JLabel time) {
		this.time = time;
	}

	public JLabel getHoldPreview() {
		return holdPreview;
	}

	public void setHoldPreview(JLabel holdPreview) {
		this.holdPreview = holdPreview;
	}

	public JLabel[] getQueue() {
		return queue;
	}

	public void setQueue(JLabel[] queue) {
		this.queue = queue;
	}

	public JLabel getLines() {
		return lines;
	}

	public void setLines(JLabel lines) {
		this.lines = lines;
	}

	/**
	 * @return the level
	 */
	public JLabel getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(JLabel level) {
		this.level = level;
	}

	/**
	 * @return the action
	 */
	public JLabel getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(JLabel action) {
		this.action = action;
	}

	public JLabel getGoal() {
		return goal;
	}

	public void setGoal(JLabel goal) {
		this.goal = goal;
	}
}