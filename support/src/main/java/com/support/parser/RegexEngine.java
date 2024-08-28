package com.support.parser;

import java.util.regex.Pattern;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

public class RegexEngine implements TextWatcher {
	private String[] mPatternsString;
	private Pattern[] mPatterns;
	private TextView mCaller;
	private RegexEngineNotifier mNotifier;

	public static final String PATTERN_INVALID = "invalid";

	private boolean isErrorMessageShown = false;
	private String mErrorMessage = "Invalid Field";

	public RegexEngine(String[] patterns, View caller)
			throws NullPointerException, IllegalArgumentException {
		if (caller != null && caller instanceof TextView)
			mCaller = (TextView) caller;
		else
			throw new IllegalArgumentException(
					"View must be instance of text view");

		if (patterns != null) {
			mPatterns = new Pattern[patterns.length];
			for (int i = 0; i < mPatterns.length; i++) {
				mPatterns[i] = Pattern.compile(patterns[i]);
			}
		} else
			throw new NullPointerException("Pattern block cannot be empty");

		mPatternsString = patterns;
	}

	public RegexEngine(String[] patterns, View caller, String message)
			throws NullPointerException, IllegalArgumentException {
		if (caller != null && caller instanceof TextView)
			mCaller = (TextView) caller;
		else
			throw new IllegalArgumentException(
					"View must be instance of text view");

		if (patterns != null) {
			mPatterns = new Pattern[patterns.length];
			for (int i = 0; i < mPatterns.length; i++) {
				mPatterns[i] = Pattern.compile(patterns[i]);
			}
		} else
			throw new NullPointerException("Pattern block cannot be empty");

		mPatternsString = patterns;

		isErrorMessageShown = true;
		mErrorMessage = message;
	}

	public void setRegexNotifier(RegexEngineNotifier notifier) {
		mNotifier = notifier;
	}

	public interface RegexEngineNotifier {
		public abstract void notifyRegex(String pattern, View v);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		boolean isMatched = false;
		for (int i = 0; i < mPatterns.length; i++) {
			if (mPatterns[i].matcher(s).matches()) {
				if (mNotifier != null) {
					mNotifier.notifyRegex(mPatternsString[i], mCaller);
					isMatched = true;
				}
				break;
			}
		}

		if (!isMatched && mNotifier != null) {
			mNotifier.notifyRegex(PATTERN_INVALID, mCaller);
			if (isErrorMessageShown)
				mCaller.setError(mErrorMessage);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
