package com.byc.tools.patch.ui.wizards;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.byc.tools.patch.model.PatchInfo;

/**
 * 
 * @author ycbai
 *
 */
public class ChangeVersionPage extends AbstractMakePatchPage {

	private Text patchPathText;

	private Button pathButton;

	private Text versionText;

	private Composite container;

	public ChangeVersionPage(PatchInfo patchInfo) {
		super("Change Version Page", patchInfo);
		setTitle("Change Version");
		setDescription("Please fill the version info.");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;

		Label dataPathLabel = new Label(container, SWT.NONE);
		dataPathLabel.setText("Patch Path");

		patchPathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		patchPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pathButton = new Button(container, SWT.PUSH);
		pathButton.setText("Browse...");

		Label versionLabel = new Label(container, SWT.NONE);
		versionLabel.setText("Target Version");

		versionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		addListeners();
		updatePageStatus();

		setControl(container);
		setPageComplete(false);
	}

	private void addListeners() {
		patchPathText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				patchInfo.setPath(patchPathText.getText());
				updatePageStatus();
			}
		});
		pathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dial = new DirectoryDialog(getShell(), SWT.NONE);
				String directory = dial.open();
				if (StringUtils.isNotEmpty(directory)) {
					patchPathText.setText(directory);
				}
			}
		});
		versionText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				patchInfo.setVersion(versionText.getText());
				updatePageStatus();
			}
		});
	}
	
	private void updatePageStatus() {
		boolean isOK = true;
		if ("".equals(patchPathText.getText())) {
			isOK = false;
		} else if ("".equals(versionText.getText())) {
			isOK = false;
		}
		setPageComplete(isOK);
	}

}