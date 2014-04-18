package synitex.common.gwt.util.shared;

import java.util.ArrayList;
import java.util.List;

public class Locale {

	public static String CURRENT_LANG_COOKIE = "current.lang";

	public static final String RU = "ru";
	public static final String EN = "en";
	public static final String LV = "lv";

	private static List<String> ALL = new ArrayList<String>();
	static {
		ALL.add(RU);
		ALL.add(EN);
		//ALL.add(LV);
	}

	public static List<String> getSupportedLocales() {
		return ALL;
	}

}
