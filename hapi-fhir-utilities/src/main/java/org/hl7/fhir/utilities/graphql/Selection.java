package org.hl7.fhir.utilities.graphql;

public class Selection {    
  private Field field;
  private Fragment inlineFragment;
  private FragmentSpread fragmentSpread;
  public Field getField() {
    return field;
  }
  public void setField(Field field) {
    this.field = field;
  }
  public Fragment getInlineFragment() {
    return inlineFragment;
  }
  public void setInlineFragment(Fragment inlineFragment) {
    this.inlineFragment = inlineFragment;
  }
  public FragmentSpread getFragmentSpread() {
    return fragmentSpread;
  }
  public void setFragmentSpread(FragmentSpread fragmentSpread) {
    this.fragmentSpread = fragmentSpread;
  }

}