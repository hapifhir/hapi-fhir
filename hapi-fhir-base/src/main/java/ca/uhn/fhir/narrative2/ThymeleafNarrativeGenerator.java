package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
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
import java.util.Optional;
import java.util.Set;

public class ThymeleafNarrativeGenerator extends BaseNarrativeGenerator {

	private TemplateEngine getTemplateEngine() {
		TemplateEngine engine = new TemplateEngine();
		ProfileResourceResolver resolver = new ProfileResourceResolver();
		engine.setTemplateResolver(resolver);
		StandardDialect dialect = new StandardDialect() {
			@Override
			public Set<IProcessor> getProcessors(String theDialectPrefix) {
				Set<IProcessor> retVal = super.getProcessors(theDialectPrefix);
				retVal.add(new NarrativeTahProcessor(theDialectPrefix));
				return retVal;
			}

		};

		engine.setDialect(dialect);
		return engine;
	}

	@Override
	protected String applyTemplate(INarrativeTemplate theTemplate, IBase theTargetContext) {

		Context context = new Context();
		context.setVariable("resource", theTargetContext);
		context.setVariable("context", theTargetContext);
		context.setVariable("fhirVersion", getFhirContext().getVersion().getVersion().name());

		String result = getTemplateEngine().process(theTemplate.getTemplateName(), context);

//		if (myCleanWhitespace) {
//			ourLog.trace("Pre-whitespace cleaning: ", result);
//			result = cleanWhitespace(result);
//			ourLog.trace("Post-whitespace cleaning: ", result);
//		}

		return result;
	}


	@Override
	protected TemplateTypeEnum getStyle() {
		return TemplateTypeEnum.THYMELEAF;
	}

	private class ProfileResourceResolver extends DefaultTemplateResolver {
		@Override
		protected boolean computeResolvable(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			return getManifest().getTemplateByName(getStyle(), theTemplate).isPresent();
		}

		@Override
		protected TemplateMode computeTemplateMode(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			return TemplateMode.XML;
		}

		@Override
		protected ITemplateResource computeTemplateResource(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			return getManifest()
				.getTemplateByName(getStyle(), theTemplate)
				.map(t -> new StringTemplateResource(t.getTemplateText()))
				.orElseThrow(() -> new IllegalArgumentException("Unknown template: " + theTemplate));
		}

		@Override
		protected ICacheEntryValidity computeValidity(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			return AlwaysValidCacheEntryValidity.INSTANCE;
		}
	}

	private class NarrativeTahProcessor extends AbstractElementTagProcessor {

		public NarrativeTahProcessor(String dialectPrefix) {
			super(TemplateMode.XML, dialectPrefix, "narrative", true, null, true, 0);
		}

		@Override
		protected void doProcess(ITemplateContext theTemplateContext, IProcessableElementTag theTag, IElementTagStructureHandler theStructureHandler) {
			String name = theTag.getAttributeValue("th:name");
			String element = theTag.getAttributeValue("th:element");

			IEngineConfiguration configuration = theTemplateContext.getConfiguration();
			IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
			final IStandardExpression expression = expressionParser.parseExpression(theTemplateContext, element);
			final Object elementValue = expression.execute(theTemplateContext);

			Optional<INarrativeTemplate> templateOpt = getManifest().getTemplateByName(getStyle(), name);
			if (!templateOpt.isPresent()) {
				throw new InternalErrorException("Unknown templateOpt name: " + name);
			}

			theStructureHandler.replaceWith(name + " BBB " + elementValue, false);
		}
	}


//	/**
//	 * This is a thymeleaf extension that allows people to do things like
//	 * <th:block th:narrative="${result}"/>
//	 */
//	private class NarrativeAttributeProcessor extends AbstractAttributeTagProcessor {
//
//		protected NarrativeAttributeProcessor(String theDialectPrefix) {
//			super(TemplateMode.XML, theDialectPrefix, null, false, "narrative", true, 0, true);
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		protected void doProcess(ITemplateContext theContext, IProcessableElementTag theTag, AttributeName theAttributeName, String theAttributeValue, IElementTagStructureHandler theStructureHandler) {
//			IEngineConfiguration configuration = theContext.getConfiguration();
//			IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
//
//			final IStandardExpression expression = expressionParser.parseExpression(theContext, theAttributeValue);
//			final Object value = expression.execute(theContext);
//
//			if (value == null) {
//				return;
//			}
//
//			Context context = new Context();
//			context.setVariable("fhirVersion", getFhirContext().getVersion().getVersion().name());
//			context.setVariable("resource", value);
//
//			String name = null;
//
//			Class<? extends Object> nextClass = value.getClass();
//			do {
//				name = myClassToName.get(nextClass);
//				nextClass = nextClass.getSuperclass();
//			} while (name == null && nextClass.equals(Object.class) == false);
//
//			if (name == null) {
//				if (value instanceof IBaseResource) {
//					name = myContext.getResourceDefinition((Class<? extends IBaseResource>) value).getName();
//				} else if (value instanceof IDatatype) {
//					name = value.getClass().getSimpleName();
//					name = name.substring(0, name.length() - 2);
//				} else if (value instanceof IBaseDatatype) {
//					name = value.getClass().getSimpleName();
//					if (name.endsWith("Type")) {
//						name = name.substring(0, name.length() - 4);
//					}
//				} else {
//					throw new DataFormatException("Don't know how to determine name for type: " + value.getClass());
//				}
//				name = name.toLowerCase();
//				if (!myNameToNarrativeTemplate.containsKey(name)) {
//					name = null;
//				}
//			}
//
//			if (name == null) {
//				if (myIgnoreMissingTemplates) {
//					ourLog.debug("No narrative template available for type: {}", value.getClass());
//					return;
//				}
//				throw new DataFormatException("No narrative template for class " + value.getClass());
//			}
//
//			String result = myProfileTemplateEngine.process(name, context);
//			String trim = result.trim();
//
//			theStructureHandler.setBody(trim, true);
//
//		}
//
//	}
}
