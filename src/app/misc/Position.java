package app.misc;

public class Position<T extends Number> {
	T x, y;

	public Position(T x, T y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(T x, T y) {
		this.x = x;
		this.y = y;
	}
	
	public T getX() {
		return x;
	}

	public void setX(T x) {
		this.x = x;
	}

	public T getY() {
		return y;
	}

	public void setY(T y) {
		this.y = y;
	}
	
	public int getFloorX() {
		return (int)Math.floor(x.doubleValue());
	}

	public int getFloorY() {
		return (int)Math.floor(y.doubleValue());
	}
	public Position<Integer> iAdd(int x, int y){
		if(this.x instanceof Double)
			return new Position<Integer>((int)Math.floor(this.x.doubleValue())+x, (int)Math.floor(this.y.doubleValue())+y);
		return new Position<Integer>(this.x.intValue()+x, this.y.intValue()+y);
	}
	public Position<Double> dAdd(double x, double y){
		return new Position<Double>(this.x.doubleValue()+x, this.y.doubleValue()+y);
	}
	
	public Position<Integer> floorToIntPos(){
		return new Position<Integer>((int)Math.floor(x.doubleValue()), (int)Math.floor(y.doubleValue()));
	}
	
	public Position<Integer> toIntPos(){
		return new Position<Integer>(x.intValue(), y.intValue());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		Position other = (Position) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("Position<%s> (%.2f, %.2f) Hash: %d", x.getClass().toString(), x.doubleValue(), y.doubleValue(), hashCode());
	}
}
