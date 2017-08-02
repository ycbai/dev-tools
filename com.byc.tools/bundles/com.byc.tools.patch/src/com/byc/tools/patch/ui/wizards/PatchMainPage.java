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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.prefs.PreferenceUtils;
import com.byc.tools.patch.utils.PatchFileUtil;

/**
 *
 * @author ycbai
 *
 */
public class PatchMainPage extends AbstractMakePatchPage {

	private Button oldPatchSelBtn;

	private Button newPatchSelBtn;

	private Text pluginsPathText;

	private Button pluginsPathButton;

	private Button patchPathButton;

	private Label versionLabel;

	private Text versionText;

	private Text patchPathText;

	private Label patchBranchLabel;

	private Combo patchBranchCombo;

	private boolean isNewPatch = true;

	public PatchMainPage(PatchInfo patchInfo) {
		super("PatchMainPage", patchInfo);
		setTitle("Make Patch");
		setDescription("Please fill infos.");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout containerLayout = new GridLayout();
		container.setLayout(containerLayout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite btnComp = new Composite(container, SWT.NONE);
		GridLayout btnCompLayout = new GridLayout();
		btnCompLayout.numColumns = 2;
		btnComp.setLayout(btnCompLayout);

		oldPatchSelBtn = new Button(btnComp, SWT.RADIO);
		oldPatchSelBtn.setText("Old Patch");
		oldPatchSelBtn.setSelection(false);

		newPatchSelBtn = new Button(btnComp, SWT.RADIO);
		newPatchSelBtn.setText("New Patch");
		newPatchSelBtn.setSelection(true);

		Composite patchContainer = new Composite(container, SWT.NONE);
		patchContainer.setLayout(new GridLayout());
		patchContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		createPatchPanel(patchContainer);

		addListeners();
		updatePageUI();
		updatePageStatus();

		setControl(container);
		setPageComplete(false);
	}

	private void createPatchPanel(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label dataPathLabel = new Label(container, SWT.NONE);
		dataPathLabel.setText("Plugins Path");

		pluginsPathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		pluginsPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pluginsPathButton = new Button(container, SWT.PUSH);
		pluginsPathButton.setText("Browse...");

		versionLabel = new Label(container, SWT.NONE);
		versionLabel.setText("Target Version");
		versionLabel.setLayoutData(new GridData());

		versionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		patchBranchLabel = new Label(container, SWT.NONE);
		patchBranchLabel.setText("Patch Branch");
		patchBranchLabel.setLayoutData(new GridData());

		patchBranchCombo = new Combo(container, SWT.NONE);
		patchBranchCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		String[] patchBranchNames = PreferenceUtils.getPatchBranchNames();
		patchBranchCombo.setItems(patchBranchNames);
		if (patchBranchNames.length > 0) {
			patchBranchCombo.select(0);
		}
		
		new Label(container, SWT.NONE);

		Label patchPathLabel = new Label(container, SWT.NONE);
		patchPathLabel.setText("Patch Path");

		patchPathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		patchPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		patchPathText.setEditable(false);

		patchPathButton = new Button(container, SWT.PUSH);
		patchPathButton.setText("Browse...");
	}

	private void addListeners() {
		oldPatchSelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isNewPatch = false;
				updatePageUI();
				updatePageStatus();
			}
		});
		newPatchSelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isNewPatch = true;
				updatePageUI();
				updatePageStatus();
			}
		});
		patchBranchCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				patchInfo.setPatchBranch(patchBranchCombo.getText());
				updatePageStatus();
			}
		});
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
		patchInfo.setPluginsFolder(new File(pluginsPath));
		fillDefaultVersion();
	}

	private void updatePageUI() {
		hideControl(versionLabel, isNewPatch);
		hideControl(versionText, isNewPatch);
		hideControl(patchBranchLabel, !isNewPatch);
		hideControl(patchBranchCombo, !isNewPatch);
	}

	private void updatePageStatus() {
		setErrorMessage(null);
		if (StringUtils.isBlank(pluginsPathText.getText())) {
			setErrorMessage("Plugins Path cannot be null!");
			return;
		}
		if (patchInfo.getJarFiles() == null || patchInfo.getJarFiles().isEmpty()) {
			setErrorMessage("There is not any plugin in " + pluginsPathText.getText());
			return;
		}
		if (versionText.isVisible() && StringUtils.isBlank(versionText.getText())) {
			setErrorMessage("Target version cannot be null!");
			return;
		}
		if (patchBranchCombo.isVisible() && StringUtils.isBlank(patchBranchCombo.getText())) {
			setErrorMessage("Patch Branch cannot be null!");
			return;
		}
		if (StringUtils.isBlank(patchPathText.getText())) {
			setErrorMessage("Patch Path cannot be null!");
			return;
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

	public boolean isNewPatch() {
		return isNewPatch;
	}

}