package com.byc.tools.patch.prefs;

import java.util.List;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.byc.tools.patch.constants.IGenericConstants;
import com.byc.tools.patch.ui.common.TableEditor;

public class PatchBranchFieldEditor extends TableEditor {

	private String[] repositoryNames;

	public PatchBranchFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
		repositoryNames = PreferenceUtils.getGitRepsURLs();
	}

	@Override
	protected Table createTable(Composite parent) {
		Table patchBranchTable = new Table(parent, SWT.BORDER | SWT.SINGLE);
		patchBranchTable.setLinesVisible(true);
		patchBranchTable.setHeaderVisible(true);

		TableColumn branchNameColumn = new TableColumn(patchBranchTable, SWT.NONE);
		branchNameColumn.setText("Branch Name"); //$NON-NLS-1$
		branchNameColumn.setWidth(150);

		return patchBranchTable;
	}

	@Override
	protected IStructuredContentProvider createContentProvider() {
		return new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				return ((List) inputElement).toArray();
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

		};
	}

	@Override
	protected ITableLabelProvider createLabelProvider() {
		return new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				String item = ((String) element);
				if (columnIndex == 0) {
					return PatchBranchHelper.getBranchName(item);
				}
				throw new IllegalStateException();
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}

		};
	}

	@Override
	protected String writeString(List<String> items) {
		return PatchBranchHelper.writeString(items);
	}

	@Override
	protected String getNewInputObject() {
		PatchBranchConfigDialog dialog = new PatchBranchConfigDialog(getShell(), repositoryNames);
		if (dialog.open() == Window.OK) {
			return dialog.getString();
		}
		return null;
	}

	@Override
	protected String getExistingInputObject(String item) {
		PatchBranchConfigDialog dialog = new PatchBranchConfigDialog(getShell(), repositoryNames, item);
		if (dialog.open() == Window.OK) {
			return dialog.getString();
		}
		return null;
	}

	@Override
	protected List<String> readString(String stringList) {
		return PatchBranchHelper.readString(stringList);
	}

	public void setRepositoryNames(String[] repositoryNames) {
		this.repositoryNames = repositoryNames;
	}

}
