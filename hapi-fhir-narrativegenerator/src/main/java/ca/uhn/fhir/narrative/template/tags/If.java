package ca.uhn.fhir.narrative.template.tags;

import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.LNode;

class If extends Tag {

    /*
     * Standard if/else block
     */
    @Override
    public Object render(Map<String, Object> context, LNode... nodes) {

        for (int i = 0; i < nodes.length - 1; i += 2) {

            Object exprNodeValue = nodes[i].render(context);
            LNode blockNode = nodes[i + 1];

            if (super.asBoolean(exprNodeValue)) {
                return blockNode.render(context);
            }
        }

        return null;
    }
}
