package instagram.utils;

public class ProfileUtils {

	public static int getNumberCount(String value) {
		value = value.replaceAll(",", "");
		boolean isDot = value.contains(".");
		value = value.replaceAll("\\.", "");
		if (isDot) {
			value = value.replaceAll("k", "00");
			value = value.replaceAll("m", "00000");
		} else {
			value = value.replaceAll("k", "000");
			value = value.replaceAll("m", "000000");
		}
		return Integer.parseInt(value);
	}

}
