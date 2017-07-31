package com.byc.tools.patch.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.byc.tools.patch.PatchPlugin;

public class PatchPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PatchPreferencePage() {
		super();
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(PatchPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		addField(new GenericListEditor(PreferenceUtils.PREF_KEY_GIT_REPS, "Git Repositories", "Git Repository URL", getFieldEditorParent()));
		addField(new GenericListEditor(PreferenceUtils.PREF_KEY_BRANCH_NAMES, "Patch Branches", "Branch name", getFieldEditorParent()));
	}

}
