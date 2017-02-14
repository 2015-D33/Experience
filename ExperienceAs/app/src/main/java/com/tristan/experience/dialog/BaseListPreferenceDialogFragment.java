package com.tristan.experience.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * @author hanguodong @ Zhihu Inc.
 * @since 02-14-2017
 *
 * listPreference dialog形式显示 无需依赖 PreferenceFragment、 PreferenceActivity
 */

public abstract class BaseListPreferenceDialogFragment<T> extends AppCompatDialogFragment {

	protected CharSequence[] mEntryTitles;

	protected T[] mEntryValues;

	protected T mValue;

	protected int mCheckedIndex;

	@StringRes
	protected abstract int setTitleId();

	@ArrayRes
	protected abstract int setEntriesArrayId();

	protected String[] setEntryies(){
		return null;
	}

	protected abstract T[] setEntryValus();

	/**
	 * 取 sp 值
	 * @return
	 */
	protected abstract T getValue();

	/**
	 * 存 sp 值
	 */
	protected abstract void OnValueChecked(T value);

	@Override public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mValue = getValue();
		if (setEntryies() != null && setEntryies().length > 0) {
			mEntryTitles = setEntryies();
		} else {
			mEntryTitles = getResources().getStringArray(setEntriesArrayId());
		}
		mEntryValues = setEntryValus();
	}

	@Override public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(getString(setTitleId()));
		dialog.setPositiveButton(null, null);
		mCheckedIndex = getValueIndex();
		dialog.setSingleChoiceItems(mEntryTitles, mCheckedIndex, selectItemListener);
		dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				dismissAllowingStateLoss();
			}
		});
		return dialog.create();
	}

	private int getValueIndex() {
		return findIndexOfValue(mValue);
	}

	public int findIndexOfValue(T value) {
		if (value != null && mEntryValues != null) {
			for (int i = mEntryValues.length - 1; i >= 0; i--) {
				if (mEntryValues[i].equals(value)) {
					return i;
				}
			}
		}
		return -1;
	}

	DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener() {

		@Override public void onClick(DialogInterface dialog, int which) {
			if (mCheckedIndex != which) {
				mCheckedIndex = which;
				mValue = mEntryValues[mCheckedIndex];
				OnValueChecked(mValue);
			}
			dismissAllowingStateLoss();
		}
	};
}
