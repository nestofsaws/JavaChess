package a7;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

public class ChessGame extends Observable implements Observer {

	private ChessBoard board;
	private ChessPlayer player1;
	private ChessPlayer player2;

	ArrayList<ChessMove> log;

	public ChessGame(ChessPlayer player1, ChessPlayer player2) {
		this.player1 = player1;
		this.player2 = player2;
		board = new ChessBoard();

		new Rook(player1, this, new ChessPosition(0, 0));
		new Knight(player1, this, new ChessPosition(1, 0));
		new Bishop(player1, this, new ChessPosition(2, 0));
		new Queen(player1, this, new ChessPosition(3, 0));
		new King(player1, this, new ChessPosition(4, 0));
		new Bishop(player1, this, new ChessPosition(5, 0));
		new Knight(player1, this, new ChessPosition(6, 0));
		new Rook(player1, this, new ChessPosition(7, 0));

		for (int i = 0; i < 8; i++) {
			new Pawn(player1, this, new ChessPosition(i, 1));
		}

		new Rook(player2, this, new ChessPosition(0, 7));
		new Knight(player2, this, new ChessPosition(1, 7));
		new Bishop(player2, this, new ChessPosition(2, 7));
		new Queen(player2, this, new ChessPosition(3, 7));
		new King(player2, this, new ChessPosition(4, 7));
		new Bishop(player2, this, new ChessPosition(5, 7));
		new Knight(player2, this, new ChessPosition(6, 7));
		new Rook(player2, this, new ChessPosition(7, 7));

		for (int i = 0; i < 8; i++) {
			new Pawn(player2, this, new ChessPosition(i, 6));
		}
		log = new ArrayList<ChessMove>();
	}

	public ChessPlayer getPlayer1() {
		return player1;
	}

	public ChessPlayer getPlayer2() {
		return player2;
	}

	public ChessBoard getBoard() {
		return board;
	}

	@Override
	public void update(Observable o, Object arg) {
		log.add((ChessMove) arg);
		setChanged();
		notifyObservers(arg);

	}

	public int getLogSize() {
		return log.size();
	}

	public ChessMove[] getMoves(int n) {

		ChessMove[] moves = null;
		int lSize = getLogSize();

		moves = new ChessMove[lSize];
		moves = log.toArray(moves);

		if (n > 0 && n < getLogSize()) {
			List<ChessMove> logs = log.subList(0, n);
			moves = new ChessMove[n];
			moves = logs.toArray(moves);

			return moves;
		}

		else if (n < 0 && Math.abs(n) <= getLogSize()) {
			List<ChessMove> logs = log.subList(getLogSize() - Math.abs(n),
					getLogSize());
			moves = new ChessMove[Math.abs(n)];
			moves = logs.toArray(moves);

			return moves;
		}

		else {

			return moves;
		}
	}

	public void undo() {
		try {
			ChessPiece current = log.get(getLogSize() - 1).getPiece();
			ChessPiece captured = log.get(getLogSize() - 1).getCaptured();

			ChessPosition original = log.get(getLogSize() - 1).getFrom();
			ChessPosition destination = log.get(getLogSize() - 1).getTo();

			this.getBoard().removePieceFrom(destination);
			this.getBoard().placePieceAt(current, original);

			if (captured != null) {
				this.getBoard().placePieceAt(captured, destination);
			}
			log.remove(getLogSize() - 1);

			setChanged();
			notifyObservers();

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("No move to undo!!");
		}
	}
}
