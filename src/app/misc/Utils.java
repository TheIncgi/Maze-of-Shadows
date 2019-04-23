package app.misc;

public class Utils {
	public static double clamp(double in, double low, double high) {
		return Math.max(low, Math.min(high, in));
	}
	public static boolean inRange(float x, float low, float high){
        return low <= x && x < high;
    }
	
}
