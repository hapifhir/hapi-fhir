package ca.uhn.fhir.narrative.template.filters;

class Times extends Filter {

    /*
     * times(input, operand)
     *
     * multiplication
     */
    @Override
    public Object apply(Object value, Object... params) {

        if(value == null) {
            value = 0L;
        }

        super.checkParams(params, 1);

        Object rhsObj = params[0];

        if (super.isInteger(value) && super.isInteger(rhsObj)) {
            return super.asNumber(value).longValue() * super.asNumber(rhsObj).longValue();
        }

        return super.asNumber(value).doubleValue() * super.asNumber(rhsObj).doubleValue();
    }
}
