package com.byc.tools.patch.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.byc.tools.patch.PatchPlugin;
import com.byc.tools.patch.constants.IGenericConstants;

public class PatchPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private PatchBranchFieldEditor patchBranchEditor;

	public PatchPreferencePage() {
		super();
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(PatchPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		GenericListEditor repListEditor = new GenericListEditor(PreferenceUtils.PREF_KEY_GIT_REPS, "Git Repositories", "Git Repository URL", getFieldEditorParent());
		addField(repListEditor);
		patchBranchEditor = new PatchBranchFieldEditor(PreferenceUtils.PREF_KEY_BRANCHES, "Patch Branches", getFieldEditorParent());
		addField(patchBranchEditor);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String propertyName = event.getProperty();
		if (IGenericConstants.EVENT_REPS_CHANGED.equals(propertyName)) {
			patchBranchEditor.setRepositoryNames((String[])event.getNewValue());
		}
	}

}
