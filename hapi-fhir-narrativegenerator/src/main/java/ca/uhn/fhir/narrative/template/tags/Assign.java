package ca.uhn.fhir.narrative.template.tags;

import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.FilterNode;
import ca.uhn.fhir.narrative.template.nodes.LNode;

class Assign extends Tag {

    /*
     * Assigns some value to a variable
     */
    @Override
    public Object render(Map<String, Object> context, LNode... nodes) {

        String id = String.valueOf(nodes[0].render(context));

        FilterNode filter = null;
        LNode expression;

        if(nodes.length >= 3) {
            filter = (FilterNode)nodes[1];
            expression = nodes[2];
        }
        else {
            expression = nodes[1];
        }

        Object value = expression.render(context);

        if(filter != null) {
            value = filter.apply(value, context);
        }

        context.put(id, value);

        return "";
    }
}
