package com.byc.tools.patch.prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;

import com.byc.tools.patch.PatchPlugin;
import com.byc.tools.patch.constants.IGenericConstants;

public class PreferenceUtils {

	public final static String PREF_KEY_GIT_USER_NAME = "GUN";

	public final static String PREF_KEY_GIT_PASSWORD = "GPWD";

	public final static String PREF_KEY_GIT_REPS = "GRS";

	public final static String PREF_KEY_BRANCHES = "PBS";

	private static IPreferenceStore store = PatchPlugin.getDefault().getPreferenceStore();

	public static String getGitUsername() {
		String username = store.getString(PREF_KEY_GIT_USER_NAME);
		return username;
	}

	public static String getGitPassword() {
		String password = store.getString(PREF_KEY_GIT_PASSWORD);
		return password;
	}

	public static String[] getGitRepsURLs() {
		String grs = store.getString(PREF_KEY_GIT_REPS);
		if (grs != null) {
			return getArrayByString(grs);
		}
		return new String[0];
	}

	public static String[] getPatchBranches() {
		String grs = store.getString(PREF_KEY_BRANCHES);
		if (grs != null) {
			return getArrayByString(grs);
		}
		return new String[0];
	}

	public static String[] getPatchBranchNames() {
		List<String> patchBrNames = new ArrayList<>();
		String[] patchBranches = getPatchBranches();
		for (String patchBranch : patchBranches) {
			String[] bra = patchBranch.split(IGenericConstants.COLON);
			if (bra.length > 1) {
				patchBrNames.add(bra[0]);
			}
		}
		return patchBrNames.toArray(new String[0]);
	}

	public static String[] getArrayByString(String stringList) {
		StringTokenizer st = new StringTokenizer(stringList, IGenericConstants.ROW_SEPARATOR + "\n\r");//$NON-NLS-1$
		ArrayList<Object> v = new ArrayList<Object>();
		while (st.hasMoreElements()) {
			v.add(st.nextElement());
		}
		return v.toArray(new String[v.size()]);
	}

}
