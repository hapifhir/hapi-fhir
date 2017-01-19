package ca.uhn.fhir.narrative.template.tags;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.narrative.template.nodes.LNode;

class Tablerow extends Tag {

    private static final String COLS = "cols";
    private static final String LIMIT = "limit";

    /*
     * tablerowloop.length       # => length of the entire for loop
     * tablerowloop.index        # => index of the current iteration
     * tablerowloop.index0       # => index of the current iteration (zero based)
     * tablerowloop.rindex       # => how many items are still left?
     * tablerowloop.rindex0      # => how many items are still left? (zero based)
     * tablerowloop.first        # => is this the first iteration?
     * tablerowloop.last         # => is this the last iteration?
     * tablerowloop.col          # => index of column in the current row
     * tablerowloop.col0         # => index of column in the current row (zero based)
     * tablerowloop.col_first    # => is this the first column in the row?
     * tablerowloop.col_last     # => is this the last column in the row?
     */
    private static final String TABLEROWLOOP = "tablerowloop";
    private static final String LENGTH = "length";
    private static final String INDEX = "index";
    private static final String INDEX0 = "index0";
    private static final String RINDEX = "rindex";
    private static final String RINDEX0 = "rindex0";
    private static final String FIRST = "first";
    private static final String LAST = "last";
    private static final String COL = "col";
    private static final String COL0 = "col0";
    private static final String COL_FIRST = "col_first";
    private static final String COL_LAST = "col_last";

    /*
     * Tables
     */
    @Override
    public Object render(Map<String, Object> context, LNode... nodes) {

        String valueName = super.asString(nodes[0].render(context));
        Object[] collection = super.asArray(nodes[1].render(context));
        LNode block = nodes[2];
        Map<String, Integer> attributes = getAttributes(collection, 3, context, nodes);

        int cols = attributes.get(COLS);
        int limit = attributes.get(LIMIT);

        Map<String, Object> tablerowloopContext = new HashMap<String, Object>();

        tablerowloopContext.put(LENGTH, collection.length);

        context.put(TABLEROWLOOP, tablerowloopContext);

        StringBuilder builder = new StringBuilder();

        int total = Math.min(collection.length, limit);

        if(total == 0) {

            builder.append("<tr class=\"row1\">\n</tr>\n");
        }
        else {

            for(int i = 0, c = 1, r = 0; i < total; i++, c++) {

                context.put(valueName, collection[i]);

                tablerowloopContext.put(INDEX0, i);
                tablerowloopContext.put(INDEX, i + 1);
                tablerowloopContext.put(RINDEX0, total - i - 1);
                tablerowloopContext.put(RINDEX, total - i);
                tablerowloopContext.put(FIRST, i == 0);
                tablerowloopContext.put(LAST, i == total - 1);
                tablerowloopContext.put(COL0, c - 1);
                tablerowloopContext.put(COL, c);
                tablerowloopContext.put(COL_FIRST, c == 1);
                tablerowloopContext.put(COL_LAST, c == cols);

                if(c == 1) {
                    r++;
                    builder.append("<tr class=\"row").append(r).append("\">").append(r == 1 ? "\n" : "");
                }

                builder.append("<td class=\"col").append(c).append("\">");
                builder.append(super.asString(block.render(context)));
                builder.append("</td>");

                if(c == cols || i == total - 1) {
                    builder.append("</tr>\n");
                    c = 0;
                }
            }
        }

        context.remove(TABLEROWLOOP);

        return builder.toString();
    }

    private Map<String, Integer> getAttributes(Object[] collection, int fromIndex, Map<String, Object> context, LNode... tokens) {

        Map<String, Integer> attributes = new HashMap<String, Integer>();

        attributes.put(COLS, collection.length);
        attributes.put(LIMIT, Integer.MAX_VALUE);

        for (int i = fromIndex; i < tokens.length; i++) {

            Object[] attribute = super.asArray(tokens[i].render(context));

            try {
                attributes.put(super.asString(attribute[0]), super.asNumber(attribute[1]).intValue());
            }
            catch (Exception e) {
                /* just ignore incorrect attributes */
            }
        }

        return attributes;
    }
}
