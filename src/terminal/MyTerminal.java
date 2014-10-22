package terminal;

public class MyTerminal {
	private static String info = "Terminal";

	public static String getInfo() {
		return info;
	}

	public static void setInfo(String info) {
		MyTerminal.info = info;
	}

	public static void addInfo(String info) {
		MyTerminal.info += "\n" + info;
	}
}
