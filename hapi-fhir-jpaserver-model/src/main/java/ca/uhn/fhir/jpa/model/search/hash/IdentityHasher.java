package ca.uhn.fhir.jpa.model.search.hash;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Arrays;
import java.util.Objects;

/**
 * Responsible for computing the hash for a particular type of objects.
 * The hash computation will include all the Integer, Boolean and String values, including arrays of the same.
 * The used hashing algorithm is {@link Hashing#murmur3_128(int)}.
 */
public class IdentityHasher {
	/**
	 * Don't change this without careful consideration. You will break existing hashes!
	 */
	private static final HashFunction HASH_FUNCTION = Hashing.murmur3_128(0);

	/**
	 * Don't make this public 'cause nobody better be able to modify it!
	 */
	private static final byte[] DELIMITER_BYTES = "|".getBytes(Charsets.UTF_8);

	public interface IHashIdentity {}

	/**
	 * The following method can be made public and moved out if any other functionality needs similar hashing.
	 * @param bean the hash identity object holding the information which needs to be hashed.
	 * @return the hash value
	 */
	@SuppressWarnings("UnstableApiUsage")
	static long hash(IHashIdentity bean) {
		Hasher hasher = HASH_FUNCTION.newHasher();

		Class<? extends IHashIdentity> targetClass = bean.getClass();
		try {
			Arrays.stream(Introspector.getBeanInfo(targetClass).getPropertyDescriptors())
					.filter(pd -> Objects.nonNull(pd.getReadMethod()))
					.forEach(pd -> {
						try {
							Object value = pd.getReadMethod().invoke(bean);
							hashValue(value, hasher);
						} catch (Exception e) {
							throw new RuntimeException(
									Msg.code(2510) + "Failed to access " + targetClass + "#" + pd.getName(), e);
						}
					});
		} catch (IntrospectionException e) {
			throw new RuntimeException(Msg.code(2510) + "Failed to introspect " + targetClass, e);
		}
		return hasher.hash().asLong();
	}

	@SuppressWarnings("UnstableApiUsage")
	private static void hashValue(Object theValue, Hasher theHasher) {
		if (theValue == null) {
			return;
		}

		if (theValue instanceof Class) {
			return;
		}

		boolean useDelimiter = false;
		Object[] valueArray;
		if (theValue.getClass().isArray()) {
			valueArray = (Object[]) theValue;
			useDelimiter = true;
		} else {
			valueArray = new Object[] {theValue};
		}

		for (Object value : valueArray) {
			if (value == null) {
				theHasher.putByte((byte) 0);
			} else if (value instanceof String) {
				String stringValue = UrlUtil.escapeUrlParam((String) value);
				byte[] bytes = stringValue.getBytes(Charsets.UTF_8);
				theHasher.putBytes(bytes);
			} else if (value instanceof Integer) {
				Integer intValue = (Integer) value;
				theHasher.putInt(intValue);
			} else if (value instanceof Boolean) {
				Boolean boolValue = (Boolean) value;
				theHasher.putBoolean(boolValue);
			}
			if (useDelimiter) {
				theHasher.putBytes(DELIMITER_BYTES);
			}
		}
	}
}
