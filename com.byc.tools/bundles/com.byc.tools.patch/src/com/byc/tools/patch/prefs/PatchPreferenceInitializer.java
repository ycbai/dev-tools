package com.byc.tools.patch.prefs;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.byc.tools.patch.PatchPlugin;

public class PatchPreferenceInitializer extends AbstractPreferenceInitializer {

	private static String gitReps = null;

	private static String patchBrs = null;

	public PatchPreferenceInitializer() {
		gitReps = "Talend/tcommon-studio-se;Talend/tcommon-studio-ee;Talend/tdi-studio-se;Talend/tdi-studio-ee;Talend/tbd-studio-se;Talend/tbd-studio-ee;Talend/tesb-studio-se;Talend/tesb-studio-ee";
		patchBrs = "patch/6.3.1:Talend/tcommon-studio-ee#168a4fc,Talend/tesb-studio-ee#42445eb,Talend/tcommon-studio-se#c01c79a,Talend/tdi-studio-ee#87a6593,Talend/tbd-studio-ee#b7d0461,Talend/tdi-studio-se#2d84ef0,Talend/tbd-studio-se#2c2e413,Talend/tesb-studio-se#9a9878f;patch/6.4.1:Talend/tcommon-studio-ee#3e70906,Talend/tesb-studio-ee#8acae3a,Talend/tcommon-studio-se#d9b3a17,Talend/tdi-studio-ee#d442964,Talend/tbd-studio-ee#98d8074,Talend/tdi-studio-se#2169974,Talend/tbd-studio-se#2a7498a,Talend/tesb-studio-se#12fca24";
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = PatchPlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceUtils.PREF_KEY_GIT_REPS, gitReps);
		store.setDefault(PreferenceUtils.PREF_KEY_BRANCHES, patchBrs);
	}

}
