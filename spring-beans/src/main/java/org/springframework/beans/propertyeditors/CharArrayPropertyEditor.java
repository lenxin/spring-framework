package org.springframework.beans.propertyeditors;

import org.springframework.lang.Nullable;

import java.beans.PropertyEditorSupport;

/**
 * Editor for char arrays. Strings will simply be converted to
 * their corresponding char representations.
 *
 * @see String#toCharArray()
 * @since 1.2.8
 */
public class CharArrayPropertyEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(@Nullable String text) {
		setValue(text != null ? text.toCharArray() : null);
	}

	@Override
	public String getAsText() {
		char[] value = (char[]) getValue();
		return (value != null ? new String(value) : "");
	}
}
