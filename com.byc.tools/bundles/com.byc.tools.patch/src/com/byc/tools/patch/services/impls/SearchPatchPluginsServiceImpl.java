package com.byc.tools.patch.services.impls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.byc.tools.patch.constants.IGenericConstants;
import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.prefs.PreferenceUtils;
import com.byc.tools.patch.services.SearchPatchPluginsService;
import com.byc.tools.patch.utils.GithubUtil;

public class SearchPatchPluginsServiceImpl extends MakePatchServiceImpl implements SearchPatchPluginsService {

	private static String FILE_NAME_PATTERN = "\\w+(\\.\\w+)+";

	private static String FILE_NAME_ATTRIBUTE = "files";

	@Override
	public Set<String> searchPatchPlugins(String patchBranch, IProgressMonitor monitor) throws PatchException {
		Set<String> pluginNames = new HashSet<String>();

		if (StringUtils.isBlank(patchBranch)) {
			return pluginNames;
		}

		String[] patchBranches = PreferenceUtils.getPatchBranches();
		if (patchBranches.length == 0) {
			return pluginNames;
		}

		Map<String, String> rep2sha1Map = new HashMap<>();
		for (String br : patchBranches) {
			String[] bra = br.split(IGenericConstants.COLON);
			if (bra.length > 1) {
				if (patchBranch.equals(bra[0])) {
					String[] r2sArray = bra[1].split(IGenericConstants.COMMA);
					for (String r2s : r2sArray) {
						String[] array = r2s.split(IGenericConstants.WN);
						if (array.length > 1) {
							rep2sha1Map.put(array[0], array[1]);
						}
					}
				}
			}
		}
		if (rep2sha1Map.size() == 0) {
			return pluginNames;
		}

		String[] gitReps = PreferenceUtils.getGitRepsURLs();
		int repSize = gitReps.length;
		if (repSize == 0) {
			return pluginNames;
		}

		int totalCount = getCount();
		int unitWeight = totalCount / repSize;
		String startSha1 = null;
		for (String rep : gitReps) {
			monitor.setTaskName("Searching repository: " + rep);
			startSha1 = rep2sha1Map.get(rep);
			if (startSha1 == null) {
				continue;
			}
			String url = GithubUtil.getDifferentsOfTwoCommitsURL(rep, startSha1, patchBranch);
			search(pluginNames, url);
			monitor.worked(unitWeight);
		}

		// String url =
		// "https://api.github.com/repos/Talend/tdi-studio-se/compare/dec1792...patch/6.4.1";

		return pluginNames;
	}

	private void search(Set<String> pluginNames, String queryUrl) throws PatchException {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(queryUrl);

			// add request header
			// request.addHeader("User-Agent", USER_AGENT);
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(result.toString());
			JSONArray filesArray = (JSONArray) obj.get(FILE_NAME_ATTRIBUTE);
			if (filesArray != null) {
				for (Object object : filesArray) {
					JSONObject fileObj = (JSONObject) object;
					String fileName = (String) fileObj.get("filename");
					Pattern pattern = Pattern.compile(FILE_NAME_PATTERN);
					Matcher matcher = pattern.matcher(fileName);
					if (matcher.find()) {
						fileName = matcher.group();
						pluginNames.add(fileName);
						System.out.println(fileName);
					}
				}
			}
		} catch (Exception e) {
			throw new PatchException(e);
		}
	}

}
