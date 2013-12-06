package com.crtb.measure;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private Button mLoginBtn;
	private EditText mUserNameText;
	private EditText mPasswrodText;
	private SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mPreferences = getSharedPreferences("crtb.xml", MODE_PRIVATE);
		mUserNameText = (EditText) findViewById(R.id.username);
		mPasswrodText = (EditText) findViewById(R.id.password);
		mUserNameText.setText(mPreferences.getString("username", "xcbjsbdygq"));
		mPasswrodText.setText(mPreferences.getString("password", "123"));
		mLoginBtn = (Button) findViewById(R.id.login);
		mLoginBtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		String userName = mUserNameText.getText().toString();
		String password = mPasswrodText.getText().toString();
		
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "用户或密码不能为空", Toast.LENGTH_SHORT).show();
			return ;
		}
		Editor editor = mPreferences.edit();
		editor.putString("username", userName);
		editor.putString("password", password);
		editor.commit();
		startActivity(intent);
		finish();
	}
	
}
