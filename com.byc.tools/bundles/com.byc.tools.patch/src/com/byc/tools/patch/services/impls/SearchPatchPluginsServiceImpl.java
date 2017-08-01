package com.byc.tools.patch.services.impls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.services.SearchPatchPluginsService;

public class SearchPatchPluginsServiceImpl extends MakePatchServiceImpl implements SearchPatchPluginsService {

	@Override
	public Set<String> searchPatchPlugins(String patchBranch) throws PatchException {
		Set<String> pluginNames = new HashSet<String>();
		String url = "https://api.github.com/repos/Talend/tdi-studio-se/compare/dec1792...patch/6.4.1";
		String fileNamePattern = "\\w+(\\.\\w+)+";
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);

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
			JSONArray filesArray = (JSONArray) obj.get("files");
			if (filesArray != null) {
				for (Object object : filesArray) {
					JSONObject fileObj = (JSONObject) object;
					String fileName = (String) fileObj.get("filename");
					Pattern pattern = Pattern.compile(fileNamePattern);
					Matcher matcher = pattern.matcher(fileName);
					if (matcher.find()) {
						fileName = matcher.group();
						pluginNames.add(fileName);
						System.out.println(fileName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pluginNames;
	}

	public static void main(String[] args) {
		SearchPatchPluginsServiceImpl ins = new SearchPatchPluginsServiceImpl();
		try {
			ins.searchPatchPlugins(null);
		} catch (PatchException e) {
			e.printStackTrace();
		}
	}

}
