package com.byc.tools.patch.prefs;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

import com.byc.tools.patch.constants.IGenericConstants;

public class GenericListEditor extends ListEditor {

	private String lastPath;

	private String dialogMessage;

	public GenericListEditor(String name, String labelText, String dialogMessage, Composite parent) {
		init(name, labelText);
		this.dialogMessage = dialogMessage;
		createControl(parent);
	}

	@Override
	protected String createList(String[] items) {
		StringBuffer path = new StringBuffer("");//$NON-NLS-1$

		for (int i = 0; i < items.length; i++) {
			path.append(items[i]);
			path.append(IGenericConstants.ROW_SEPARATOR);
		}
		return path.toString();
	}

	@Override
	protected String getNewInputObject() {
		InputDialog dlg = new InputDialog(getShell(), "", dialogMessage, "", null);
		if (dlg.open() == InputDialog.OK) {
			String dir = dlg.getValue();
			if (dir != null) {
				dir = dir.trim();
				if (dir.length() == 0) {
					return null;
				}
				lastPath = dir;
			}
		}
		return lastPath;
	}

	@Override
	protected String[] parseString(String stringList) {
		return PreferenceUtils.getArrayByString(stringList);
	}

	@Override
	protected void selectionChanged() {
		super.selectionChanged();
		fireValueChanged(IGenericConstants.EVENT_REPS_CHANGED, null, getList().getItems());
	}

}
