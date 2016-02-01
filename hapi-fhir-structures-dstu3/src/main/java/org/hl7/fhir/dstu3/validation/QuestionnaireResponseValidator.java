package org.hl7.fhir.dstu3.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.TimeType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.utils.IWorkerContext;

/**
 * Validates that an instance of {@link QuestionnaireResponse} is valid against the {@link Questionnaire} that it claims
 * to conform to.
 * 
 * @author James Agnew
 */
public class QuestionnaireResponseValidator extends BaseValidator {

	/*
	 * ***************************************************************** Note to anyone working on this class -
	 * 
	 * This class has unit tests which run within the HAPI project build. Please sync any changes here to HAPI and ensure
	 * that unit tests are run. ****************************************************************
	 */

	private IWorkerContext myWorkerCtx;

	public QuestionnaireResponseValidator(IWorkerContext theWorkerCtx) {
		this.myWorkerCtx = theWorkerCtx;
	}

	private Set<Class<? extends Type>> allowedTypes(Class<? extends Type> theClass0) {
		return allowedTypes(theClass0, null);
	}

	private Set<Class<? extends Type>> allowedTypes(Class<? extends Type> theClass0, Class<? extends Type> theClass1) {
		HashSet<Class<? extends Type>> retVal = new HashSet<Class<? extends Type>>();
		retVal.add(theClass0);
		if (theClass1 != null) {
			retVal.add(theClass1);
		}
		return Collections.unmodifiableSet(retVal);
	}

	private List<QuestionnaireResponseItemComponent> findResponsesByLinkId(List<QuestionnaireResponseItemComponent> theItem, String theLinkId) {
		Validate.notBlank(theLinkId, "theLinkId must not be blank");

		ArrayList<QuestionnaireResponseItemComponent> retVal = new ArrayList<QuestionnaireResponse.QuestionnaireResponseItemComponent>();
		for (QuestionnaireResponseItemComponent next : theItem) {
			if (theLinkId.equals(next.getLinkId())) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	public void validate(List<ValidationMessage> theErrors, QuestionnaireResponse theAnswers) {
		LinkedList<String> pathStack = new LinkedList<String>();
		pathStack.add("QuestionnaireResponse");
		pathStack.add(QuestionnaireResponse.SP_QUESTIONNAIRE);

		if (!fail(theErrors, IssueType.INVALID, pathStack, theAnswers.hasQuestionnaire(), "QuestionnaireResponse does not specity which questionnaire it is providing answers to")) {
			return;
		}

		Reference questionnaireRef = theAnswers.getQuestionnaire();
		Questionnaire questionnaire = getQuestionnaire(theAnswers, questionnaireRef);
		if (!fail(theErrors, IssueType.INVALID, pathStack, questionnaire != null, "Questionnaire {0} is not found in the WorkerContext", theAnswers.getQuestionnaire().getReference())) {
			return;
		}

		QuestionnaireResponseStatus status = theAnswers.getStatus();
		boolean validateRequired = false;
		if (status == QuestionnaireResponseStatus.COMPLETED || status == QuestionnaireResponseStatus.AMENDED) {
			validateRequired = true;
		}

		pathStack.removeLast();
		// pathStack.add("group(0)");
		validateItems(theErrors, questionnaire.getItem(), theAnswers.getItem(), pathStack, theAnswers, validateRequired);
	}

	private Questionnaire getQuestionnaire(QuestionnaireResponse theAnswers, Reference theQuestionnaireRef) {
		Questionnaire retVal;
		String value = theQuestionnaireRef.getReferenceElement().getValue();
		if (theQuestionnaireRef.getReferenceElement().isLocal()) {
			retVal = (Questionnaire) theQuestionnaireRef.getResource();
			if (retVal == null) {
				for (Resource next : theAnswers.getContained()) {
					if (value.equals(next.getId())) {
						retVal = (Questionnaire) next;
					}
				}
			}
		} else {
			retVal = myWorkerCtx.fetchResource(Questionnaire.class, value);
		}
		return retVal;
	}

	private ValueSet getValueSet(QuestionnaireResponse theResponse, Reference theQuestionnaireRef) {
		ValueSet retVal;
		if (theQuestionnaireRef.getReferenceElement().isLocal()) {
			retVal = (ValueSet) theQuestionnaireRef.getResource();
			if (retVal == null) {
				for (Resource next : theResponse.getContained()) {
					if (theQuestionnaireRef.getReferenceElement().getValue().equals(next.getId())) {
						retVal = (ValueSet) next;
					}
				}
			}
		} else {
			retVal = myWorkerCtx.fetchResource(ValueSet.class, theQuestionnaireRef.getReferenceElement().getValue());
		}
		return retVal;
	}

	private void validateGroup(List<ValidationMessage> theErrors, QuestionnaireItemComponent theQuestGroup, QuestionnaireResponseItemComponent theRespGroup, LinkedList<String> thePathStack, QuestionnaireResponse theResponse, boolean theValidateRequired) {
		validateItems(theErrors, theQuestGroup.getItem(), theRespGroup.getItem(), thePathStack, theResponse, theValidateRequired);
	}

	private void validateQuestion(List<ValidationMessage> theErrors, QuestionnaireItemComponent theQuestion, QuestionnaireResponseItemComponent theRespGroup, LinkedList<String> thePathStack, QuestionnaireResponse theResponse, boolean theValidateRequired) {
		String linkId = theQuestion.getLinkId();
		if (!fail(theErrors, IssueType.INVALID, thePathStack, isNotBlank(linkId), "Questionnaire is invalid, question found with no link ID")) {
			return;
		}

		QuestionnaireItemType type = theQuestion.getType();
		if (type == null) {
			rule(theErrors, IssueType.INVALID, thePathStack, false, "Questionnaire is invalid, no type specified for question with link ID[{0}]", linkId);
			return;
		}

		// List<QuestionnaireResponseItemComponent> responses;
		// if (theRespGroup == null) {
		// responses = findResponsesByLinkId(theResponse.getItem(), linkId);
		// } else {
		// responses = findResponsesByLinkId(theRespGroup.getItem(), linkId);
		// }
		List<QuestionnaireResponseItemAnswerComponent> responses = theRespGroup.getAnswer();

		if (responses.size() > 1) {
			rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !theQuestion.getRepeats(), "Multiple answers found with linkId[{0}]", linkId);
		}
		if (responses.size() == 0) {
			if (theValidateRequired) {
				rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !theQuestion.getRequired(), "Missing answer item for required item with linkId[{0}]", linkId);
			} else {
				hint(theErrors, IssueType.BUSINESSRULE, thePathStack, !theQuestion.getRequired(), "Missing answer item for required item with linkId[{0}]", linkId);
			}
			return;
		}

		// QuestionnaireResponseItemComponent responseItem = responses.get(0);
		try {
			// thePathStack.add("item(" + responses.indexOf(responseItem) + ")");
			validateQuestionAnswers(theErrors, theQuestion, thePathStack, type, theRespGroup, theResponse, theValidateRequired);
		} finally {
			// thePathStack.removeLast();
		}
	}

	private void validateItems(List<ValidationMessage> theErrors, List<QuestionnaireItemComponent> theQuestionnaireItems, List<QuestionnaireResponseItemComponent> theResponseItems, LinkedList<String> thePathStack, QuestionnaireResponse theResponse, boolean theValidateRequired) {
		Set<String> allowedItems = new HashSet<String>();
		for (QuestionnaireItemComponent nextQuestionnaireItem : theQuestionnaireItems) {
			if (nextQuestionnaireItem.getType()== QuestionnaireItemType.NULL || nextQuestionnaireItem.getType() == null) {
				rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Questionnaire definition contains item with no type");
				String linkId = nextQuestionnaireItem.getLinkId();
				if (isNotBlank(linkId)) {
					// Just so that we don't also get a warning about the answer being present
					allowedItems.add(linkId);
				}
				continue;
			}
			
			if (!QuestionnaireItemType.DISPLAY.equals(nextQuestionnaireItem.getType())) {
				String itemType = QuestionnaireItemType.GROUP.equals(nextQuestionnaireItem.getType()) ? "group" : "question";
				String linkId = nextQuestionnaireItem.getLinkId();
				if (isBlank(linkId)) {
					rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Questionnaire definition contains {0} with no linkId", itemType);
					continue;
				}
				allowedItems.add(linkId);

				List<QuestionnaireResponseItemComponent> responseItems = findResponsesByLinkId(theResponseItems, linkId);
				if (responseItems.isEmpty()) {
					if (nextQuestionnaireItem.getRequired()) {
						if (theValidateRequired) {
							rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Missing required {0} with linkId[{1}]", itemType, linkId);
						} else {
							hint(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Missing required {0} with linkId[{1}]", itemType, linkId);
						}
					}
					continue;
				}
				if (responseItems.size() > 1) {
					if (nextQuestionnaireItem.getRepeats() == false) {
						int index = theResponseItems.indexOf(responseItems.get(1));
						thePathStack.add("item(" + index + ")");
						rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Multiple repetitions of {0} with linkId[{1}] found at this position, but this item cannot repeat", itemType, linkId);
						thePathStack.removeLast();
					}
				}
				for (QuestionnaireResponseItemComponent nextResponseItem : responseItems) {
					int index = theResponseItems.indexOf(nextResponseItem);
					thePathStack.add("item(" + index + ")");
					if (nextQuestionnaireItem.getType() == QuestionnaireItemType.GROUP) {
						validateGroup(theErrors, nextQuestionnaireItem, nextResponseItem, thePathStack, theResponse, theValidateRequired);
					} else {
						validateQuestion(theErrors, nextQuestionnaireItem, nextResponseItem, thePathStack, theResponse, theValidateRequired);
					}
					thePathStack.removeLast();
				}
			}
		}

		// Make sure there are no items in response that aren't in the questionnaire
		int idx = -1;
		for (QuestionnaireResponseItemComponent next : theResponseItems) {
			idx++;
			if (!allowedItems.contains(next.getLinkId())) {
				thePathStack.add("item(" + idx + ")");
				rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Item with linkId[{0}] found at this position, but this item does not exist at this position in Questionnaire", next.getLinkId());
				thePathStack.removeLast();
			}
		}
	}

	private void validateQuestionAnswers(List<ValidationMessage> theErrors, QuestionnaireItemComponent theQuestion, LinkedList<String> thePathStack, QuestionnaireItemType type, QuestionnaireResponseItemComponent responseQuestion, QuestionnaireResponse theResponse, boolean theValidateRequired) {

		String linkId = theQuestion.getLinkId();
		Set<Class<? extends Type>> allowedAnswerTypes = determineAllowedAnswerTypes(type);
		if (allowedAnswerTypes.isEmpty()) {
			rule(theErrors, IssueType.BUSINESSRULE, thePathStack, responseQuestion.isEmpty(), "Question with linkId[{0}] has no answer type but an answer was provided", linkId);
		} else {
			rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !(responseQuestion.getAnswer().size() > 1 && !theQuestion.getRepeats()), "Multiple answers to non repeating question with linkId[{0}]", linkId);
			if (theValidateRequired) {
				rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !(theQuestion.getRequired() && responseQuestion.getAnswer().isEmpty()), "Missing answer to required question with linkId[{0}]", linkId);
			} else {
				hint(theErrors, IssueType.BUSINESSRULE, thePathStack, !(theQuestion.getRequired() && responseQuestion.getAnswer().isEmpty()), "Missing answer to required question with linkId[{0}]", linkId);
			}
		}

		int answerIdx = -1;
		for (QuestionnaireResponseItemAnswerComponent nextAnswer : responseQuestion.getAnswer()) {
			answerIdx++;
			try {
				thePathStack.add("answer(" + answerIdx + ")");
				Type nextValue = nextAnswer.getValue();
				if (!allowedAnswerTypes.contains(nextValue.getClass())) {
					rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] found of type [{1}] but this is invalid for question of type [{2}]", linkId, nextValue.getClass().getSimpleName(), type.toCode());
					continue;
				}

				// Validate choice answers
				if (type == QuestionnaireItemType.CHOICE || type == QuestionnaireItemType.OPENCHOICE) {
					if (nextAnswer.getValue() instanceof StringType) {
						// n.b. we can only be here if it's an open-choice
						String value = ((StringType)nextAnswer.getValue()).getValue();
						if (isBlank(value)) {
							if (Boolean.TRUE.equals(theQuestion.getRequiredElement().getValue())) {
								rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] has no value but this item is required", linkId);
							}
						}
					} else {
						Coding coding = (Coding) nextAnswer.getValue();
						if (isBlank(coding.getCode()) && isBlank(coding.getDisplay()) && isBlank(coding.getSystem())) {
							rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] is of type coding, but none of code, system, and display are populated", linkId);
							continue;
						} else if (isBlank(coding.getCode()) && isBlank(coding.getSystem())) {
							if (type != QuestionnaireItemType.OPENCHOICE) {
								rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] is of type only has a display populated (no code or system) but question does not allow {1}", linkId, QuestionnaireItemType.OPENCHOICE.name());
								continue;
							}
						} else if (isBlank(coding.getCode()) || isBlank(coding.getSystem())) {
							rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] has a coding, but this coding does not contain a code and system (both must be present, or neither as the question allows {1})", linkId, QuestionnaireItemType.OPENCHOICE.name());
							continue;
						}

						String optionsRef = theQuestion.getOptions().getReference();
						if (isNotBlank(optionsRef)) {
							ValueSet valueSet = getValueSet(theResponse, theQuestion.getOptions());
							if (valueSet == null) {
								rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Question with linkId[{0}] has options ValueSet[{1}] but this ValueSet can not be found", linkId, optionsRef);
								continue;
							}

							boolean found = false;
							if (coding.getSystem().equals(valueSet.getCodeSystem().getSystem())) {
								for (ConceptDefinitionComponent next : valueSet.getCodeSystem().getConcept()) {
									if (coding.getCode().equals(next.getCode())) {
										found = true;
										break;
									}
								}
							}

							rule(theErrors, IssueType.BUSINESSRULE, thePathStack, found, "Question with linkId[{0}] has answer with system[{1}] and code[{2}] but this is not a valid answer for ValueSet[{3}]", linkId, coding.getSystem(), coding.getCode(), optionsRef);
						}
					}
				}

				validateItems(theErrors, theQuestion.getItem(), nextAnswer.getItem(), thePathStack, theResponse, theValidateRequired);

			} finally {
				thePathStack.removeLast();
			}

		} // for answers
	}

	private Set<Class<? extends Type>> determineAllowedAnswerTypes(QuestionnaireItemType type) {
		Set<Class<? extends Type>> allowedAnswerTypes;
		switch (type) {
		case ATTACHMENT:
			allowedAnswerTypes = allowedTypes(Attachment.class);
			break;
		case BOOLEAN:
			allowedAnswerTypes = allowedTypes(BooleanType.class);
			break;
		case CHOICE:
			allowedAnswerTypes = allowedTypes(Coding.class);
			break;
		case DATE:
			allowedAnswerTypes = allowedTypes(DateType.class);
			break;
		case DATETIME:
			allowedAnswerTypes = allowedTypes(DateTimeType.class);
			break;
		case DECIMAL:
			allowedAnswerTypes = allowedTypes(DecimalType.class);
			break;
		case INSTANT:
			allowedAnswerTypes = allowedTypes(InstantType.class);
			break;
		case INTEGER:
			allowedAnswerTypes = allowedTypes(IntegerType.class);
			break;
		case OPENCHOICE:
			allowedAnswerTypes = allowedTypes(Coding.class, StringType.class);
			break;
		case QUANTITY:
			allowedAnswerTypes = allowedTypes(Quantity.class);
			break;
		case REFERENCE:
			allowedAnswerTypes = allowedTypes(Reference.class);
			break;
		case STRING:
			allowedAnswerTypes = allowedTypes(StringType.class);
			break;
		case TEXT:
			allowedAnswerTypes = allowedTypes(StringType.class);
			break;
		case TIME:
			allowedAnswerTypes = allowedTypes(TimeType.class);
			break;
		case URL:
			allowedAnswerTypes = allowedTypes(UriType.class);
			break;
		case NULL:
		default:
			allowedAnswerTypes = Collections.emptySet();
		}
		return allowedAnswerTypes;
	}
}
