/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ReflectionUtil;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CompositeParam<A extends IQueryParameterType, B extends IQueryParameterType> extends BaseParam
		implements IQueryParameterType {

	private final A myLeftType;
	private final B myRightType;

	public CompositeParam(A theLeftInstance, B theRightInstance) {
		myLeftType = theLeftInstance;
		myRightType = theRightInstance;
	}

	public CompositeParam(Class<A> theLeftType, Class<B> theRightType) {
		Validate.notNull(theLeftType, "theLeftType must not be null");
		Validate.notNull(theRightType, "theRightType must not be null");
		myLeftType = ReflectionUtil.newInstance(theLeftType);
		myRightType = ReflectionUtil.newInstance(theRightType);
	}

	@Override
	String doGetQueryParameterQualifier() {
		return null;
	}

	@Override
	String doGetValueAsQueryToken() {
		StringBuilder b = new StringBuilder();
		if (myLeftType != null) {
			b.append(myLeftType.getValueAsQueryToken());
		}
		b.append('$');
		if (myRightType != null) {
			b.append(myRightType.getValueAsQueryToken());
		}
		return b.toString();
	}

	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		if (isBlank(theValue)) {
			myLeftType.setValueAsQueryToken(theContext, theParamName, theQualifier, "");
			myRightType.setValueAsQueryToken(theContext, theParamName, theQualifier, "");
		} else {
			List<String> parts = ParameterUtil.splitParameterString(theValue, '$', false);
			if (parts.size() > 2) {
				throw new InvalidRequestException(Msg.code(1947)
						+ "Invalid value for composite parameter (only one '$' is valid for this parameter, others must be escaped). Value was: "
						+ theValue);
			}
			myLeftType.setValueAsQueryToken(theContext, theParamName, theQualifier, parts.get(0));
			if (parts.size() > 1) {
				myRightType.setValueAsQueryToken(theContext, theParamName, theQualifier, parts.get(1));
			}
		}
	}

	/**
	 * @return Returns the left value for this parameter (the first of two parameters in this composite)
	 */
	public A getLeftValue() {
		return myLeftType;
	}

	/**
	 * @return Returns the right value for this parameter (the second of two parameters in this composite)
	 */
	public B getRightValue() {
		return myRightType;
	}

	/**
	 * Get the values of the subcomponents, in order.
	 */
	public List<IQueryParameterType> getValues() {
		return Collections.unmodifiableList(Arrays.asList(myLeftType, myRightType));
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("myLeftType", getLeftValue());
		b.append("myRightType", getRightValue());
		return b.toString();
	}
}
