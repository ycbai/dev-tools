package com.byc.tools.patch.ui.wizards;

import java.io.File;
import java.util.ArrayList;
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

	private Text patchPathText;

	private Button sourcePathButton;

	private Button targetPathButton;

	private Button exportButton;

	private Text versionText;

	private Text targetPatchPathText;

	private Composite container;

	private Composite exportComposite;

	private boolean doExport;

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
		dataPathLabel.setText("Patch Path");

		patchPathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		patchPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		sourcePathButton = new Button(container, SWT.PUSH);
		sourcePathButton.setText("Browse...");

		Label versionLabel = new Label(container, SWT.NONE);
		versionLabel.setText("Target Version");

		versionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(container, SWT.NONE);

		exportButton = new Button(container, SWT.CHECK);
		exportButton.setText("Export");

		exportComposite = new Composite(container, SWT.NONE);
		GridLayout exportCompLayout = new GridLayout();
		exportCompLayout.numColumns = 2;
		exportCompLayout.marginWidth = 0;
		exportCompLayout.marginHeight = 0;
		exportComposite.setLayout(exportCompLayout);
		GridData exportCompGD = new GridData(GridData.FILL_HORIZONTAL);
		exportCompGD.horizontalSpan = 2;
		exportComposite.setLayoutData(exportCompGD);
		exportComposite.setVisible(false);

		targetPatchPathText = new Text(exportComposite, SWT.BORDER | SWT.SINGLE);
		targetPatchPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		targetPathButton = new Button(exportComposite, SWT.PUSH);
		targetPathButton.setText("Browse...");

		addListeners();
		updatePageStatus();

		setControl(container);
		setPageComplete(false);
	}

	private void addListeners() {
		patchPathText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updatePageStatus();
			}
		});
		sourcePathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dial = new DirectoryDialog(getShell(), SWT.NONE);
				String directory = dial.open();
				if (StringUtils.isNotEmpty(directory)) {
					patchPathText.setText(directory);
					List<File> jarFiles = new ArrayList<>();
					try {
						jarFiles = PatchFileUtil.getJarFiles(new File(directory));
						if (jarFiles == null || jarFiles.isEmpty()) {
							throw new PatchException("There is not any patch file in " + directory);
						}
					} catch (PatchException ex) {
						ExceptionLogger.log(ex);
					}
					patchInfo.setJarFiles(jarFiles);
					String defaultVersion = getDefaultVersion();
					if (defaultVersion != null) {
						versionText.setText(defaultVersion);
					}
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
		exportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportComposite.setVisible(exportButton.getSelection());
				setDoExport(exportButton.getSelection());
			}
		});
		targetPatchPathText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				patchInfo.setTargetPath(targetPatchPathText.getText());
				updatePageStatus();
			}
		});
		targetPathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
				fileDialog.setText("Please select the exported path of the patch");
				fileDialog.setFilterExtensions(new String[] { ".zip" });
				String path = fileDialog.open();
				if (StringUtils.isNotEmpty(path)) {
					targetPatchPathText.setText(path);
				}
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

	public boolean isDoExport() {
		return doExport;
	}

	public void setDoExport(boolean doExport) {
		this.doExport = doExport;
	}

}