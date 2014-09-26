package cn.core.mobile.library.tools;

/**
 * 位置
 * 
 * @author Richard.Ma
 * 
 */
public class GpsUtils {

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double distance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378.137;
		return s;
	}

}
