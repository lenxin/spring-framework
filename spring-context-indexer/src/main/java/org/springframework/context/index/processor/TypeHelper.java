package org.springframework.context.index.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Type utilities.
 *
 * @author Stephane Nicoll
 * @since 5.0
 */
class TypeHelper {

	private final ProcessingEnvironment env;

	private final Types types;


	public TypeHelper(ProcessingEnvironment env) {
		this.env = env;
		this.types = env.getTypeUtils();
	}


	public String getType(Element element) {
		return getType(element != null ? element.asType() : null);
	}

	public String getType(AnnotationMirror annotation) {
		return getType(annotation != null ? annotation.getAnnotationType() : null);
	}

	public String getType(TypeMirror type) {
		if (type == null) {
			return null;
		}
		if (type instanceof DeclaredType) {
			DeclaredType declaredType = (DeclaredType) type;
			Element enclosingElement = declaredType.asElement().getEnclosingElement();
			if (enclosingElement != null && enclosingElement instanceof TypeElement) {
				return getQualifiedName(enclosingElement) + "$" + declaredType.asElement().getSimpleName().toString();
			}
			else {
				return getQualifiedName(declaredType.asElement());
			}
		}
		return type.toString();
	}

	private String getQualifiedName(Element element) {
		if (element instanceof QualifiedNameable) {
			return ((QualifiedNameable) element).getQualifiedName().toString();
		}
		return element.toString();
	}

	/**
	 * Return the super class of the specified {@link Element} or null if this
	 * {@code element} represents {@link Object}.
	 */
	public Element getSuperClass(Element element) {
		List<? extends TypeMirror> superTypes = this.types.directSupertypes(element.asType());
		if (superTypes.isEmpty()) {
			return null;  // reached java.lang.Object
		}
		return this.types.asElement(superTypes.get(0));
	}

	/**
	 * Return the interfaces that are <strong>directly</strong> implemented by the
	 * specified {@link Element} or an empty list if this {@code element} does not
	 * implement any interface.
	 */
	public List<Element> getDirectInterfaces(Element element) {
		List<? extends TypeMirror> superTypes = this.types.directSupertypes(element.asType());
		List<Element> directInterfaces = new ArrayList<>();
		if (superTypes.size() > 1) { // index 0 is the super class
			for (int i = 1; i < superTypes.size(); i++) {
				Element e = this.types.asElement(superTypes.get(i));
				if (e != null) {
					directInterfaces.add(e);
				}
			}
		}
		return directInterfaces;
	}

	public List<? extends AnnotationMirror> getAllAnnotationMirrors(Element e) {
		try {
			return this.env.getElementUtils().getAllAnnotationMirrors(e);
		}
		catch (Exception ex) {
			// This may fail if one of the annotations is not available.
			return Collections.emptyList();
		}
	}

}
