package antonio.franzese.iss2021_resumableb.annotations;

import java.lang.annotation.*;

@Target(value = {ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface VirtualRobotSpec {
}
