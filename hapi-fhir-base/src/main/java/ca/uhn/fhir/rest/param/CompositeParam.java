package ca.uhn.fhir.rest.param;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class CompositeParam<A extends IQueryParameterType, B extends IQueryParameterType> implements IQueryParameterType {

	private A myLeftType;
	private B myRightType;

	public CompositeParam(A theLeftInstance, B theRightInstance) {
		myLeftType = theLeftInstance;
		myRightType = theRightInstance;
	}

	public CompositeParam(Class<A> theLeftType, Class<B> theRightType) {
		Validate.notNull(theLeftType);
		Validate.notNull(theRightType);
		try {
			myLeftType = theLeftType.newInstance();
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate type: " + myLeftType, e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate type: " + myLeftType, e);
		}
		try {
			myRightType = theRightType.newInstance();
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate type: " + myRightType, e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate type: " + myRightType, e);
		}
	}

	/**
	 * @return Returns the left value for this parameter (the first of two parameters in this composite)
	 */
	public A getLeftValue() {
		return myLeftType;
	}

	@Override
	public String getQueryParameterQualifier() {
		return null;
	}

	/**
	 * @return Returns the right value for this parameter (the second of two parameters in this composite)
	 */
	public B getRightValue() {
		return myRightType;
	}

	@Override
	public String getValueAsQueryToken() {
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
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		if (isBlank(theValue)) {
			myLeftType.setValueAsQueryToken(theQualifier, "");
			myRightType.setValueAsQueryToken(theQualifier, "");
		} else {
			List<String> parts = ParameterUtil.splitParameterString(theValue, '$', false);
			if (parts.size() > 2) {
				throw new InvalidRequestException("Invalid value for composite parameter (only one '$' is valid for this parameter, others must be escaped). Value was: " + theValue);
			}
			myLeftType.setValueAsQueryToken(theQualifier, parts.get(0));
			if (parts.size() > 1) {
				myRightType.setValueAsQueryToken(theQualifier, parts.get(1));
			}
		}
	}

}
