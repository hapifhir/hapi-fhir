package example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD})
public @interface Sliced {

   String discriminator();

   Slice[] slices();

   SlicingRuleEnum rule();

   enum SlicingRuleEnum {
      CLOSED,
      
      OPEN,
      
      OPEN_AT_END
   }
   
}
