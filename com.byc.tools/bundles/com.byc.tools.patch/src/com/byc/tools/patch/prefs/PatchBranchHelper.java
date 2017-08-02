package com.byc.tools.patch.prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.byc.tools.patch.constants.IGenericConstants;

public class PatchBranchHelper {

	public static String writeString(List<String> items) {
		if (items == null || items.size() == 0) {
			return null;
		}
		int size = items.size();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < size; i++) {
			buf.append(items.get(i));
			if (i != size - 1)
				buf.append(IGenericConstants.ROW_SEPARATOR);
		}
		return buf.toString();
	}

	public static List<String> readString(String stringList) {
		if (stringList == null || "".equals(stringList)) //$NON-NLS-1$
			return Collections.emptyList();
		List<String> result = new ArrayList<String>();
		for (String item : stringList.split(IGenericConstants.ROW_SEPARATOR)) {
			result.add(item);
		}
		return result;
	}

	public static String getBranchName(String item) {
		if (item == null || item.length() == 0) {
			return IGenericConstants.EMPTY;
		}
        String[] itemArray = item.split(IGenericConstants.COLON);
        return itemArray[0];
    }

}
