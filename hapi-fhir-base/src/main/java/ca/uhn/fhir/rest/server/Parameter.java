package ca.uhn.fhir.rest.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Parameter {
    private String name;
    private boolean required;
    private Class<?> type;
	private IParser parser;

    public Parameter(){}

    public Parameter(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(final Class<?> type) {
        this.type = type;
        if (type.getSimpleName().equals("IdentifierDt")) {
        	this.parser = new IParser() {
				@Override
				public Object parse(String theString) throws InternalErrorException {
					Object dt;
					try {
						dt = type.newInstance();
						
						Method method = dt.getClass().getMethod("setValueAsQueryToken", String.class);
						method.invoke(dt, theString);
						
					} catch (InstantiationException e) {
						throw new InternalErrorException(e);
					} catch (IllegalAccessException e) {
						throw new InternalErrorException(e);
					} catch (NoSuchMethodException e) {
						throw new InternalErrorException(e);
					} catch (SecurityException e) {
						throw new InternalErrorException(e);
					} catch (IllegalArgumentException e) {
						throw new InternalErrorException(e);
					} catch (InvocationTargetException e) {
						throw new InternalErrorException(e);
					}
					return dt;
				}
			};
        } else {
        	throw new ConfigurationException("Unsupported data type for parameter: " + type.getCanonicalName());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

	public Object parse(String theString) throws InternalErrorException {
		return parser.parse(theString);
	}
	
	private interface IParser
	{
		Object parse(String theString) throws InternalErrorException;
	}
	
}
