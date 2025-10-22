/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.CompositeAndListParam;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.NumberAndListParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityAndListParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.SpecialAndListParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriAndListParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.binder.QueryParameterAndBinder;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum JpaParamUtil {
	;

	/**
	 * This is a utility method intended provided to help the JPA module.
	 */
	public static IQueryParameterAnd<?> parseQueryParams(
			FhirContext theContext,
			RestSearchParameterTypeEnum paramType,
			String theUnqualifiedParamName,
			List<QualifiedParamList> theParameters) {
		QueryParameterAndBinder binder;
		switch (paramType) {
			case COMPOSITE:
				throw new UnsupportedOperationException(Msg.code(496));
			case DATE:
				binder = new QueryParameterAndBinder(DateAndListParam.class, Collections.emptyList());
				break;
			case NUMBER:
				binder = new QueryParameterAndBinder(NumberAndListParam.class, Collections.emptyList());
				break;
			case QUANTITY:
				binder = new QueryParameterAndBinder(QuantityAndListParam.class, Collections.emptyList());
				break;
			case REFERENCE:
				binder = new QueryParameterAndBinder(ReferenceAndListParam.class, Collections.emptyList());
				break;
			case STRING:
				binder = new QueryParameterAndBinder(StringAndListParam.class, Collections.emptyList());
				break;
			case TOKEN:
				binder = new QueryParameterAndBinder(TokenAndListParam.class, Collections.emptyList());
				break;
			case URI:
				binder = new QueryParameterAndBinder(UriAndListParam.class, Collections.emptyList());
				break;
			case HAS:
				binder = new QueryParameterAndBinder(HasAndListParam.class, Collections.emptyList());
				break;
			case SPECIAL:
				binder = new QueryParameterAndBinder(SpecialAndListParam.class, Collections.emptyList());
				break;
			default:
				throw new IllegalArgumentException(Msg.code(497) + "Parameter '" + theUnqualifiedParamName
						+ "' has type " + paramType + " which is currently not supported.");
		}

		return binder.parse(theContext, theUnqualifiedParamName, theParameters);
	}

	/**
	 * This is a utility method intended provided to help the JPA module.
	 */
	public static IQueryParameterAnd<?> parseQueryParams(
			ISearchParamRegistry theSearchParamRegistry,
			FhirContext theContext,
			RuntimeSearchParam theParamDef,
			String theUnqualifiedParamName,
			List<QualifiedParamList> theParameters) {

		RestSearchParameterTypeEnum paramType = theParamDef.getParamType();

		if (paramType == RestSearchParameterTypeEnum.COMPOSITE) {

			List<ComponentAndCorrespondingParam> compositeList = resolveCompositeComponents(theSearchParamRegistry, theParamDef);

			if (compositeList.size() != 2) {
				throw new ConfigurationException(Msg.code(498) + "Search parameter of type " + theUnqualifiedParamName
						+ " must have 2 composite types declared in parameter annotation, found "
						+ compositeList.size());
			}

			RuntimeSearchParam left = compositeList.get(0).getComponentParameter();
			RuntimeSearchParam right = compositeList.get(1).getComponentParameter();

			@SuppressWarnings({"unchecked", "rawtypes"})
			CompositeAndListParam<IQueryParameterType, IQueryParameterType> cp = new CompositeAndListParam(
					getCompositeBindingClass(left.getParamType(), left.getName()),
					getCompositeBindingClass(right.getParamType(), right.getName()));

			cp.setValuesAsQueryTokens(theContext, theUnqualifiedParamName, theParameters);

			return cp;
		} else {
			return parseQueryParams(theContext, paramType, theUnqualifiedParamName, theParameters);
		}
	}

	/**
	 * Given a composite or combo SearchParameter, this method will resolve the components
	 * in the order they appear in the composite parameter definition. The return objects
	 * are a record containing both the component from the composite parameter definition,
	 * but also the corresponding SearchParameter definition for the target of the component.
	 *
	 * @param theSearchParamRegistry The active SearchParameter registry
	 * @param theCompositeParameter The composite search parameter
	 */
	@Nonnull
	public static List<ComponentAndCorrespondingParam> resolveCompositeComponents(
			ISearchParamRegistry theSearchParamRegistry, RuntimeSearchParam theCompositeParameter) {
		List<ComponentAndCorrespondingParam> compositeList = new ArrayList<>();
		List<RuntimeSearchParam.Component> components = theCompositeParameter.getComponents();
		for (RuntimeSearchParam.Component next : components) {
			String url = next.getReference();
			RuntimeSearchParam componentParam = theSearchParamRegistry.getActiveSearchParamByUrl(
					url, ISearchParamRegistry.SearchParamLookupContextEnum.ALL);
			if (componentParam == null) {
				throw new InternalErrorException(Msg.code(499) + "Can not find SearchParameter: " + url);
			}
			compositeList.add(new ComponentAndCorrespondingParam(next, componentParam));
		}
		return compositeList;
	}

	private static Class<?> getCompositeBindingClass(
			RestSearchParameterTypeEnum paramType, String theUnqualifiedParamName) {

		switch (paramType) {
			case DATE:
				return DateParam.class;
			case NUMBER:
				return NumberParam.class;
			case QUANTITY:
				return QuantityParam.class;
			case REFERENCE:
				return ReferenceParam.class;
			case STRING:
				return StringParam.class;
			case TOKEN:
				return TokenParam.class;
			case URI:
				return UriParam.class;
			case HAS:
				return HasParam.class;
			case SPECIAL:
				return SpecialParam.class;

			case COMPOSITE:
			default:
				throw new IllegalArgumentException(Msg.code(500) + "Parameter '" + theUnqualifiedParamName
						+ "' has type " + paramType + " which is currently not supported.");
		}
	}


	/**
	 * Given a component for a composite or combo SearchParameter (as returned by {@link #resolveCompositeComponents(ISearchParamRegistry, RuntimeSearchParam)})
	 * determines the type associated with the target parameter.
	 */
	public static RestSearchParameterTypeEnum getParameterTypeForComposite(ISearchParamRegistry theSearchParamRegistry, ComponentAndCorrespondingParam theComponentAndParam) {
		String chain = theComponentAndParam.getComponent().getComboUpliftChain();
		if (chain != null) {
			RuntimeSearchParam targetParameter = theComponentAndParam.getComponentParameter();
			for (String target : targetParameter.getTargets()) {
				RuntimeSearchParam chainTargetParam = theSearchParamRegistry.getActiveSearchParam(target, chain, ISearchParamRegistry.SearchParamLookupContextEnum.ALL);
				if (chainTargetParam != null) {
					return chainTargetParam.getParamType();
				}
			}
			// Fallback if we can't find a chain target
			return RestSearchParameterTypeEnum.TOKEN;
		} else {
			return theComponentAndParam.getComponentParameter().getParamType();
		}
	}

	// FIXME: document
	public static String getParameterNameForComposite(ComponentAndCorrespondingParam theComponentAndParam) {
		if (theComponentAndParam.getComponent().getComboUpliftChain() != null) {
			return theComponentAndParam.getComponentParameter().getName() + "." + theComponentAndParam.getComponent().getComboUpliftChain();
		} else {
			return theComponentAndParam.getComponentParameter().getName();
		}
	}

	/**
	 * Return type for {@link #resolveCompositeComponents(ISearchParamRegistry, RuntimeSearchParam)}
	 *
	 * @since 8.6.0
	 */
	public static class ComponentAndCorrespondingParam {

		private final String myCombinedParamName;
		private final RuntimeSearchParam.Component myComponent;
		private final RuntimeSearchParam myComponentParameter;
		private final String myParamName;
		private final String myChain;

		/**
		 * Constructor
		 */
		ComponentAndCorrespondingParam(@Nonnull RuntimeSearchParam.Component theComponent, @Nonnull RuntimeSearchParam theComponentParameter) {
			this.myComponent = theComponent;
			this.myComponentParameter = theComponentParameter;

			int dotIdx = theComponentParameter.getName().indexOf(".");
			if (dotIdx != -1) {
				myParamName = theComponentParameter.getName().substring(0, dotIdx);
				myChain = theComponentParameter.getName().substring(dotIdx + 1);
				this.myCombinedParamName = theComponentParameter.getName();
			} else {
				myParamName = theComponentParameter.getName();
				myChain = theComponent.getComboUpliftChain();
				if (myChain != null) {
					myCombinedParamName = myParamName + "." + myChain;
				} else {
					myCombinedParamName = myParamName;
				}
			}

		}

		/**
		 * The component definition in the source composite SearchParameter
		 */
		@Nonnull
		public RuntimeSearchParam.Component getComponent() {
			return myComponent;
		}

		/**
		 * The target definition referred to by the {@link #getComponent() component} in the source composite SearchParameter
		 */
		@Nonnull
		public RuntimeSearchParam getComponentParameter() {
			return myComponentParameter;
		}

		/**
		 * The parameter name, without any chained parameter names
		 */
		@Nonnull
		public String getParamName() {
			return myParamName;
		}

		/**
		 * The chain portion of the component parameter if any, or <code>null</code> if not. Excludes
		 * the leading period, so if the component parameter is "subject.name", this will return "name".
		 */
		@Nullable
		public String getChain() {
			return myChain;
		}

		/**
		 * The combined parameter name, including any chained parameter names, e.g. "subject.name"
		 */
		@Nonnull
		public String getCombinedParamName() {
			return myCombinedParamName;
		}

	}

}
