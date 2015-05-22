package com.yisi.yisiHome.baseFragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseActivity.YisiApp;
import com.yisi.yisiHome.baseEntity.EntityVersion;
import com.yisi.yisiHome.baseServer.UpdateService;
import com.yisi.yisiHome.utils.Constants;
import com.yisi.yisiHome.utils.DialogUtils;
import com.yisi.yisiHome.utils.WebHelper;
import com.yisi.yisiHome.utils.DialogUtils.InitView;


@SuppressLint("ValidFragment")
public class UpdateFragment extends BaseFragment {

	private Context context = null;
	private Dialog updateAlert = null;
	private EntityVersion version;
	private TextView title;
	private TextView content;
	private Button confurm;
	private Button cancle;
	private Dialog note;
	private boolean noUpdate = false;
	private String upDateContent = "";
	final int update = 0x56;// 执行更新
	final int noupdate = 0x58;// 没有更新
	final int GET_UPDATE_MSG_ERR = 0x60;// 没有获取到version名

	public UpdateFragment(Context context) {
		this.context = context;
	}

	/**
	 * 执行更新
	 */
	private void toUpdate() {
		UpdateService service = new UpdateService(context);
		service.downLoad(version);
	}

	/**
	 * 检查是否有更新
	 */
	private boolean check() {

		String localVersion = "";
		try {
			String tem = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode
					+ "";
			if (tem != null) {
				localVersion = tem;
			}
		} catch (Exception e) {
			Log.i("message", "本地版本号获取失败!");
		}
		String url = Constants.URL_UPDATE;
		WebHelper<EntityVersion> helper = new WebHelper<EntityVersion>(
				EntityVersion.class);
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("verCode", localVersion));
			version = helper.getObject(url, params);
		} catch (Exception e) {
		}
		String versionName = "";
		if (version != null) {// connect success
			versionName = version.getVerCode();
			upDateContent = version.getVerContent();
		} else {// connect false
			noUpdate = true;
			handler.obtainMessage(GET_UPDATE_MSG_ERR).sendToTarget();
			return false;
		}
		if (Integer.valueOf(versionName)<=Integer.valueOf(localVersion)) {// no update
			noUpdate = true;
			return false;
		}
		noUpdate = false;
		return true;
	}

	/**
	 * 选择是否更新
	 */
	private void updating() {
		updateAlert = showDialog(new InitView() {
			
			@Override
			public void init(Window win) {
				content = (TextView) win.findViewById(R.id.content);
				 title = (TextView) win.findViewById(R.id.title);
				 content.setText(upDateContent);
				 confurm = (Button) win.findViewById(R.id.lay_left);
				 cancle = (Button) win.findViewById(R.id.lay_right);
				content.setText(version.getVerContent());
				confurm.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						toUpdate();
						updateAlert.dismiss();
					}
				});
				cancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						updateAlert.dismiss();
					}
				});
			}
		});
	}

	/**
	 * 没有更新
	 */
	private void noUpdate() {
		note = showDialog(new InitView() {

			@Override
			public void init(Window win) {
				content = (TextView) win.findViewById(R.id.content);
				 title = (TextView) win.findViewById(R.id.title);
				 content.setText(upDateContent);
				 confurm = (Button) win.findViewById(R.id.lay_left);
				 cancle = (Button) win.findViewById(R.id.lay_right);
				title.setText("未发现更新");
				content.setText("你现在已经是最新版本了");
				cancle.setVisibility(View.GONE);
				LayoutParams p = confurm.getLayoutParams();
				p.width = 200;
				confurm.setLayoutParams(p);
				confurm.setText("确认");
				confurm.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						note.dismiss();
					}
				});
			}
		});
	}

	/**
	 * 执行更新(调用接口)
	 * 
	 * @param 调用者的Activity
	 */
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			YisiApp.disMissProgressDialog();
			switch (msg.what) {
			case noupdate:
				noUpdate();
				break;
			case update:
				updating();
				break;
			case GET_UPDATE_MSG_ERR:
				YisiApp.tell(context, "访问出错,请重试");
//				YisiApp.disMissProgressDialog();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void update() {// 更新接口

		updateAlert = new Builder(context).create();
		final int update = 0x56;
		final int noupdate = 0x58;
		YisiApp.showProgressDialog(context, null, "正在获取新版本信息,请稍候...");
		new Thread() {
			@Override
			public void run() {
				if (check()) {// no update
					handler.obtainMessage(update).sendToTarget();
				} else if (noUpdate) {
					handler.obtainMessage(noupdate).sendToTarget();
				}
				super.run();
			}
		}.start();
	}

	private Dialog showDialog(InitView callBack) {// 显示弹出层
	// AlertDialog dialog = new Builder(context).create();
	// dialog.show();
	// YisiApp.disMissProgressDialog();
	// Window win = dialog.getWindow();
	// win.setContentView(R.layout.frame_dialog_update_layout);
	// content = (TextView) win.findViewById(R.id.content);
	// title = (TextView) win.findViewById(R.id.title);
	// content.setText(upDateContent);
	// confurm = (Button) win.findViewById(R.id.lay_left);
	// cancle = (Button) win.findViewById(R.id.lay_right);
		return DialogUtils.getInstance().showDialog(context,
				DialogUtils.WRAP_CONTENT, DialogUtils.WRAP_CONTENT,
				R.layout.frame_dialog_update_layout, callBack);
		// return dialog;
	}
}
