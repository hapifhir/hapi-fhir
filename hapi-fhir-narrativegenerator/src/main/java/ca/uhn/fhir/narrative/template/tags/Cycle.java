package ca.uhn.fhir.narrative.template.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.LNode;

class Cycle extends Tag {

    private static final String PREPEND = "\"'";

    /*
     * Cycle is usually used within a loop to alternate
     * between values, like colors or DOM classes.
     */
    @Override
    public Object render(Map<String, Object> context, LNode... nodes) {

        // The group-name is either the first token-expression, or if that is
        // null (indicating there is no name), give it the name PREPEND followed
        // by the number of expressions in the cycle-group.
        String groupName = nodes[0] == null ?
                PREPEND + (nodes.length - 1) :
                super.asString(nodes[0].render(context));

        // Prepend a groupName with a single- and double quote as to not
        // let the groupName conflict with other variable assignments
        groupName = PREPEND + groupName;

        Object obj = context.remove(groupName);

        List<Object> elements = new ArrayList<Object>();

        for (int i = 1; i < nodes.length; i++) {
            elements.add(nodes[i].render(context));
        }

        CycleGroup group;

        if (obj == null) {
            group = new CycleGroup(elements.size());
        }
        else {
            group = (CycleGroup) obj;
        }

        context.put(groupName, group);

        return group.next(elements);
    }

    private static class CycleGroup {

        private final int sizeFirstCycle;
        private int currentIndex;

        CycleGroup(int sizeFirstCycle) {
            this.sizeFirstCycle = sizeFirstCycle;
            this.currentIndex = 0;
        }

        Object next(List<Object> elements) {

            Object obj;

            if (currentIndex >= elements.size()) {
                obj = "";
            }
            else {
                obj = elements.get(currentIndex);
            }

            currentIndex++;

            if (currentIndex == sizeFirstCycle) {
                currentIndex = 0;
            }

            return obj;
        }
    }
}
