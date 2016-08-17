package com.byc.tools.patch.ui.wizards;

import java.io.File;
import java.util.List;

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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.log.ExceptionLogger;
import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.utils.PatchFileUtil;

/**
 * 
 * @author ycbai
 *
 */
public class PatchMainPage extends AbstractMakePatchPage {

	private Text pluginsPathText;

	private Button pluginsPathButton;

	private Button patchPathButton;

	private Text versionText;

	private Text patchPathText;

	private Composite container;

	public PatchMainPage(PatchInfo patchInfo) {
		super("PatchMainPage", patchInfo);
		setTitle("Make Patch");
		setDescription("Please fill infos.");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;

		Label dataPathLabel = new Label(container, SWT.NONE);
		dataPathLabel.setText("Plugins Path");

		pluginsPathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		pluginsPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pluginsPathButton = new Button(container, SWT.PUSH);
		pluginsPathButton.setText("Browse...");

		Label versionLabel = new Label(container, SWT.NONE);
		versionLabel.setText("Target Version");

		versionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(container, SWT.NONE);

		Label patchPathLabel = new Label(container, SWT.NONE);
		patchPathLabel.setText("Patch Path");

		patchPathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		patchPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		patchPathText.setEditable(false);

		patchPathButton = new Button(container, SWT.PUSH);
		patchPathButton.setText("Browse...");

		addListeners();
		updatePageStatus();

		setControl(container);
		setPageComplete(false);
	}

	private void addListeners() {
		pluginsPathText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateJarFiles(pluginsPathText.getText());
				updatePageStatus();
			}
		});
		pluginsPathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dial = new DirectoryDialog(getShell(), SWT.NONE);
				String directory = dial.open();
				if (StringUtils.isNotEmpty(directory)) {
					pluginsPathText.setText(directory);
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
		patchPathText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				patchInfo.setTargetPath(patchPathText.getText());
				updatePageStatus();
			}
		});
		patchPathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
				fileDialog.setText("Please select the exported path of the patch");
				fileDialog.setFilterExtensions(new String[] { ".zip" });
				String path = fileDialog.open();
				if (StringUtils.isNotEmpty(path)) {
					patchPathText.setText(path);
				}
			}
		});
	}

	private void updateJarFiles(String pluginsPath) {
		try {
			List<File> jarFiles = PatchFileUtil.getJarFiles(new File(pluginsPath));
			patchInfo.setJarFiles(jarFiles);
			fillDefaultVersion();
		} catch (PatchException ex) {
			ExceptionLogger.log(ex);
		}
	}

	private void updatePageStatus() {
		setErrorMessage(null);
		List<File> jarFiles = patchInfo.getJarFiles();
		if (StringUtils.isBlank(pluginsPathText.getText())) {
			setErrorMessage("Plugins Path cannot be null!");
		} else if (jarFiles == null || jarFiles.isEmpty()) {
			setErrorMessage("There is not any plugin in " + pluginsPathText.getText());
		} else if (StringUtils.isBlank(versionText.getText())) {
			setErrorMessage("Target version cannot be null!");
		} else if (StringUtils.isBlank(patchPathText.getText())) {
			setErrorMessage("Patch Path cannot be null!");
		}
		setPageComplete(getErrorMessage() == null);
	}

	private String getDefaultVersion() {
		String version = null;
		List<File> jarFiles = patchInfo.getJarFiles();
		if (jarFiles != null && jarFiles.size() > 0) {
			for (File file : jarFiles) {
				version = PatchFileUtil.getPatchDefaultVersion(file.getName());
				if (version != null) {
					break;
				}
			}
		}
		return version;
	}

	private void fillDefaultVersion() {
		String defaultVersion = getDefaultVersion();
		if (defaultVersion != null) {
			versionText.setText(defaultVersion);
		}
	}

}