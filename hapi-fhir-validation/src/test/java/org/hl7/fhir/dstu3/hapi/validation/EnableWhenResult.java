package org.hl7.fhir.dstu3.hapi.validation;

import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemEnableWhenComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;

public class EnableWhenResult {
    private final boolean enabled;
    private final QuestionnaireItemEnableWhenComponent enableWhenCondition;
    private final QuestionnaireResponseItemComponent responseItem;
    private final String linkId;

    /**
     * Evaluation result of enableWhen condition
     * 
     * @param enabled
     *            Evaluation result
     * @param linkId
     *            LinkId of the questionnaire item
     * @param enableWhenCondition
     *            Evaluated enableWhen condition
     * @param responseItem
     *            item in QuestionnaireResponse
     */
    public EnableWhenResult(boolean enabled, String linkId, QuestionnaireItemEnableWhenComponent enableWhenCondition,
            QuestionnaireResponseItemComponent responseItem) {
        this.enabled = enabled;
        this.linkId = linkId;
        this.responseItem = responseItem;
        this.enableWhenCondition = enableWhenCondition;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getLinkId() {
        return linkId;
    }

    public QuestionnaireResponseItemComponent getResponseItem() {
        return responseItem;
    }

    public QuestionnaireItemEnableWhenComponent getEnableWhenCondition() {
        return enableWhenCondition;
    }
}
