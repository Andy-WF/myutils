package utils;

public class TestUse {

	public static void main(String[] args) {
		
		// 这就拿到了，按需使用
		String password = ConfigUtil.getValueByKey("DEFAULT_PASSWORD");

		// 设置默认管理员密码
		user.setUserpswd(ConfigUtil.getValueByKey("DEFAULT_PASSWORD"));

	}

}
