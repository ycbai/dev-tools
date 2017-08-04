package com.byc.tools.patch.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.byc.tools.patch.PatchPlugin;

public class DevToolsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(PatchPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceUtils.PREF_KEY_GIT_USER_NAME, "Git Username", getFieldEditorParent()));
		Composite passwordFieldParent = getFieldEditorParent();
		StringFieldEditor passwordField = new StringFieldEditor(PreferenceUtils.PREF_KEY_GIT_PASSWORD, "Git Password", passwordFieldParent);
		Text passwordText = passwordField.getTextControl(passwordFieldParent);
		passwordText.setEchoChar('*');
		addField(passwordField);
	}

}
