package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.Currency;

/**
 * Editor for {@code java.util.Currency}, translating currency codes into Currency
 * objects. Exposes the currency code as text representation of a Currency object.
 *
 * @see java.util.Currency
 * @since 3.0
 */
public class CurrencyEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(Currency.getInstance(text));
	}

	@Override
	public String getAsText() {
		Currency value = (Currency) getValue();
		return (value != null ? value.getCurrencyCode() : "");
	}
}
