package ca.uhn.fhir.narrative.template.filters;

import java.util.*;
import java.util.Map;

class Sort extends Filter {

    /*
     * sort(input, property = nil)
     *
     * Sort elements of the array provide optional property with
     * which to sort an array of hashes or drops
     */
    @Override
    public Object apply(Object value, Object... params) {

        if (value == null) {
            return "";
        }

        if(!super.isArray(value)) {
            throw new RuntimeException("cannot sort: " + value);
        }

        Object[] array = super.asArray(value);
        String property = params.length == 0 ? null : super.asString(params[0]);

        List<Comparable> list = asComparableList(array, property);

        Collections.sort(list);

        return property == null ?
                list.toArray(new Comparable[list.size()]) :
                list.toArray(new SortableMap[list.size()]);
    }

    private List<Comparable> asComparableList(Object[] array, String property) {

        List<Comparable> list = new ArrayList<Comparable>();

        for (Object obj : array) {

            if(obj instanceof java.util.Map && property != null) {
                list.add(new SortableMap((java.util.Map<String, Comparable>)obj, property));
            }
            else {
                list.add((Comparable) obj);
            }
        }

        return list;
    }

    static class SortableMap extends HashMap<String, Comparable> implements Comparable<SortableMap> {

        final String property;

        SortableMap(java.util.Map<String, Comparable> map, String property) {
            super.putAll(map);
            this.property = property;
        }

        @Override
        public int compareTo(SortableMap that) {

            Comparable thisValue = this.get(property);
            Comparable thatValue = that.get(property);

            if(thisValue == null || thatValue == null) {
                throw new RuntimeException("Liquid error: comparison of Hash with Hash failed");
            }

            return thisValue.compareTo(thatValue);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            for(java.util.Map.Entry entry : super.entrySet()) {
                builder.append(entry.getKey()).append(entry.getValue());
            }

            return builder.toString();
        }
    }
}
