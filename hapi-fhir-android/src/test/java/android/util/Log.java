package android.util;

public class Log {

	public static final int VERBOSE = 2;
	public static final int DEBUG = 3;
	public static final int INFO = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static final int ASSERT = 7;

	public static boolean isLoggable(String theName, int thePriority) {
		return true;
	}

	public static boolean isLoggable(int thePriority) {
		return true;
	}
	
	public static int println(int thePriority, String theName, String theMessage) {
		System.out.println("[" + theName + "] " + theMessage);
		return 0;
	}
	
}
