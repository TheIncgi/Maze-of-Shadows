package app.misc;

public class Utils {
	public static double clamp(double in, double low, double high) {
		return Math.max(low, Math.min(high, in));
	}
	
	
}
