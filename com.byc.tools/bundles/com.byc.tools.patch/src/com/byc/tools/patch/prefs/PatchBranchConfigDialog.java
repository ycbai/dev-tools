package com.byc.tools.patch.prefs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.byc.tools.patch.constants.IGenericConstants;

public class PatchBranchConfigDialog extends Dialog {

	private String[] repositoryNames;

	private String initString;

	private String branchName;

	private Map<String, String> rep2sha1Map;

	private RepositoryListener repositoryListener;

	public PatchBranchConfigDialog(Shell parentShell, String[] repositoryNames) {
		this(parentShell, repositoryNames, null);
	}

	public PatchBranchConfigDialog(Shell parentShell, String[] repositoryNames, String initString) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.APPLICATION_MODAL);
		this.repositoryNames = repositoryNames;
		this.initString = initString;
		rep2sha1Map = new HashMap<>();
		repositoryListener = new RepositoryListener();
		init();
	}

	private void init() {
		if (initString != null) {
			String[] brca = initString.split(IGenericConstants.COLON);
			if (brca.length > 1) {
				branchName = brca[0];
				String brcs = brca[1];
				String[] rep2sha1Array = brcs.split(IGenericConstants.COMMA);
				for (String rep2sha1 : rep2sha1Array) {
					String[] r2sArray = rep2sha1.split(IGenericConstants.WN);
					if (r2sArray.length > 1) {
						rep2sha1Map.put(r2sArray[0], r2sArray[1]);
					}
				}
			}
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Patch Branch Configration");
		newShell.setSize(450, 500);
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();

		Point size = getShell().getSize();
		Point location = getInitialLocation(size);
		getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);

		Label branchNameLabel = new Label(composite, SWT.None);
		branchNameLabel.setText("Branch Name");
		Text branchNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		branchNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (branchName != null) {
			branchNameText.setText(branchName);
		}
		branchNameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				branchName = branchNameText.getText();
			}
		});

		if (repositoryNames != null) {
			ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);
			scrolledComposite.setLayout(new GridLayout());
			GridData scrolledGridData = new GridData(GridData.FILL_BOTH);
			scrolledGridData.horizontalSpan = 2;
			scrolledComposite.setLayoutData(scrolledGridData);
			Composite repositoryCompoiste = new Composite(scrolledComposite, SWT.NONE);
			GridLayout repositoryGridLayout = new GridLayout();
			repositoryGridLayout.numColumns = 2;
			repositoryGridLayout.marginWidth = 0;
			repositoryCompoiste.setLayout(repositoryGridLayout);
			repositoryCompoiste.setLayoutData(new GridData(GridData.FILL_BOTH));

			scrolledComposite.setContent(repositoryCompoiste);
			scrolledComposite.setExpandVertical(true);
			scrolledComposite.setExpandHorizontal(true);
			scrolledComposite.setMinSize(200, 250);

			for (String repositoryName : repositoryNames) {
				createRepositoryText(repositoryCompoiste, repositoryName);
			}
		}

		return composite;
	}

	private void createRepositoryText(Composite parent, String repName) {
		Label repNameLabel = new Label(parent, SWT.None);
		repNameLabel.setText(repName);
		Text sha1Text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		sha1Text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sha1Text.setData(repName);
		String sha1 = rep2sha1Map.get(repName);
		if (sha1 != null) {
			sha1Text.setText(sha1);
		}
		sha1Text.addModifyListener(repositoryListener);
	}

	public String getString() {
		StringBuffer sb = new StringBuffer();
		if (branchName != null) {
			sb.append(branchName);
			sb.append(IGenericConstants.COLON);
		}
		Set<Entry<String, String>> rep2sha1EntrySet = rep2sha1Map.entrySet();
		Iterator<Entry<String, String>> rep2sha1Iterator = rep2sha1EntrySet.iterator();
		int count = 0;
		int size = rep2sha1Map.size();
		while (rep2sha1Iterator.hasNext()) {
			Entry<String, String> rep2sha1 = rep2sha1Iterator.next();
			sb.append(rep2sha1.getKey());
			sb.append(IGenericConstants.WN);
			sb.append(rep2sha1.getValue());
			count++;
			if (count < size) {
				sb.append(IGenericConstants.COMMA);
			}
		}
		return sb.toString();
	}

	class RepositoryListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			Text sourceText = (Text)e.getSource();
			String repName = String.valueOf(sourceText.getData());
			String sha1 = sourceText.getText();
			rep2sha1Map.put(repName, sha1);
		}

	}

}
