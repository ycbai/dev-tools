package com.byc.tools.patch.services.impls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.services.SearchPatchPluginsService;

public class SearchPatchPluginsServiceImpl extends MakePatchServiceImpl implements SearchPatchPluginsService {

	@Override
	public List<String> searchPatchPlugins(String patchBranch) throws PatchException {
		String url = "https://api.github.com/repos/Talend/tdi-studio-se/compare/dec1792...patch/6.4.1";
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);

			// add request header
			// request.addHeader("User-Agent", USER_AGENT);
			HttpResponse response = client.execute(request);

			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
