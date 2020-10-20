package org.springframework.context.index.sample;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * A test annotation that triggers a dedicated entry in the index.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@Indexed
public @interface MetaControllerIndexed {
}
