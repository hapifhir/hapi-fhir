package ca.uhn.fhir.narrative.template.tags;

import ca.uhn.fhir.narrative.template.LValue;

import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.BlockNode;
import ca.uhn.fhir.narrative.template.nodes.LNode;

class Case extends Tag {

    /*
     * Block tag, its the standard case...when block
     */
    @Override
    public Object render(Map<String, Object> context, LNode... nodes) {

        //        ^(CASE condition           var
        //            ^(WHEN term+ block)    1,2,3  b1
        //            ^(ELSE block?))               b2

        Object condition = nodes[0].render(context);

        for (int i = 1; i < nodes.length; i++) {

            LNode node = nodes[i];

            if(i == nodes.length - 1 && node instanceof BlockNode) {
                // this must be the trailing (optional) else-block
                return node.render(context);
            }
            else {

                boolean hit = false;

                // Iterate through the list of terms (of which we do not know the size):
                // - term (',' term)*
                // - term ('or' term)*
                // and stop when we encounter a BlockNode
                while(!(node instanceof BlockNode)) {

                    Object whenExpressionValue = node.render(context);

                    if (LValue.areEqual(condition, whenExpressionValue)) {
                        hit = true;
                    }

                    i++;
                    node = nodes[i];
                }

                if(hit) {
                    return node.render(context);
                }
            }
        }

        return null;
    }
}
