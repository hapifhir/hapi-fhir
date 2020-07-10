package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.util.StopWatch;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.helger.commons.base64.Base64;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HashFunctionTester {
	byte[] bytes1 = "ABCDiohseoiey oisegoyi  loegsiyosg l".getBytes(Charsets.UTF_8);
	byte[] bytes2 = "sepyo  pyoiyi fdfff".getBytes(Charsets.UTF_8);
	byte[] bytes3 = "us".getBytes(Charsets.UTF_8);
	byte[] bytes4 = "f;pspus sgrygliy gfdygfio fdgxylxgfdyfgxloygfxdofgixyl yxxfly3ar3r8a3988".getBytes(Charsets.UTF_8);

	@Test
	public void testHashBenchmark() {

		test(Hashing.murmur3_128(), "murmur3_128 ");
		test(Hashing.sha256()     , "sha256      ");
		test(Hashing.sha384()     , "sha384      ");
		test(Hashing.sha512()     , "sha512      ");

	}

	public void test(HashFunction theHashFunction, String theName) {
		int loops = 10000;
		StopWatch sw = new StopWatch();
		String output = "";
		for (int i = 0; i < loops; i++) {
			Hasher hasher = theHashFunction.newHasher();
			hasher.putBytes(bytes1);
			hasher.putBytes(bytes2);
			hasher.putBytes(bytes3);
			hasher.putBytes(bytes4);
			output = Base64.encodeBytes(hasher.hash().asBytes());
		}
		ourLog.info("{} took {}ms for {} or {}/second to generate {} chars: {}", theName, sw.getMillis(), loops, sw.getThroughput(loops, TimeUnit.SECONDS), output.length(), output);
	}

	private static final Logger ourLog = LoggerFactory.getLogger(HashFunctionTester.class);
}
