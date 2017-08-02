package com.byc.tools.patch.utils;

public class GithubUtil {

	public static final String GITHUB_API_PREFIX = "https://api.github.com";

	public static final String GITHUB_API_REPS = "repos";

	public static final String GITHUB_API_COMPARE = "compare";

	public static String getDifferentsOfTwoCommitsURL(String repository, String startSha1, String endSha1) {
		String urlFormat = "%s/%s/%s/%s/%s...%s";
		return String.format(urlFormat, GITHUB_API_PREFIX, GITHUB_API_REPS, repository, GITHUB_API_COMPARE, startSha1, endSha1);
	}

}
