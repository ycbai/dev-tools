package com.byc.tools.patch.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author ycbai
 *
 */
public class ChangeVersionPage extends WizardPage {
	
	private Text dataPathText;

	private Text versionText;

	private Composite container;

	public ChangeVersionPage() {
		super("Change Version Page");
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
		dataPathLabel.setText("Plugins Path");
		
		dataPathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		
		Button pathButton = new Button(container, SWT.PUSH);
		pathButton.setText("Browse...");
		
		Label versionLabel = new Label(container, SWT.NONE);
		versionLabel.setText("Target Version");

		versionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		
		// required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}
	
	public String getDataPath() {
		if (dataPathText != null && !dataPathText.isDisposed()) {
			return dataPathText.getText();
		}
		return "";
	}

	public String getVersion() {
		if (versionText != null && !versionText.isDisposed()) {
			return versionText.getText();
		}
		return "";
	}
	
}