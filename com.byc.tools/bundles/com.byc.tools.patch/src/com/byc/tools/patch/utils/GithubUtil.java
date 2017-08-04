package com.byc.tools.patch.utils;

import java.util.Base64;

import com.byc.tools.patch.constants.IGenericConstants;
import com.byc.tools.patch.prefs.PreferenceUtils;

public class GithubUtil {

	public static final String GITHUB_API_PREFIX = "https://api.github.com";

	public static final String GITHUB_API_REPS = "repos";

	public static final String GITHUB_API_COMPARE = "compare";

	public static String getDifferentsOfTwoCommitsURL(String repository, String startSha1, String endSha1) {
		String urlFormat = "%s/%s/%s/%s/%s...%s";
		return String.format(urlFormat, GITHUB_API_PREFIX, GITHUB_API_REPS, repository, GITHUB_API_COMPARE, startSha1,
				endSha1);
	}

	public static String getAuthHeaderString() {
		String username = PreferenceUtils.getGitUsername();
		String password = PreferenceUtils.getGitPassword();
		if (username == null || password == null) {
			return null;
		}
		String userNamePwd = username + IGenericConstants.COLON + password;
		String encodedUP = Base64.getEncoder().encodeToString(userNamePwd.getBytes());
		return "Basic " + encodedUP;
	}

}
