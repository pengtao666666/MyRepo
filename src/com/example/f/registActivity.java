package com.example.f;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
 * 用户注册界面
 * 
 * @author Administrator
 * 
 */
public class registActivity extends Activity {
	private EditText et_username;// 用户名
	private EditText et_password;// 密码
	private EditText et_nick;// 昵称
	private EditText et_phone;// 电话号码
	private RadioGroup rg_type;// 用户类型
	private ImageView iv_head;// 用户头像
	private Button btn_regist;// 注册按钮
	private String user_type = "GENERAL_USER";// 默认为普通用户，通过单选按钮切换类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		initData();
	}

	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_nick = (EditText) findViewById(R.id.et_nick);
		et_phone = (EditText) findViewById(R.id.et_phone);
		rg_type = (RadioGroup) findViewById(R.id.rg_type);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		btn_regist = (Button) findViewById(R.id.btn_regist);
	}

	private void initData() {
		String token = UserInfo.getToken(getApplicationContext());
		Toast.makeText(getApplicationContext(), "token:" + token, 0).show();
		rg_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 获取变更后的选中项的ID
				int radioButtonId = group.getCheckedRadioButtonId();
				switch (radioButtonId) {
				case R.id.rb_nomal:// 切换到普通用户
					user_type = "GENERAL_USER";
					Log.i("rg", "GENERAL_USER");
					break;
				case R.id.rb_admin:// 切换到管理员
					user_type = "ADMINISTRATOR";
					Log.i("rg", "ADMINISTRATOR");
					break;
				case R.id.rb_vip:// 切换到VIP
					user_type = "VIP_USER";
					Log.i("rg", "VIP_USER");
					break;

				case R.id.rb_svip:// 切换到SVIP
					user_type = "SVIP_USER";
					Log.i("rg", "SVIP_USER");
					break;
				default:
					break;
				}
			}
		});

		// 注册的点击事件
		btn_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDataFromServer();
			}
		});
	}

	/**
	 * 请求服务器
	 */
	private void getDataFromServer() {
		String username = et_username.getText().toString().trim();
		String pass = et_password.getText().toString().trim();
		String nick = et_nick.getText().toString().trim();
		String phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pass)
				|| TextUtils.isEmpty(nick) || TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "用户注册信息不完整...", 0).show();
			return;
		}
//07-03 11:10:31.117: I/TAG(889): post请求失败Unsupported Media Type
		// 否则就请求接口，将用户信息传入服务器
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userDef", user_type);
		params.addBodyParameter("name", username);
		params.addBodyParameter("pwd", pass);
		params.addBodyParameter("alias", nick);
		params.addBodyParameter("mobile", phone);
		params.addBodyParameter("id", "");
		params.addBodyParameter("createTime", System.currentTimeMillis()+"");
		params.addBodyParameter("roleIds", "");
		params.addBodyParameter("headPortrait", "");// 这里的头像到时候你自己传
		utils.send(HttpMethod.POST, Path.Regist, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String s) {
						Log.i("TAG", "post请求失败" + s.toString());
						Toast.makeText(registActivity.this,
								"注册失败结果:" + s.toString(), 0).show();
						Intent i = new Intent(registActivity.this,
								LoginMainActivity.class);
						startActivity(i);
					}

					@Override
					public void onSuccess(ResponseInfo<String> s) {
						Log.i("TAG", "post请求成功" + s.result);
						Toast.makeText(registActivity.this,
								"注册成功结果:" + s.result, 0).show();
						// 请求成功需要把用户的token保存。在这里处理返回的信息哈，解析数据
						try {
							JSONObject jobj = new JSONObject(s.result);
							Intent i = new Intent(registActivity.this,
									LoginMainActivity.class);
							startActivity(i);
							// jobj.get(name)
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}
