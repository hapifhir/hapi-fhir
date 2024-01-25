package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import com.google.common.hash.HashCode;
import jakarta.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

// LUKETODO:  javadoc
public class ResourceHistoryState {
	@Nullable
	private final String myResourceText;

	@Nullable
	private final byte[] myResourceBinary;

	private final ResourceEncodingEnum myEncoding;
	private final HashCode myHashCode;

	public ResourceHistoryState(
			@Nullable String theResourceText,
			@Nullable byte[] theResourceBinary,
			ResourceEncodingEnum theEncoding,
			HashCode theHashCode) {
		myResourceText = theResourceText;
		myResourceBinary = theResourceBinary;
		myEncoding = theEncoding;
		myHashCode = theHashCode;
	}

	@Nullable
	public String getResourceText() {
		return myResourceText;
	}

	@Nullable
	public byte[] getResourceBinary() {
		return myResourceBinary;
	}

	public ResourceEncodingEnum getEncoding() {
		return myEncoding;
	}

	public HashCode getHashCode() {
		return myHashCode;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}
		ResourceHistoryState that = (ResourceHistoryState) theO;
		return Objects.equals(myResourceText, that.myResourceText)
				&& Arrays.equals(myResourceBinary, that.myResourceBinary)
				&& myEncoding == that.myEncoding
				&& Objects.equals(myHashCode, that.myHashCode);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(myResourceText, myEncoding, myHashCode);
		result = 31 * result + Arrays.hashCode(myResourceBinary);
		return result;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ResourceHistoryState.class.getSimpleName() + "[", "]")
				.add("myResourceText='" + myResourceText + "'")
				.add("myResourceBinary=" + Arrays.toString(myResourceBinary))
				.add("myEncoding=" + myEncoding)
				.add("myHashCode=" + myHashCode)
				.toString();
	}
}
