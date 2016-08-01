package com.byc.tools.patch.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

import com.byc.tools.patch.PatchPlugin;

public class ExceptionStatusUtils {

	public static MultiStatus getStatus(Throwable t) {
		List<Status> childStatuses = new ArrayList<>();
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		for (StackTraceElement stackTrace : stackTraces) {
			Status status = new Status(IStatus.ERROR, PatchPlugin.PLUGIN_ID, stackTrace.toString());
			childStatuses.add(status);
		}
		MultiStatus ms = new MultiStatus(PatchPlugin.PLUGIN_ID, IStatus.ERROR, childStatuses.toArray(new Status[] {}),
				t.toString(), t);
		return ms;
	}

}
