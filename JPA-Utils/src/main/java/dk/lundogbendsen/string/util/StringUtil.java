package dk.lundogbendsen.string.util;

public final class StringUtil {

	// Private constructor and final class prevents instantiation
	private StringUtil() {
	}

	public static void prettyPrintHeadline(String headLine) {
		String msg = "\n**********************************************************";
		msg += "\n* " + headLine;
		msg += "\n**********************************************************";
		System.out.println(msg);
	}
}
