package org.springframework.context.index.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link StereotypesProvider} implementation that provides the
 * {@value STEREOTYPE} stereotype for each package-info.
 *
 * @since 5.0
 */
class PackageInfoStereotypesProvider implements StereotypesProvider {
	public static final String STEREOTYPE = "package-info";

	@Override
	public Set<String> getStereotypes(Element element) {
		Set<String> stereotypes = new HashSet<>();
		if (element.getKind() == ElementKind.PACKAGE) {
			stereotypes.add(STEREOTYPE);
		}
		return stereotypes;
	}
}
