/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.narrative;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.narrative2.BaseNarrativeGenerator;
import ca.uhn.fhir.narrative2.INarrativeTemplate;
import ca.uhn.fhir.narrative2.NarrativeGeneratorTemplateUtils;
import ca.uhn.fhir.narrative2.TemplateTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IBase;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.DefaultTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseThymeleafNarrativeGenerator extends BaseNarrativeGenerator {

	public static final String FHIRPATH = "fhirpath";
	private IMessageResolver myMessageResolver;
	private IFhirPathEvaluationContext myFhirPathEvaluationContext;

	/**
	 * Constructor
	 */
	protected BaseThymeleafNarrativeGenerator() {
		super();
	}

	public void setFhirPathEvaluationContext(IFhirPathEvaluationContext theFhirPathEvaluationContext) {
		myFhirPathEvaluationContext = theFhirPathEvaluationContext;
	}

	private TemplateEngine getTemplateEngine(FhirContext theFhirContext) {
		TemplateEngine engine = new TemplateEngine();
		ITemplateResolver resolver = new NarrativeTemplateResolver(theFhirContext);
		engine.setTemplateResolver(resolver);
		if (myMessageResolver != null) {
			engine.setMessageResolver(myMessageResolver);
		}
		StandardDialect dialect = new StandardDialect() {
			@Override
			public Set<IProcessor> getProcessors(String theDialectPrefix) {
				Set<IProcessor> retVal = super.getProcessors(theDialectPrefix);
				retVal.add(new NarrativeTagProcessor(theFhirContext, theDialectPrefix));
				retVal.add(new NarrativeAttributeProcessor(theDialectPrefix, theFhirContext));
				return retVal;
			}
		};
		engine.setDialect(dialect);

		engine.addDialect(new NarrativeGeneratorDialect(theFhirContext));
		return engine;
	}

	@Override
	protected String applyTemplate(FhirContext theFhirContext, INarrativeTemplate theTemplate, IBase theTargetContext) {

		Context context = new Context();
		context.setVariable("resource", theTargetContext);
		context.setVariable("context", theTargetContext);
		context.setVariable("narrativeUtil", NarrativeGeneratorTemplateUtils.INSTANCE);
		context.setVariable(
				"fhirVersion", theFhirContext.getVersion().getVersion().name());

		return getTemplateEngine(theFhirContext).process(theTemplate.getTemplateName(), context);
	}

	@Override
	protected EnumSet<TemplateTypeEnum> getStyle() {
		return EnumSet.of(TemplateTypeEnum.THYMELEAF);
	}

	private String applyTemplateWithinTag(
			FhirContext theFhirContext, ITemplateContext theTemplateContext, String theName, String theElement) {
		IEngineConfiguration configuration = theTemplateContext.getConfiguration();
		IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
		final IStandardExpression expression = expressionParser.parseExpression(theTemplateContext, theElement);
		Object elementValueObj = expression.execute(theTemplateContext);
		final IBase elementValue = (IBase) elementValueObj;
		if (elementValue == null) {
			return "";
		}

		List<INarrativeTemplate> templateOpt;
		if (isNotBlank(theName)) {
			templateOpt = getManifest().getTemplateByName(theFhirContext, getStyle(), theName);
			if (templateOpt.isEmpty()) {
				throw new InternalErrorException(Msg.code(1863) + "Unknown template name: " + theName);
			}
		} else {
			templateOpt = getManifest().getTemplateByElement(theFhirContext, getStyle(), elementValue);
			if (templateOpt.isEmpty()) {
				throw new InternalErrorException(Msg.code(1864) + "No template for type: " + elementValue.getClass());
			}
		}

		return applyTemplate(theFhirContext, templateOpt.get(0), elementValue);
	}

	public void setMessageResolver(IMessageResolver theMessageResolver) {
		myMessageResolver = theMessageResolver;
	}

	private class NarrativeTemplateResolver extends DefaultTemplateResolver {
		private final FhirContext myFhirContext;

		private NarrativeTemplateResolver(FhirContext theFhirContext) {
			myFhirContext = theFhirContext;
		}

		@Override
		protected boolean computeResolvable(
				IEngineConfiguration theConfiguration,
				String theOwnerTemplate,
				String theTemplate,
				Map<String, Object> theTemplateResolutionAttributes) {
			if (theOwnerTemplate == null) {
				return getManifest()
								.getTemplateByName(myFhirContext, getStyle(), theTemplate)
								.size()
						> 0;
			} else {
				return getManifest()
								.getTemplateByFragmentName(myFhirContext, getStyle(), theTemplate)
								.size()
						> 0;
			}
		}

		@Override
		protected TemplateMode computeTemplateMode(
				IEngineConfiguration theConfiguration,
				String theOwnerTemplate,
				String theTemplate,
				Map<String, Object> theTemplateResolutionAttributes) {
			return TemplateMode.XML;
		}

		@Override
		protected ITemplateResource computeTemplateResource(
				IEngineConfiguration theConfiguration,
				String theOwnerTemplate,
				String theTemplate,
				Map<String, Object> theTemplateResolutionAttributes) {
			if (theOwnerTemplate == null) {
				return getManifest().getTemplateByName(myFhirContext, getStyle(), theTemplate).stream()
						.findFirst()
						.map(t -> new StringTemplateResource(t.getTemplateText()))
						.orElseThrow(() -> new IllegalArgumentException("Unknown template: " + theTemplate));
			} else {
				return getManifest().getTemplateByFragmentName(myFhirContext, getStyle(), theTemplate).stream()
						.findFirst()
						.map(t -> new StringTemplateResource(t.getTemplateText()))
						.orElseThrow(() -> new IllegalArgumentException("Unknown template: " + theTemplate));
			}
		}

		@Override
		protected ICacheEntryValidity computeValidity(
				IEngineConfiguration theConfiguration,
				String theOwnerTemplate,
				String theTemplate,
				Map<String, Object> theTemplateResolutionAttributes) {
			return AlwaysValidCacheEntryValidity.INSTANCE;
		}
	}

	private class NarrativeTagProcessor extends AbstractElementTagProcessor {

		private final FhirContext myFhirContext;

		NarrativeTagProcessor(FhirContext theFhirContext, String dialectPrefix) {
			super(TemplateMode.XML, dialectPrefix, "narrative", true, null, true, 0);
			myFhirContext = theFhirContext;
		}

		@Override
		protected void doProcess(
				ITemplateContext theTemplateContext,
				IProcessableElementTag theTag,
				IElementTagStructureHandler theStructureHandler) {
			String name = theTag.getAttributeValue("th:name");
			String element = theTag.getAttributeValue("th:element");

			String appliedTemplate = applyTemplateWithinTag(myFhirContext, theTemplateContext, name, element);
			theStructureHandler.replaceWith(appliedTemplate, false);
		}
	}

	/**
	 * This is a thymeleaf extension that allows people to do things like
	 * <th:block th:narrative="${result}"/>
	 */
	private class NarrativeAttributeProcessor extends AbstractAttributeTagProcessor {

		private final FhirContext myFhirContext;

		NarrativeAttributeProcessor(String theDialectPrefix, FhirContext theFhirContext) {
			super(TemplateMode.XML, theDialectPrefix, null, false, "narrative", true, 0, true);
			myFhirContext = theFhirContext;
		}

		@Override
		protected void doProcess(
				ITemplateContext theContext,
				IProcessableElementTag theTag,
				AttributeName theAttributeName,
				String theAttributeValue,
				IElementTagStructureHandler theStructureHandler) {
			String text = applyTemplateWithinTag(myFhirContext, theContext, null, theAttributeValue);
			theStructureHandler.setBody(text, false);
		}
	}

	private class NarrativeGeneratorDialect implements IDialect, IExpressionObjectDialect {

		private final FhirContext myFhirContext;

		public NarrativeGeneratorDialect(FhirContext theFhirContext) {
			myFhirContext = theFhirContext;
		}

		@Override
		public String getName() {
			return "NarrativeGeneratorDialect";
		}

		@Override
		public IExpressionObjectFactory getExpressionObjectFactory() {
			return new NarrativeGeneratorExpressionObjectFactory(myFhirContext);
		}
	}

	private class NarrativeGeneratorExpressionObjectFactory implements IExpressionObjectFactory {

		private final FhirContext myFhirContext;

		public NarrativeGeneratorExpressionObjectFactory(FhirContext theFhirContext) {
			myFhirContext = theFhirContext;
		}

		@Override
		public Set<String> getAllExpressionObjectNames() {
			return Sets.newHashSet(FHIRPATH);
		}

		@Override
		public Object buildObject(IExpressionContext context, String expressionObjectName) {
			if (FHIRPATH.equals(expressionObjectName)) {
				return new NarrativeGeneratorFhirPathExpressionObject(myFhirContext);
			}
			return null;
		}

		@Override
		public boolean isCacheable(String expressionObjectName) {
			return false;
		}
	}

	private class NarrativeGeneratorFhirPathExpressionObject {

		private final FhirContext myFhirContext;

		public NarrativeGeneratorFhirPathExpressionObject(FhirContext theFhirContext) {
			myFhirContext = theFhirContext;
		}

		public IBase evaluateFirst(IBase theInput, String theExpression) {
			IFhirPath fhirPath = newFhirPath();
			Optional<IBase> output = fhirPath.evaluateFirst(theInput, theExpression, IBase.class);
			return output.orElse(null);
		}

		public List<IBase> evaluate(IBase theInput, String theExpression) {
			IFhirPath fhirPath = newFhirPath();
			return fhirPath.evaluate(theInput, theExpression, IBase.class);
		}

		private IFhirPath newFhirPath() {
			IFhirPath fhirPath = myFhirContext.newFhirPath();
			if (myFhirPathEvaluationContext != null) {
				fhirPath.setEvaluationContext(myFhirPathEvaluationContext);
			}
			return fhirPath;
		}
	}
}
