package a7;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ChessGameView extends JPanel implements Observer, MouseListener {

	private ChessGame game;
	private JLabel p1;
	private JLabel p2;
	private BoardSpot[][] boardspot;
	private ArrayList<MoveListener> listeners;
	private ChessPiece piece = null;

	public ChessGameView(ChessGame game) {

		this.game = game;
		game.addObserver(this);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		this.boardspot = new BoardSpot[8][8];
		listeners = new ArrayList<MoveListener>();
		piece = null;

		p2 = new JLabel("P2");
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 0.5;
		c.weighty = 0.5;
		this.add(p2, c);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				boardspot[i][j] = new BoardSpot(this.getName(),
						new ChessPosition(j, (7 - i)));
				c.fill = GridBagConstraints.BOTH;
				c.ipadx = 15;
				c.ipady = 15;
				c.gridx = j;
				c.gridy = i + 1;
				c.weightx = 0.5;
				c.weighty = 0.5;

				if (game.getBoard().getPieceAt(new ChessPosition(j, (7 - i))) != null) {

					String newMark = Character.toString(game.getBoard()
							.getPieceAt(new ChessPosition(j, (7 - i)))
							.getMark());

					this.boardspot[i][j].setText(newMark);

				} else {
					this.boardspot[i][j].setText(" ");
				}

				this.boardspot[i][j].setOpaque(true);
				this.boardspot[i][j].setPreferredSize(new Dimension(20, 20));

				if ((i + j) % 2 != 0)
					this.boardspot[i][j].setBackground(Color.gray);
				else
					this.boardspot[i][j].setBackground(Color.white);

				this.boardspot[i][j].addMouseListener(this);
				this.add(boardspot[i][j], c);
				this.boardspot[i][j]
						.setHorizontalAlignment(SwingConstants.CENTER);

			}

		}
		p1 = new JLabel("P1");
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 4;
		c.gridy = 9;
		this.add(p1, c);

	}

	public void addMoveListener(MoveListener l) {
		listeners.add(l);
	}

	public void removeMoveListener(MoveListener l) {
		listeners.remove(l);
	}

	@Override
	public void update(Observable o, Object arg) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (game.getBoard().getPieceAt(new ChessPosition(j, (7 - i))) != null) {

					String newMark = Character.toString(game.getBoard()
							.getPieceAt(new ChessPosition(j, (7 - i)))
							.getMark());
					boardspot[i][j].setText(newMark);
				} else {
					boardspot[i][j].setText(" ");
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		BoardSpot spot = (BoardSpot) e.getComponent();
		if (piece == null) {
			piece = game.getBoard().getPieceAt(spot.getPosition());

		} else {
			try {
				piece.moveTo(spot.getPosition());
				piece = null;

			} catch (IllegalMove e1) {
				System.out.println("Illegal Move");
			}
			update(game, spot);
		}
	}

	public void mouseEntered(MouseEvent e) {
		BoardSpot spot = (BoardSpot) e.getComponent();
		spot.setBorder(BorderFactory.createLoweredBevelBorder());
		// spot.setBorder(BorderFactory.createLineBorder(Color.green));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		BoardSpot spot = (BoardSpot) e.getComponent();
		spot.setBorder(null);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}

class BoardSpot extends JLabel {
	ChessPosition position;

	public BoardSpot(String labelName, ChessPosition position) {
		super(labelName);
		this.position = position;

	}

	public ChessPosition getPosition() {
		return position;
	}
}
