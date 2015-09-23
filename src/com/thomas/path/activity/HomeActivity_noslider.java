package com.thomas.path.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.thoams.path.BaseActivity;
import com.thoams.path.BaseApplication;
import com.thomas.path.R;
import com.thomas.path.manager.DialogManager;
import com.thomas.path.manager.TitleManager;

public class HomeActivity_noslider extends BaseActivity implements OnClickListener {
	private Button btnAddPerson;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		findViews();
		init();
		setListener();
	}

	@Override
	public void onRecvData(JsonObject response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		TitleManager.showTitle(this, new int[] { TitleManager.CAMERA },
				R.string.app_name, this);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		btnAddPerson = (Button) this.findViewById(R.id.btn_add_person);
		dialog = DialogManager.getCommWarnDialog(this, this);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		btnAddPerson.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {  
		case R.id.btn_add_person:
			addPerson();
			break;

		case R.id.warn_sure_bt:
			BaseApplication.mInstance.exit();
			break;
		default:
			break;
		}
	}

	private void addPerson() {

	}

	@Override
	public void onBackPressed() {
		dialog.show();
	}
}
