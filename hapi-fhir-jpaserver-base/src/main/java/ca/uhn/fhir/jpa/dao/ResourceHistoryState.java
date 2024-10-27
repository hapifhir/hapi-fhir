/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import com.google.common.hash.HashCode;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * POJO to contain the results of {@link ResourceHistoryCalculator#calculateResourceHistoryState(IBaseResource, ResourceEncodingEnum, List)}
 */
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
