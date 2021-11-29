public class BadClass {
	public void init() {
		throw new RuntimeException(Msg.code(1), "good");
		throw new RuntimeException("nocode");
		throw new RuntimeException(Msg.code(2), "duplicate code");
		throw new RuntimeException(Msg.code(2), "duplicate code");
	}
}
