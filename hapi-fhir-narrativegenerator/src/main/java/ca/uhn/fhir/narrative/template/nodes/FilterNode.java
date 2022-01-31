package ca.uhn.fhir.narrative.template.nodes;

import ca.uhn.fhir.i18n.Msg;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.narrative.template.filters.Filter;

public class FilterNode implements LNode {

    private Filter filter;
    private List<LNode> params;

    public FilterNode(String filterName, Filter filter) {
        if (filter == null) {
            throw new IllegalArgumentException(Msg.code(731) + "no filter available  named: " + filterName);
        }
        this.filter = filter;
        this.params = new ArrayList<LNode>();
    }

    public void add(LNode param) {
        params.add(param);
    }

    public Object apply(Object value, Map<String, Object> variables) {

        List<Object> paramValues = new ArrayList<Object>();

        for (LNode node : params) {
            paramValues.add(node.render(variables));
        }

        return filter.apply(value, paramValues.toArray(new Object[paramValues.size()]));
    }

    @Override
    public Object render(Map<String, Object> context) {
        throw new RuntimeException(Msg.code(732) + "cannot render a filter");
    }
}
