package app.misc;

public class DoublePosition {
	double x, y;

	public DoublePosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public int getFloorX() {
		return (int)Math.floor(x);
	}
	
	public void addToSelf(DoublePosition b) {
		x += b.x;
		y += b.y;
	}
	
	public int getFloorY() {
		return (int)Math.floor(y);
	}
	public DoublePosition add(int x, int y){
		return new DoublePosition(this.x+x, this.y+y);
	}
	public DoublePosition add(double x, double y){
		return new DoublePosition(this.x+x, this.y+y);
	}
	
	public IntegerPosition floorToIntPos(){
		return new IntegerPosition((int)Math.floor(x), (int)Math.floor(y));
	}
	
	public IntegerPosition toIntPos(){
		return new IntegerPosition((int)x, (int)y);
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		DoublePosition other = (DoublePosition) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("DoublePosition (%.2f, %.2f) Hash: %d", x, y, hashCode());
	}

	public DoublePosition divide(double d) {
		return new DoublePosition(x/d, y/d);
	}
}
