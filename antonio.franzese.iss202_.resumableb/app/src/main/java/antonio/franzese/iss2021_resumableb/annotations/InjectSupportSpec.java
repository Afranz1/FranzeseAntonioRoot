package antonio.franzese.iss2021_resumableb.annotations;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InjectSupportSpec {
}
