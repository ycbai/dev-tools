package com.byc.tools.patch.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.byc.tools.patch.ui.wizards.MakePatchWizard;

/**
 * 
 * @author ycbai
 *
 */
public class MakePatchHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		WizardDialog dialog = new WizardDialog(window.getShell(), new MakePatchWizard());
		dialog.open();

		return null;
	}

}
