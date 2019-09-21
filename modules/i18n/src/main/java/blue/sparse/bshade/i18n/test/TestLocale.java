package blue.sparse.bshade.i18n.test;

import blue.sparse.bshade.i18n.LocaleKey;
import blue.sparse.bshade.i18n.value.LocaleValue;

public enum TestLocale implements LocaleKey {

	MESSAGE_TEST("A simple test."),
	MESSAGE_PLACEHOLDERS("A message with {value} placeholders."),
	MESSAGE_BUY("Buy {count}x items for ${price #,###.00}."),
	MESSAGE_FLIGHT("Flight: {if enabled \"&aEnabled\" else \"&cDisabled\"}"),
	ITEM_EMERALD("&eEmerald"),
	ITEM_DIAMOND("&bDiamond")
	;

	private LocaleValue value;

	TestLocale(String raw) {
		this.value = LocaleValue.parse(raw);
	}

	@Override
	public LocaleValue get() {
		return value;
	}
}
