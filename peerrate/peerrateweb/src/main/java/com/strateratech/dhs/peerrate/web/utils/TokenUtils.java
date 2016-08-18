package com.strateratech.dhs.peerrate.web.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

/**
 * Util for reading tokenized strings
 * 
 * @author 2020
 * @date Mar 10, 2016
 * @version
 *
 */
public class TokenUtils {
	/**
	 * Super flexible Regex that basically treats any and all of the following
	 * characters s delimiters :,.|;
	 * 
	 * Note: it also greedily strips whitespace around delimiters so you don't
	 * have to trim everything.
	 */
	public static final String TOKEN_SPLIT_REGEX = "\\s*[;:,\\.\\|]\\s*";
	public static final String COMMA = ",";
	public static final String COLON = ":";
	public static final String WHITESPACE_REGEX = "\\s+";
    public static final CharSequence PERIOD = ".";

	/**
	 * Splits a string and returns a list of delimited strings minus whitespace.
	 * No empty strings will be included so "bob,,Matt" becomes just
	 * Arrays.asList(new String[]{"bob","Matt"})
	 * 
	 * @param input
	 * @return
	 */
	public static List<String> split(@Nonnull String input) {
		List<String> tokens = new ArrayList<String>();
		for (String token : input.split(TOKEN_SPLIT_REGEX)) {
			if (!StringUtils.isBlank(token)) {
				tokens.add(token);
			}
		}
		return tokens;
	}

}
