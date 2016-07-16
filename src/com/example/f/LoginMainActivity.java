package com.example.f;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contact.UserInfo;
import com.example.path.Path;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 注册界面
 * @author Administrator
 *
 */
public class LoginMainActivity extends Activity implements OnClickListener {
	EditText ed1, ed2, ed3;
	Button btn_zc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}


	private void initView() {
		ed1 = (EditText) findViewById(R.id.ed1);//用户名
		ed2 = (EditText) findViewById(R.id.ed2);//密码
		ed3 = (EditText) findViewById(R.id.ed3);
		btn_zc = (Button) findViewById(R.id.bt1);
		btn_zc.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	
	@Override
	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt1:
			String name=ed1.getText().toString().trim();
			String pass=ed2.getText().toString().trim();
			if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)){
				Toast.makeText(getApplicationContext(), "用户名或者密码为空...", 0).show();
				return;
			}
				getDataFromServer(name,pass);
				break;

			default:
				break;
			}
	}

	private void getDataFromServer(String name, String pass) {
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("name", name);
		params.addBodyParameter("pwd", pass);
		utils.send(HttpMethod.POST, Path.login, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String s) {
						Log.i("TAG", "post请求失败" + s.toString());
						Toast.makeText(LoginMainActivity.this, "登陆失败结果:"+s.toString(), 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> s) {
						Log.i("TAG", "post请求成功" + s.result);
						Toast.makeText(LoginMainActivity.this, "登陆成功结果:"+s.result, 0).show();
						String result=s.result;
						try {
							JSONObject jobj=new JSONObject(result);
							String token=jobj.getString("result");
							UserInfo.setToken(getApplicationContext(), token);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

}
