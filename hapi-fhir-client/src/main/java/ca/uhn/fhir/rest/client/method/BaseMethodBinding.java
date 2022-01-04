package ca.uhn.fhir.rest.client.method;

/*-
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.exceptions.*;
import ca.uhn.fhir.util.ReflectionUtil;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseMethodBinding<T> implements IClientResponseHandler<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseMethodBinding.class);
	private FhirContext myContext;
	private Method myMethod;
	private List<IParameter> myParameters;
	private Object myProvider;
	private boolean mySupportsConditional;
	private boolean mySupportsConditionalMultiple;

	public BaseMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		assert theMethod != null;
		assert theContext != null;

		myMethod = theMethod;
		myContext = theContext;
		myProvider = theProvider;
		myParameters = MethodUtil.getResourceParameters(theContext, theMethod, theProvider, getRestOperationType());

		for (IParameter next : myParameters) {
			if (next instanceof ConditionalParamBinder) {
				mySupportsConditional = true;
				if (((ConditionalParamBinder) next).isSupportsMultiple()) {
					mySupportsConditionalMultiple = true;
				}
				break;
			}
		}

	}

	protected IParser createAppropriateParserForParsingResponse(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, List<Class<? extends IBaseResource>> thePreferTypes) {
		EncodingEnum encoding = EncodingEnum.forContentType(theResponseMimeType);
		if (encoding == null) {
			NonFhirResponseException ex = NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseInputStream);
			populateException(ex, theResponseInputStream);
			throw ex;
		}

		IParser parser = encoding.newParser(getContext());

		parser.setPreferTypes(thePreferTypes);

		return parser;
	}

	public List<Class<?>> getAllowableParamAnnotations() {
		return null;
	}

	public FhirContext getContext() {
		return myContext;
	}

	public Set<String> getIncludes() {
		Set<String> retVal = new TreeSet<String>();
		for (IParameter next : myParameters) {
			if (next instanceof IncludeParameter) {
				retVal.addAll(((IncludeParameter) next).getAllow());
			}
		}
		return retVal;
	}

	public Method getMethod() {
		return myMethod;
	}

	public List<IParameter> getParameters() {
		return myParameters;
	}

	public Object getProvider() {
		return myProvider;
	}

	/**
	 * Returns the name of the resource this method handles, or <code>null</code> if this method is not resource specific
	 */
	public abstract String getResourceName();

	public abstract RestOperationTypeEnum getRestOperationType();

	public abstract BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException;

	/**
	 * Does this method have a parameter annotated with {@link ConditionalParamBinder}. Note that many operations don't actually support this paramter, so this will only return true occasionally.
	 */
	public boolean isSupportsConditional() {
		return mySupportsConditional;
	}

	/**
	 * Does this method support conditional operations over multiple objects (basically for conditional delete)
	 */
	public boolean isSupportsConditionalMultiple() {
		return mySupportsConditionalMultiple;
	}

	protected BaseServerResponseException processNon2xxResponseAndReturnExceptionToThrow(int theStatusCode, String theResponseMimeType, InputStream theResponseInputStream) {
		BaseServerResponseException ex;
		switch (theStatusCode) {
		case Constants.STATUS_HTTP_400_BAD_REQUEST:
			ex = new InvalidRequestException("Server responded with HTTP 400");
			break;
		case Constants.STATUS_HTTP_404_NOT_FOUND:
			ex = new ResourceNotFoundException("Server responded with HTTP 404");
			break;
		case Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED:
			ex = new MethodNotAllowedException("Server responded with HTTP 405");
			break;
		case Constants.STATUS_HTTP_409_CONFLICT:
			ex = new ResourceVersionConflictException("Server responded with HTTP 409");
			break;
		case Constants.STATUS_HTTP_412_PRECONDITION_FAILED:
			ex = new PreconditionFailedException("Server responded with HTTP 412");
			break;
		case Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY:
			IParser parser = createAppropriateParserForParsingResponse(theResponseMimeType, theResponseInputStream, theStatusCode, null);
			// TODO: handle if something other than OO comes back
			BaseOperationOutcome operationOutcome = (BaseOperationOutcome) parser.parseResource(theResponseInputStream);
			ex = new UnprocessableEntityException(myContext, operationOutcome);
			break;
		default:
			ex = new UnclassifiedServerFailureException(theStatusCode, "Server responded with HTTP " + theStatusCode);
			break;
		}

		populateException(ex, theResponseInputStream);
		return ex;
	}

	/** For unit tests only */
	public void setParameters(List<IParameter> theParameters) {
		myParameters = theParameters;
	}

	@SuppressWarnings("unchecked")
	public static BaseMethodBinding<?> bindMethod(Method theMethod, FhirContext theContext, Object theProvider) {
		Read read = theMethod.getAnnotation(Read.class);
		Search search = theMethod.getAnnotation(Search.class);
		Metadata conformance = theMethod.getAnnotation(Metadata.class);
		Create create = theMethod.getAnnotation(Create.class);
		Update update = theMethod.getAnnotation(Update.class);
		Delete delete = theMethod.getAnnotation(Delete.class);
		History history = theMethod.getAnnotation(History.class);
		Validate validate = theMethod.getAnnotation(Validate.class);
		AddTags addTags = theMethod.getAnnotation(AddTags.class);
		DeleteTags deleteTags = theMethod.getAnnotation(DeleteTags.class);
		Transaction transaction = theMethod.getAnnotation(Transaction.class);
		Operation operation = theMethod.getAnnotation(Operation.class);
		GetPage getPage = theMethod.getAnnotation(GetPage.class);
		Patch patch = theMethod.getAnnotation(Patch.class);

		// ** if you add another annotation above, also add it to the next line:
		if (!verifyMethodHasZeroOrOneOperationAnnotation(theMethod, read, search, conformance, create, update, delete, history, validate, addTags, deleteTags, transaction, operation, getPage,
				patch)) {
			return null;
		}

		if (getPage != null) {
			return new PageMethodBinding(theContext, theMethod);
		}

		Class<? extends IBaseResource> returnType;

		Class<? extends IBaseResource> returnTypeFromRp = null;

		Class<?> returnTypeFromMethod = theMethod.getReturnType();
		if (MethodOutcome.class.isAssignableFrom(returnTypeFromMethod)) {
			// returns a method outcome
		} else if (void.class.equals(returnTypeFromMethod)) {
			// returns a bundle
		} else if (Collection.class.isAssignableFrom(returnTypeFromMethod)) {
			returnTypeFromMethod = ReflectionUtil.getGenericCollectionTypeOfMethodReturnType(theMethod);
			if (returnTypeFromMethod == null) {
				ourLog.trace("Method {} returns a non-typed list, can't verify return type", theMethod);
			} else if (!verifyIsValidResourceReturnType(returnTypeFromMethod) && !isResourceInterface(returnTypeFromMethod)) {
				throw new ConfigurationException(Msg.code(1427) + "Method '" + theMethod.getName() + "' from client type " + theMethod.getDeclaringClass().getCanonicalName()
						+ " returns a collection with generic type " + toLogString(returnTypeFromMethod)
						+ " - Must return a resource type or a collection (List, Set) with a resource type parameter (e.g. List<Patient> or List<IBaseResource> )");
			}
		} else {
			if (!isResourceInterface(returnTypeFromMethod) && !verifyIsValidResourceReturnType(returnTypeFromMethod)) {
				throw new ConfigurationException(Msg.code(1428) + "Method '" + theMethod.getName() + "' from client type " + theMethod.getDeclaringClass().getCanonicalName()
						+ " returns " + toLogString(returnTypeFromMethod) + " - Must return a resource type (eg Patient, Bundle"
						+ ", etc., see the documentation for more details)");
			}
		}

		Class<? extends IBaseResource> returnTypeFromAnnotation = IBaseResource.class;
		if (read != null) {
			returnTypeFromAnnotation = read.type();
		} else if (search != null) {
			returnTypeFromAnnotation = search.type();
		} else if (history != null) {
			returnTypeFromAnnotation = history.type();
		} else if (delete != null) {
			returnTypeFromAnnotation = delete.type();
		} else if (patch != null) {
			returnTypeFromAnnotation = patch.type();
		} else if (create != null) {
			returnTypeFromAnnotation = create.type();
		} else if (update != null) {
			returnTypeFromAnnotation = update.type();
		} else if (validate != null) {
			returnTypeFromAnnotation = validate.type();
		} else if (addTags != null) {
			returnTypeFromAnnotation = addTags.type();
		} else if (deleteTags != null) {
			returnTypeFromAnnotation = deleteTags.type();
		}

		if (!isResourceInterface(returnTypeFromAnnotation)) {
			if (!verifyIsValidResourceReturnType(returnTypeFromAnnotation)) {
				throw new ConfigurationException(Msg.code(1429) + "Method '" + theMethod.getName() + "' from client type " + theMethod.getDeclaringClass().getCanonicalName()
						+ " returns " + toLogString(returnTypeFromAnnotation) + " according to annotation - Must return a resource type");
			}
			returnType = returnTypeFromAnnotation;
		} else {
			// if (IRestfulClient.class.isAssignableFrom(theMethod.getDeclaringClass())) {
			// Clients don't define their methods in resource specific types, so they can
			// infer their resource type from the method return type.
			returnType = (Class<? extends IBaseResource>) returnTypeFromMethod;
			// } else {
			// This is a plain provider method returning a resource, so it should be
			// an operation or global search presumably
			// returnType = null;
		}

		if (read != null) {
			return new ReadMethodBinding(returnType, theMethod, theContext, theProvider);
		} else if (search != null) {
			return new SearchMethodBinding(returnType, theMethod, theContext, theProvider);
		} else if (conformance != null) {
			return new ConformanceMethodBinding(theMethod, theContext, theProvider);
		} else if (create != null) {
			return new CreateMethodBinding(theMethod, theContext, theProvider);
		} else if (update != null) {
			return new UpdateMethodBinding(theMethod, theContext, theProvider);
		} else if (delete != null) {
			return new DeleteMethodBinding(theMethod, theContext, theProvider);
		} else if (patch != null) {
			return new PatchMethodBinding(theMethod, theContext, theProvider);
		} else if (history != null) {
			return new HistoryMethodBinding(theMethod, theContext, theProvider);
		} else if (validate != null) {
			return new ValidateMethodBindingDstu2Plus(returnType, returnTypeFromRp, theMethod, theContext, theProvider, validate);
		} else if (transaction != null) {
			return new TransactionMethodBinding(theMethod, theContext, theProvider);
		} else if (operation != null) {
			return new OperationMethodBinding(returnType, returnTypeFromRp, theMethod, theContext, theProvider, operation);
		} else {
			throw new ConfigurationException(Msg.code(1430) + "Did not detect any FHIR annotations on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		// // each operation name must have a request type annotation and be
		// unique
		// if (null != read) {
		// return rm;
		// }
		//
		// SearchMethodBinding sm = new SearchMethodBinding();
		// if (null != search) {
		// sm.setRequestType(SearchMethodBinding.RequestType.GET);
		// } else if (null != theMethod.getAnnotation(PUT.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.PUT);
		// } else if (null != theMethod.getAnnotation(POST.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.POST);
		// } else if (null != theMethod.getAnnotation(DELETE.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.DELETE);
		// } else {
		// return null;
		// }
		//
		// return sm;
	}

	public static boolean isResourceInterface(Class<?> theReturnTypeFromMethod) {
		return theReturnTypeFromMethod.equals(IBaseResource.class) || theReturnTypeFromMethod.equals(IResource.class) || theReturnTypeFromMethod.equals(IAnyResource.class);
	}

	private static void populateException(BaseServerResponseException theEx, InputStream theResponseInputStream) {
		try {
			String responseText = IOUtils.toString(theResponseInputStream);
			theEx.setResponseBody(responseText);
		} catch (IOException e) {
			ourLog.debug("Failed to read response", e);
		}
	}

	private static String toLogString(Class<?> theType) {
		if (theType == null) {
			return null;
		}
		return theType.getCanonicalName();
	}

	private static boolean verifyIsValidResourceReturnType(Class<?> theReturnType) {
		if (theReturnType == null) {
			return false;
		}
		if (!IBaseResource.class.isAssignableFrom(theReturnType)) {
			return false;
		}
		return true;
		// boolean retVal = Modifier.isAbstract(theReturnType.getModifiers()) == false;
		// return retVal;
	}

	public static boolean verifyMethodHasZeroOrOneOperationAnnotation(Method theNextMethod, Object... theAnnotations) {
		Object obj1 = null;
		for (Object object : theAnnotations) {
			if (object != null) {
				if (obj1 == null) {
					obj1 = object;
				} else {
					throw new ConfigurationException(Msg.code(1431) + "Method " + theNextMethod.getName() + " on type '" + theNextMethod.getDeclaringClass().getSimpleName() + " has annotations @"
							+ obj1.getClass().getSimpleName() + " and @" + object.getClass().getSimpleName() + ". Can not have both.");
				}

			}
		}
		if (obj1 == null) {
			return false;
			// throw new ConfigurationException(Msg.code(1432) + "Method '" +
			// theNextMethod.getName() + "' on type '" +
			// theNextMethod.getDeclaringClass().getSimpleName() +
			// " has no FHIR method annotations.");
		}
		return true;
	}

}
