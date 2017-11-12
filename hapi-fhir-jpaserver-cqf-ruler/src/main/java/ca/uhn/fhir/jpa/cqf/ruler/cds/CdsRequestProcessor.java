package ca.uhn.fhir.jpa.cqf.ruler.cds;

import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.dstu3.model.*;
import org.opencds.cqf.cql.data.fhir.BaseFhirDataProvider;
import org.opencds.cqf.cql.execution.Context;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CdsRequestProcessor implements Processor {
    CdsHooksRequest request;
    PlanDefinition planDefinition;
    LibraryResourceProvider libraryResourceProvider;

    CdsRequestProcessor(CdsHooksRequest request, PlanDefinition planDefinition, LibraryResourceProvider libraryResourceProvider) {
        this.request = request;
        this.planDefinition = planDefinition;
        this.libraryResourceProvider = libraryResourceProvider;
    }

    List<CdsCard> resolveActions(Context executionContext) {
        List<CdsCard> cards = new ArrayList<>();

        walkAction(executionContext, cards, planDefinition.getAction());

        return cards;
    }

    private void walkAction(Context executionContext, List<CdsCard> cards, List<PlanDefinition.PlanDefinitionActionComponent> actions) {
        for (PlanDefinition.PlanDefinitionActionComponent action : actions) {
            boolean conditionsMet = true;
            for (PlanDefinition.PlanDefinitionActionConditionComponent condition: action.getCondition()) {
                if (condition.getKind() == PlanDefinition.ActionConditionKind.APPLICABILITY) {
                    if (!condition.hasExpression()) {
                        continue;
                    }

                    Object result = executionContext.resolveExpressionRef(condition.getExpression()).getExpression().evaluate(executionContext);

                    if (!(result instanceof Boolean)) {
                        continue;
                    }

                    if (!(Boolean) result) {
                        conditionsMet = false;
                    }
                }
            }
            if (conditionsMet) {

                /*
                    Cases:
                        Definition element provides guidance for action
                        Nested actions
                        Standardized CQL (when first 2 aren't present)
                */

                if (action.hasDefinition()) {
                    if (action.getDefinition().getReferenceElement().getResourceType().equals("ActivityDefinition")) {
                        BaseFhirDataProvider provider = (BaseFhirDataProvider) executionContext.resolveDataProvider(new QName("http://hl7.org/fhir", ""));
                        Parameters inParams = new Parameters();
                        inParams.addParameter().setName("patient").setValue(new StringType(request.getPatientId()));

                        Parameters outParams = provider.getFhirClient()
                                .operation()
                                .onInstance(new IdDt("ActivityDefinition", action.getDefinition().getReferenceElement().getIdPart()))
                                .named("$apply")
                                .withParameters(inParams)
                                .useHttpGet()
                                .execute();

                        List<Parameters.ParametersParameterComponent> response = outParams.getParameter();
                        Resource resource = response.get(0).getResource();

                        if (resource == null) {
                            continue;
                        }

                        // TODO - currently only have suggestions that create resources - implement delete and update.
                        CdsCard card = new CdsCard();
                        card.setIndicator("info");
                        CdsCard.Suggestions suggestion = new CdsCard.Suggestions();
                        suggestion.setActions(
                                Collections.singletonList(
                                        new CdsCard.Suggestions.Action()
                                                .setType(CdsCard.Suggestions.Action.ActionType.create)
                                                .setResource(resource)
                                )
                        );
                        card.getSuggestions().add(suggestion);
                        cards.add(card);
                    }

                    else {
                        // PlanDefinition $apply
                        // TODO

                        // TODO - suggestion to create CarePlan
                    }
                }

                else if (action.hasAction()) {
                    walkAction(executionContext, cards, action.getAction());
                }

                // TODO - dynamicValues

                else {
                    CdsCard card = new CdsCard();
                    card.setSummary((String) executionContext.resolveExpressionRef("getSummary").getExpression().evaluate(executionContext));
                    card.setDetail((String) executionContext.resolveExpressionRef("getDetail").getExpression().evaluate(executionContext));
                    card.setIndicator((String) executionContext.resolveExpressionRef("getIndicator").getExpression().evaluate(executionContext));
                    cards.add(card);
                }
            }
        }
    }
}
