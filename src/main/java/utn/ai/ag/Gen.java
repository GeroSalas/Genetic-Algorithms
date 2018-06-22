package utn.ai.ag;

/**
 * REINA
 */
public class Gen {

	private int row;
	private int column;
	private boolean free;
	
	public Gen(int row, int column) {
		this.row = row;
		this.column = column;
		this.free = true;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
	
}
