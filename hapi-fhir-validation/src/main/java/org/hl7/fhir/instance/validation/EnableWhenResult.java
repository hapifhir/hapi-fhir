package org.hl7.fhir.instance.validation;


import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemEnableWhenComponent;

public class EnableWhenResult {
    private final boolean enabled;
    private final QuestionnaireItemEnableWhenComponent enableWhenCondition;
    private final Element answerItem;
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
     * @param answerItem
     *            item in QuestionnaireResponse
     */
    public EnableWhenResult(boolean enabled, String linkId, QuestionnaireItemEnableWhenComponent enableWhenCondition,
            Element answerItem) {
        this.enabled = enabled;
        this.linkId = linkId;
        this.answerItem = answerItem;
        this.enableWhenCondition = enableWhenCondition;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getLinkId() {
        return linkId;
    }

    public Element getAnswerItem() {
        return answerItem;
    }

    public QuestionnaireItemEnableWhenComponent getEnableWhenCondition() {
        return enableWhenCondition;
    }
}
