package a7;
import java.util.Observable;

public abstract class ChessPiece extends Observable {

	private ChessPlayer owner;
	private ChessGame game;
	private ChessPosition position;
	protected char mark;
	

	protected ChessPiece(ChessPlayer owner, ChessGame game,
			ChessPosition init_position) {
		this.owner = owner;
		this.game = game;
		this.position = null;
		this.addObserver(game);
		
		game.getBoard().placePieceAt(this, init_position);
		
	}
	

	public ChessPlayer getOwner() {
		return owner;
	}

	public ChessGame getGame() {
		return game;
	}

	public ChessPosition getPosition() {
		return position;
	}

	public void setPosition(ChessPosition new_position) {
		position = new_position;
	}


	public void checkLineOfSight(ChessPosition start, ChessPosition end)
	  throws IllegalMove { 
			ChessGame g = getGame();
			ChessBoard b = g.getBoard();
			int dx = 0;
			int dy = 0;
			
			if (start.getY() > end.getY()) {
				dy = -1;
			}
			
			if (start.getY() < end.getY()) {
				dy = 1;
			}
			if (start.getX() > end.getX()) {
				dx = -1;
			}
			
			if (start.getX() < end.getX()) {
				dx = 1;
			}
			if (dx != 0 && dy !=0) {
				if (!(Math.abs(end.getX() - start.getX()) == Math.abs(end.getY()
						- start.getY()))) {
					throw new IllegalMove(this, start, end);
				}
			}
			ChessPosition cp = new ChessPosition((start.getX() + dx),
					(start.getY() + dy));
			
			while (!(cp.equals(end))) {
				if (b.getPieceAt(cp) != null)
					throw new IllegalMove(this, start, end);
				else
					cp = new ChessPosition((cp.getX() + dx), (cp.getY() + dy));
			}
	  }
	 

	public void moveTo(ChessPosition destination) throws IllegalMove {
		ChessGame g = getGame();
		ChessBoard b = g.getBoard();
		ChessPosition originalPosition = position;
		
		ChessMove m = new ChessMove (this, originalPosition, destination, b.getPieceAt(destination));

		if (destination.equals(position)) {
			throw new IllegalMove(this, position, destination);
		}
		if (!(b.getPieceAt(destination) == null || this.getOwner() != b.getPieceAt(destination).getOwner())) {
			throw new IllegalMove(this, position, destination);
		}
		b.removePieceFrom(destination);
		b.removePieceFrom(position);
		b.placePieceAt(this, destination);
		
		setChanged();
		notifyObservers(m);
		
	}

	public char getMark() {
		return mark;
	}
}

class Rook extends ChessPiece {
	public Rook(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'r';
		} else {
			mark = 'R';
		}
	}

	public void moveTo(ChessPosition destination) throws IllegalMove {
		if (destination.getX() == getPosition().getX()
				|| destination.getY() == getPosition().getY()) {
			checkLineOfSight(getPosition(), destination);
			super.moveTo(destination);
		} else 
			throw new IllegalMove(this, getPosition(), destination);
	}
	@Override
	public String toString() {
			return "rook";
		}
}

class Bishop extends ChessPiece {
	public Bishop(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'b';
		} else {
			mark = 'B';
		}
	}

	public void moveTo(ChessPosition destination) throws IllegalMove {
		if (Math.abs(destination.getX() - getPosition().getX()) == Math
				.abs(destination.getY() - getPosition().getY())) {
				checkLineOfSight(getPosition(), destination);
				super.moveTo(destination);
		} else 
			throw new IllegalMove(this, getPosition(), destination);
	}
	@Override
	public String toString() {
			return "bishop";
		}
}

class Knight extends ChessPiece {
	public Knight(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'n';
		} else {
			mark = 'N';
		}
	}
	public void moveTo(ChessPosition destination) throws IllegalMove {
	int dx = Math.abs(destination.getX() - getPosition().getX());
	int dy = Math.abs(destination.getY() - getPosition().getY());
	if (dx + dy == 3 && dx !=0 && dy != 0) {
		super.moveTo(destination);
	}else
		throw new IllegalMove(this, getPosition(), destination);
	}
	@Override
	public String toString() {
			return "knight";
		}
}

class Queen extends ChessPiece {
	public Queen(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'q';
		} else {
			mark = 'Q';
		}
	}
	public void moveTo(ChessPosition destination) throws IllegalMove {
		if (destination.getX() == getPosition().getX()
				|| destination.getY() == getPosition().getY()
				|| (Math.abs(destination.getX() - getPosition().getX()) == 
						Math.abs(destination.getY() - getPosition().getY()))) {
			checkLineOfSight(getPosition(), destination);
			super.moveTo(destination);
		} else 
			throw new IllegalMove(this, getPosition(), destination);
	}
	@Override
	public String toString() {
			return "queen";
		}
}

class King extends ChessPiece {
	public King(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'k';
		} else {
			mark = 'K';
		}
	}

	public void moveTo(ChessPosition destination) throws IllegalMove {
		if (Math.abs(destination.getX() - getPosition().getX()) <= 1
				&& Math.abs(destination.getY() - getPosition().getY()) <= 1) {
			super.moveTo(destination);
		} else {
			throw new IllegalMove(this, getPosition(), destination);
		}
	}
	@Override
	public String toString() {
			return "king";
		}
}

class Pawn extends ChessPiece {
	public Pawn(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'p';
		} else {
			mark = 'P';
		}
	}
	public void moveTo(ChessPosition destination) throws IllegalMove {
		ChessGame g = getGame();
		ChessBoard b = g.getBoard();
		ChessPlayer own = this.getOwner();
		ChessPiece dest = b.getPieceAt(destination);
		int py = this.getPosition().getY();
		int px = this.getPosition().getX();
		int dy = destination.getY();
		int dx = destination.getX();
		
		//pawn cannot move backwards
		if (!(own == g.getPlayer1() && dy > py || own == g.getPlayer2()&& dy < py)) {
			throw new IllegalMove(this, getPosition(), destination);
		}

		//piece blocking pawn movement
		if (dx == px && dest !=null) {
			throw new IllegalMove(this, getPosition(), destination);
		}
		
		//pawn capture
		if (dest != null && dest.getOwner() != own && Math.abs(dx - px) <= 1 && Math.abs(dy - py) <=1) {
			super.moveTo(destination);
			
		//pawn first move	
		}else if (py == 1 || py == 6 && dx == px && Math.abs(dy - py) <= 2) {
			super.moveTo(destination);	
			
		//normal pawn move	
		}else if (dest == null && dx == px && Math.abs(dy - py) == 1) {
			super.moveTo(destination);			
			} else {
				throw new IllegalMove(this, getPosition(), destination);}
			}
	@Override
	public String toString() {
			return "pawn";
		}
		
	}

		
