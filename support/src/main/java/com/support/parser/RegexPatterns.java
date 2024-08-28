package com.support.parser;

public class RegexPatterns {

	public static final String REGEX_EMAIL = "^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$";
	public static final String REGEX_UK_NUMBERS = "^(\\+44\\s?7\\d{3}|\\(?07\\d{3}\\)?)\\s?\\d{3}\\s?\\d{3}$";
	//public static final String REGEX_UK_POST_CODE_WS = "^(GIR 0AA)|((([A-PR-UWYZ][0-9][0-9]?)|(([A-PR-UWYZ][A-HK-Y][0-9][0-9]?)|(([A-PR-UWYZ][0-9][A-HJKSTUW])|([A-PR-UWYZ][A-HK-Y][0-9][ABEHMNPRVWXY])))) [0-9][A-BD-HJLNP-UW-Z]{2})";
	public static final String REGEX_UK_POST_CODE_WOS = "^(GIR 0AA)|((([A-PR-UWYZ][0-9][0-9]?)|(([A-PR-UWYZ][A-HK-Y][0-9][0-9]?)|(([A-PR-UWYZ][0-9][A-HJKSTUW])|([A-PR-UWYZ][A-HK-Y][0-9][ABEHMNPRVWXY]))))[0-9][A-BD-HJLNP-UW-Z]{2})";
	public static final String REGEX_UK_POST_CODE_WS ="";
	public static final String REGEX_ALPHANUMERIC ="^(\\d+[a-z]|[a-z]+\\d)[a-z\\d]*$";

}
