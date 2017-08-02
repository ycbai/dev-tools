package com.byc.tools.patch.prefs;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;

import com.byc.tools.patch.PatchPlugin;
import com.byc.tools.patch.constants.IGenericConstants;

public class PreferenceUtils {

	public final static String PREF_KEY_GIT_REPS = "GRS";

	public final static String PREF_KEY_BRANCHES = "PBS";

	private static IPreferenceStore store = PatchPlugin.getDefault().getPreferenceStore();

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

	public static String[] getArrayByString(String stringList) {
		StringTokenizer st = new StringTokenizer(stringList, IGenericConstants.ROW_SEPARATOR + "\n\r");//$NON-NLS-1$
		ArrayList<Object> v = new ArrayList<Object>();
		while (st.hasMoreElements()) {
			v.add(st.nextElement());
		}
		return v.toArray(new String[v.size()]);
	}

}
