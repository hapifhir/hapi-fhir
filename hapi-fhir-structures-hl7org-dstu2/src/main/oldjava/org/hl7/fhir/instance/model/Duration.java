package org.hl7.fhir.instance.model;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
/**
 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
 */
@DatatypeDef(name="Duration", profileOf=Quantity.class)
public class Duration extends Quantity {

    private static final long serialVersionUID = 1069574054L;

      public Duration copy() {
        Duration dst = new Duration();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        dst.comparator = comparator == null ? null : comparator.copy();
        dst.unit = unit == null ? null : unit.copy();
        dst.system = system == null ? null : system.copy();
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      protected Duration typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Duration))
          return false;
        Duration o = (Duration) other;
        return compareDeep(value, o.value, true) && compareDeep(comparator, o.comparator, true) && compareDeep(unit, o.unit, true)
           && compareDeep(system, o.system, true) && compareDeep(code, o.code, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Duration))
          return false;
        Duration o = (Duration) other;
        return compareValues(value, o.value, true) && compareValues(comparator, o.comparator, true) && compareValues(unit, o.unit, true)
           && compareValues(system, o.system, true) && compareValues(code, o.code, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (value == null || value.isEmpty()) && (comparator == null || comparator.isEmpty())
           && (unit == null || unit.isEmpty()) && (system == null || system.isEmpty()) && (code == null || code.isEmpty())
          ;
      }


}

