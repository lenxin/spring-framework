package org.springframework.context.annotation.spr10546;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.spr10546.scanpackage.AEnclosingConfig;

/**
 *

 */
@Configuration
@ComponentScan(basePackageClasses=AEnclosingConfig.class)
public class ParentWithComponentScanConfig {

}
