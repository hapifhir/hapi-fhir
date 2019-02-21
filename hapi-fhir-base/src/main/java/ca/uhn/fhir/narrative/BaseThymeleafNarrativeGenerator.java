package ca.uhn.fhir.narrative;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.DefaultTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.util.Map;
import java.util.Set;

public abstract class BaseThymeleafNarrativeGenerator extends BaseNarrativeGenerator {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseThymeleafNarrativeGenerator.class);

	private TemplateEngine myProfileTemplateEngine;

	private IMessageResolver resolver;

	@Override
	protected String processNamedTemplate(FhirContext theContext, String theName, IBaseResource theResource) {
		Context context = new Context();
		context.setVariable("resource", theResource);
		context.setVariable("fhirVersion", theContext.getVersion().getVersion().name());

		return myProfileTemplateEngine.process(theName, context);
	}

	@Override
	protected void initializeNarrativeEngine(FhirContext theFhirContext) {
		myProfileTemplateEngine = new TemplateEngine();
		BaseThymeleafNarrativeGenerator.ProfileResourceResolver resolver = new BaseThymeleafNarrativeGenerator.ProfileResourceResolver();
		myProfileTemplateEngine.setTemplateResolver(resolver);
		StandardDialect dialect = new StandardDialect() {
			@Override
			public Set<IProcessor> getProcessors(String theDialectPrefix) {
				Set<IProcessor> retVal = super.getProcessors(theDialectPrefix);
				retVal.add(new BaseThymeleafNarrativeGenerator.NarrativeAttributeProcessor(theFhirContext, theDialectPrefix));
				return retVal;
			}

		};
		myProfileTemplateEngine.setDialect(dialect);
		if (this.resolver != null) {
			myProfileTemplateEngine.setMessageResolver(this.resolver);
		}
	}

	public void setMessageResolver(IMessageResolver resolver) {
		this.resolver = resolver;
		if (myProfileTemplateEngine != null && resolver != null) {
			myProfileTemplateEngine.setMessageResolver(resolver);
		}
	}

	public class NarrativeAttributeProcessor extends AbstractAttributeTagProcessor {

		private FhirContext myContext;

		protected NarrativeAttributeProcessor(FhirContext theContext, String theDialectPrefix) {
			super(TemplateMode.XML, theDialectPrefix, null, false, "narrative", true, 0, true);
			myContext = theContext;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void doProcess(ITemplateContext theContext, IProcessableElementTag theTag, AttributeName theAttributeName, String theAttributeValue, IElementTagStructureHandler theStructureHandler) {
			IEngineConfiguration configuration = theContext.getConfiguration();
			IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);

			final IStandardExpression expression = expressionParser.parseExpression(theContext, theAttributeValue);
			final Object value = expression.execute(theContext);

			if (value == null) {
				return;
			}

			Context context = new Context();
			context.setVariable("fhirVersion", myContext.getVersion().getVersion().name());
			context.setVariable("resource", value);

			String name = null;

			Class<? extends Object> nextClass = value.getClass();
			do {
				name = myClassToName.get(nextClass);
				nextClass = nextClass.getSuperclass();
			} while (name == null && nextClass.equals(Object.class) == false);

			if (name == null) {
				if (value instanceof IBaseResource) {
					name = myContext.getResourceDefinition((Class<? extends IBaseResource>) value).getName();
				} else if (value instanceof IDatatype) {
					name = value.getClass().getSimpleName();
					name = name.substring(0, name.length() - 2);
				} else if (value instanceof IBaseDatatype) {
					name = value.getClass().getSimpleName();
					if (name.endsWith("Type")) {
						name = name.substring(0, name.length() - 4);
					}
				} else {
					throw new DataFormatException("Don't know how to determine name for type: " + value.getClass());
				}
				name = name.toLowerCase();
				if (!myNameToNarrativeTemplate.containsKey(name)) {
					name = null;
				}
			}

			if (name == null) {
				if (isIgnoreMissingTemplates()) {
					ourLog.debug("No narrative template available for type: {}", value.getClass());
					return;
				}
				throw new DataFormatException("No narrative template for class " + value.getClass());
			}

			String result = myProfileTemplateEngine.process(name, context);
			String trim = result.trim();

			theStructureHandler.setBody(trim, true);

		}

	}

	private final class ProfileResourceResolver extends DefaultTemplateResolver {

		@Override
		protected boolean computeResolvable(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			String template = myNameToNarrativeTemplate.get(theTemplate);
			return template != null;
		}

		@Override
		protected TemplateMode computeTemplateMode(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			return TemplateMode.XML;
		}

		@Override
		protected ITemplateResource computeTemplateResource(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			String template = myNameToNarrativeTemplate.get(theTemplate);
			return new StringTemplateResource(template);
		}

		@Override
		protected ICacheEntryValidity computeValidity(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			return AlwaysValidCacheEntryValidity.INSTANCE;
		}

	}

}
