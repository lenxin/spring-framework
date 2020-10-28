package org.springframework.web.servlet.tags.form;

import javax.servlet.jsp.JspException;

import org.springframework.lang.Nullable;

/**
 * Abstract base class to provide common methods for
 * implementing databinding-aware JSP tags for rendering an HTML '{@code input}'
 * element with a '{@code type}' of '{@code checkbox}' or '{@code radio}'.
 *



 * @since 2.5
 */
@SuppressWarnings("serial")
public abstract class AbstractCheckedElementTag extends AbstractHtmlInputElementTag {

	/**
	 * Render the '{@code input(checkbox)}' with the supplied value, marking the
	 * '{@code input}' element as 'checked' if the supplied value matches the
	 * bound value.
	 */
	protected void renderFromValue(@Nullable Object value, TagWriter tagWriter) throws JspException {
		renderFromValue(value, value, tagWriter);
	}

	/**
	 * Render the '{@code input(checkbox)}' with the supplied value, marking the
	 * '{@code input}' element as 'checked' if the supplied value matches the
	 * bound value.
	 */
	protected void renderFromValue(@Nullable Object item, @Nullable Object value, TagWriter tagWriter)
			throws JspException {

		String displayValue = convertToDisplayString(value);
		tagWriter.writeAttribute("value", processFieldValue(getName(), displayValue, getInputType()));
		if (isOptionSelected(value) || (value != item && isOptionSelected(item))) {
			tagWriter.writeAttribute("checked", "checked");
		}
	}

	/**
	 * Determines whether the supplied value matched the selected value
	 * through delegating to {@link SelectedValueComparator#isSelected}.
	 */
	private boolean isOptionSelected(@Nullable Object value) throws JspException {
		return SelectedValueComparator.isSelected(getBindStatus(), value);
	}

	/**
	 * Render the '{@code input(checkbox)}' with the supplied value, marking
	 * the '{@code input}' element as 'checked' if the supplied Boolean is
	 * {@code true}.
	 */
	protected void renderFromBoolean(Boolean boundValue, TagWriter tagWriter) throws JspException {
		tagWriter.writeAttribute("value", processFieldValue(getName(), "true", getInputType()));
		if (boundValue) {
			tagWriter.writeAttribute("checked", "checked");
		}
	}

	/**
	 * Return a unique ID for the bound name within the current PageContext.
	 */
	@Override
	@Nullable
	protected String autogenerateId() throws JspException {
		String id = super.autogenerateId();
		return (id != null ? TagIdGenerator.nextId(id, this.pageContext) : null);
	}


	/**
	 * Writes the '{@code input}' element to the supplied
	 * {@link TagWriter},
	 * marking it as 'checked' if appropriate.
	 */
	@Override
	protected abstract int writeTagContent(TagWriter tagWriter) throws JspException;

	/**
	 * Flags "type" as an illegal dynamic attribute.
	 */
	@Override
	protected boolean isValidDynamicAttribute(String localName, Object value) {
		return !"type".equals(localName);
	}

	/**
	 * Return the type of the HTML input element to generate:
	 * "checkbox" or "radio".
	 */
	protected abstract String getInputType();

}
