package com.maogousoft.logisticsmobile.driver.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;

/**
 * 自定义对话框
 * 
 * @author lenovo
 */
public class MyAlertDialog extends AlertDialog {

	public MyAlertDialog(Context context) {
		super(context, R.style.DialogTheme);
	}

	private TextView mTitle;

	private TextView mMessage;

    private EditText mInput;

	private Button button1, button2, button3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater mInflater = LayoutInflater.from(getContext());
		View view = mInflater.inflate(R.layout.alert_dialog, null);
		mTitle = (TextView) view.findViewById(android.R.id.title);
		mMessage = (TextView) view.findViewById(android.R.id.message);
        mInput = (EditText) view.findViewById(android.R.id.input);
		button1 = (Button) view.findViewById(android.R.id.button1);
		button2 = (Button) view.findViewById(android.R.id.button2);
		button3 = (Button) view.findViewById(android.R.id.button3);
		button1.setVisibility(View.GONE);
		button2.setVisibility(View.GONE);
		button3.setVisibility(View.GONE);
		setContentView(view);
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		if (mTitle != null) {
			mTitle.setText(title);
		}
	}

	public void setMessage(CharSequence message) {
		if (mMessage != null) {
			mMessage.setText(message);
		}
	}

    public void displayInputView () {
        mInput.setVisibility(View.VISIBLE);
    }

    public EditText getInputView() {
        return mInput;
    }

	public void setLeftButton(String text, android.view.View.OnClickListener listener) {
		if (button1 == null) {
			return;
		}
		button1.setText(text);
		button1.setVisibility(View.VISIBLE);
		if (listener != null)
			button1.setOnClickListener(listener);
	}

	public void setRightButton(String text, android.view.View.OnClickListener listener) {
		button2.setText(text);
		button2.setVisibility(View.VISIBLE);
		if (listener != null)
			button2.setOnClickListener(listener);
	}

	public void setCenterButton(String text, android.view.View.OnClickListener listener) {
		if (button3 == null) {
			return;
		}
		button3.setText(text);
		button3.setVisibility(View.VISIBLE);
		if (listener != null)
			button3.setOnClickListener(listener);
	}
}
