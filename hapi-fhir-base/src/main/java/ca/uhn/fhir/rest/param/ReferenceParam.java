package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import static org.apache.commons.lang3.StringUtils.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;

public class ReferenceParam extends IdDt implements IQueryParameterType {

	private BaseParam myBase=new BaseParam.ComposableBaseParam();
	private String myChain;

	public ReferenceParam() {
	}

	public ReferenceParam(String theValue) {
		setValueAsQueryToken(null, theValue);
	}

	public ReferenceParam(String theChain, String theValue) {
		setValueAsQueryToken(null, theValue);
		setChain(theChain);
	}

	public ReferenceParam(String theResourceType, String theChain, String theValue) {
		if (isNotBlank(theResourceType)) {
			setValue(theResourceType + "/" + theValue);
		} else {
			setValue(theValue);
		}
		setChain(theChain);
	}

	public String getChain() {
		return myChain;
	}

	@Override
	public Boolean getMissing() {
		return myBase.getMissing();
	}

	@Override
	public String getQueryParameterQualifier() {
		if (myBase.getMissing()!=null) {
			return myBase.getQueryParameterQualifier();
		}
		
		StringBuilder b = new StringBuilder();
		if (isNotBlank(getResourceType())) {
			b.append(':');
			b.append(getResourceType());
		}
		if (isNotBlank(myChain)) {
			b.append('.');
			b.append(myChain);
		}
		if (b.length() != 0) {
			return b.toString();
		}
		return null;
	}

	public Class<? extends IBaseResource> getResourceType(FhirContext theCtx) {
		if (isBlank(getResourceType())) {
			return null;
		}
		return theCtx.getResourceDefinition(getResourceType()).getImplementingClass();
	}

	@Override
	public String getValueAsQueryToken() {
		if (myBase.getMissing()!=null) {
			return myBase.getValueAsQueryToken();
		}
		if (isBlank(getResourceType())) {
			return getValue(); // e.g. urn:asdjd or 123 or cid:wieiuru or #1
		} else {
			return getIdPart();
		}
	}

	public void setChain(String theChain) {
		myChain = theChain;
	}

	@Override
	public void setMissing(Boolean theMissing) {
		myBase.setMissing(theMissing);
	}

	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		myBase.setValueAsQueryToken(theQualifier, theValue);
		if (myBase.getMissing()!=null) {
			myChain=null;
			setValue(null);
			return;
		}
		
		String q = theQualifier;
		String resourceType = null;
		if (isNotBlank(q)) {
			if (q.startsWith(":")) {
				int nextIdx = q.indexOf('.');
				if (nextIdx != -1) {
					resourceType = q.substring(1, nextIdx);
					myChain = q.substring(nextIdx + 1);
				} else {
					resourceType = q.substring(1);
				}
			} else if (q.startsWith(".")) {
				myChain = q.substring(1);
			}
		}

		setValue(theValue);

		if (isNotBlank(resourceType) && isBlank(getResourceType())) {
			setValue(resourceType + '/' + theValue);
		}
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
	public DateParam toDateParam() {
		DateParam retVal = new DateParam();
		retVal.setValueAsQueryToken(null, getValueAsQueryToken());
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
	public NumberParam toNumberParam() {
		NumberParam retVal = new NumberParam();
		retVal.setValueAsQueryToken(null, getValueAsQueryToken());
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
	public QuantityParam toQuantityParam() {
		QuantityParam retVal = new QuantityParam();
		retVal.setValueAsQueryToken(null, getValueAsQueryToken());
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
	public StringParam toStringParam() {
		StringParam retVal = new StringParam();
		retVal.setValueAsQueryToken(null, getValueAsQueryToken());
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
	public TokenParam toTokenParam() {
		TokenParam retVal = new TokenParam();
		retVal.setValueAsQueryToken(null, getValueAsQueryToken());
		return retVal;
	}

}
