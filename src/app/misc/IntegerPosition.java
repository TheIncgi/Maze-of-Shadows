package app.misc;

public class IntegerPosition {
	int x, y;

	public IntegerPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	

	

	public void addToSelf(IntegerPosition b) {
		x += b.x;
		y += b.y;
	}
	

	public DoublePosition add(double x, double y){
		return new DoublePosition(this.x+x, this.y+y);
	}
	public IntegerPosition add(int x, int y){
		return new IntegerPosition(this.x+x, this.y+y);
	}
	
	public DoublePosition toDoublePos(){
		return new DoublePosition(x,y);
	}

	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntegerPosition other = (IntegerPosition) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("IntegerPos (%d, %d) Hash: %d", x, y, hashCode());
	}
}
