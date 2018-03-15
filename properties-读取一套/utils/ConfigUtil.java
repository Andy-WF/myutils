package utils;


import java.util.ResourceBundle;

public class ConfigUtil {

	private static ResourceBundle rb =
		ResourceBundle.getBundle("config");
	
	public static String getValueByKey(String key) {
		return rb.getString(key);
	}
}
