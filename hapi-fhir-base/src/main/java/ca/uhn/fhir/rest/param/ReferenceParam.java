package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.CoverageIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.math.BigDecimal;

import static ca.uhn.fhir.model.primitive.IdDt.isValidLong;
import static org.apache.commons.lang3.StringUtils.*;

public class ReferenceParam extends BaseParam /*implements IQueryParameterType*/ {

	private String myChain;
	private String myResourceType;
	private String myBaseUrl;
	private String myValue;
	private String myIdPart;

	/**
	 * Constructor
	 */
	public ReferenceParam() {
		super();
	}

	/**
	 * Constructor
	 */
	public ReferenceParam(String theValue) {
		setValueAsQueryToken(null, null, null, theValue);
	}

	/**
	 * Constructor
	 */
	public ReferenceParam(String theChain, String theValue) {
		setValueAsQueryToken(null, null, null, theValue);
		setChain(theChain);
	}

	/**
	 * Constructor
	 */
	public ReferenceParam(String theResourceType, String theChain, String theValue) {
		String qualifier = "";
		if (isNotBlank(theResourceType)) {
			qualifier = ":" + theResourceType;
		}
		if (isNotBlank(theChain)) {
			qualifier = qualifier + "." + theChain;
		}

		setValueAsQueryToken(null, null, qualifier, theValue);
	}

	@Override
	String doGetQueryParameterQualifier() {
		StringBuilder b = new StringBuilder();
		if (isNotBlank(myChain)) {
			if (isNotBlank(getResourceType())) {
				b.append(':');
				b.append(getResourceType());
			}
			b.append('.');
			b.append(myChain);
		}
		if (b.length() != 0) {
			return b.toString();
		}
		return null;
	}

	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		if (isBlank(getResourceType())) {
			return myValue; // e.g. urn:asdjd or 123 or cid:wieiuru or #1
		} else {
			if (isBlank(getChain()) && isNotBlank(getResourceType())) {
				return getResourceType() + "/" + getIdPart();
			}
			return myValue;
		}
	}

	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		String q = theQualifier;
		if (isNotBlank(q)) {
			if (q.startsWith(":")) {
				int nextIdx = q.indexOf('.');
				if (nextIdx != -1) {
					myChain = q.substring(nextIdx + 1);
					myResourceType = q.substring(1, nextIdx);
				} else {
					myChain = null;
					myResourceType = q.substring(1);
				}

				myValue = theValue;
				myIdPart = theValue;

				IdDt id = new IdDt(theValue);
				if (!id.hasBaseUrl() && id.hasIdPart() && id.hasResourceType()) {
					if (id.getResourceType().equals(myResourceType)) {
						myIdPart = id.getIdPart();
					}
				}

			} else if (q.startsWith(".")) {
				myChain = q.substring(1);
				myResourceType = null;
				myValue = theValue;
				myIdPart = theValue;
			}
		} else {
			myChain = null;
			myValue = theValue;
			IdDt id = new IdDt(theValue);
			myResourceType = id.getResourceType();
			myIdPart = id.getIdPart();
			myBaseUrl = id.getBaseUrl();
		}

	}


	@CoverageIgnore
	public String getBaseUrl() {
		return myBaseUrl;
	}


	public String getChain() {
		return myChain;
	}

	public ReferenceParam setChain(String theChain) {
		myChain = theChain;
		return this;
	}

	@CoverageIgnore
	public String getIdPart() {
		return myIdPart;
	}

	@CoverageIgnore
	public BigDecimal getIdPartAsBigDecimal() {
		return new IdDt(myValue).getIdPartAsBigDecimal();
	}

	@CoverageIgnore
	public Long getIdPartAsLong() {
		return new IdDt(myValue).getIdPartAsLong();
	}

	public String getResourceType() {
		if (isNotBlank(myResourceType)) {
			return myResourceType;
		}
		if (isBlank(myChain)) {
			return new IdDt(myValue).getResourceType();
		}
		return null;
	}

	public Class<? extends IBaseResource> getResourceType(FhirContext theCtx) {
		if (isBlank(getResourceType())) {
			return null;
		}
		return theCtx.getResourceDefinition(getResourceType()).getImplementingClass();
	}

	public String getValue() {
		return myValue;
	}

	public ReferenceParam setValue(String theValue) {
		IdDt id = new IdDt(theValue);
		String qualifier= null;
		if (id.hasResourceType()) {
			qualifier = ":" + id.getResourceType();
		}
		setValueAsQueryToken(null, null, qualifier, id.getIdPart());
		return this;
	}

	public boolean hasResourceType() {
		return isNotBlank(myResourceType);
	}

	@Override
	protected boolean isSupportsChain() {
		return true;
	}

	/**
	 * Returns a new param containing the same value as this param, but with the type copnverted
	 * to {@link DateParam}. This is useful if you are using reference parameters and want to handle
	 * chained parameters of different types in a single method.
	 * <p>
	 * See <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_operations.html#dynamic_chains">Dynamic Chains</a>
	 * in the HAPI FHIR documentation for an example of how to use this method.
	 * </p>
	 */
	public DateParam toDateParam(FhirContext theContext) {
		DateParam retVal = new DateParam();
		retVal.setValueAsQueryToken(theContext, null, null, getValueAsQueryToken(theContext));
		return retVal;
	}

	/**
	 * Returns a new param containing the same value as this param, but with the type copnverted
	 * to {@link NumberParam}. This is useful if you are using reference parameters and want to handle
	 * chained parameters of different types in a single method.
	 * <p>
	 * See <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_operations.html#dynamic_chains">Dynamic Chains</a>
	 * in the HAPI FHIR documentation for an example of how to use this method.
	 * </p>
	 */
	public NumberParam toNumberParam(FhirContext theContext) {
		NumberParam retVal = new NumberParam();
		retVal.setValueAsQueryToken(theContext, null, null, getValueAsQueryToken(theContext));
		return retVal;
	}

	/**
	 * Returns a new param containing the same value as this param, but with the type copnverted
	 * to {@link QuantityParam}. This is useful if you are using reference parameters and want to handle
	 * chained parameters of different types in a single method.
	 * <p>
	 * See <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_operations.html#dynamic_chains">Dynamic Chains</a>
	 * in the HAPI FHIR documentation for an example of how to use this method.
	 * </p>
	 */
	public QuantityParam toQuantityParam(FhirContext theContext) {
		QuantityParam retVal = new QuantityParam();
		retVal.setValueAsQueryToken(theContext, null, null, getValueAsQueryToken(theContext));
		return retVal;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		if (isNotBlank(myChain)) {
			b.append("chain", myChain);
		}
		b.append("value", getValue());
		return b.build();
	}

	/**
	 * Returns a new param containing the same value as this param, but with the type copnverted
	 * to {@link StringParam}. This is useful if you are using reference parameters and want to handle
	 * chained parameters of different types in a single method.
	 * <p>
	 * See <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_operations.html#dynamic_chains">Dynamic Chains</a>
	 * in the HAPI FHIR documentation for an example of how to use this method.
	 * </p>
	 */
	public StringParam toStringParam(FhirContext theContext) {
		StringParam retVal = new StringParam();
		retVal.setValueAsQueryToken(theContext, null, null, getValueAsQueryToken(theContext));
		return retVal;
	}

	/**
	 * Returns a new param containing the same value as this param, but with the type copnverted
	 * to {@link TokenParam}. This is useful if you are using reference parameters and want to handle
	 * chained parameters of different types in a single method.
	 * <p>
	 * See <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_operations.html#dynamic_chains">Dynamic Chains</a>
	 * in the HAPI FHIR documentation for an example of how to use this method.
	 * </p>
	 */
	public TokenParam toTokenParam(FhirContext theContext) {
		TokenParam retVal = new TokenParam();
		retVal.setValueAsQueryToken(theContext, null, null, getValueAsQueryToken(theContext));
		return retVal;
	}

	public boolean isIdPartValidLong() {
		return isValidLong(getIdPart());
	}
}
