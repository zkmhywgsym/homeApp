package com.yisi.yisiHome.baseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseFragment.BaseFragment;
import com.yisi.yisiHome.baseFragment.BaseNFCFragment;
import com.yisi.yisiHome.baseFragment.MoreFragment;
import com.yisi.yisiHome.utils.Constants;

//主页
public class MainActivity extends FragmentActivity {
	// public String mode;// 模块
	public boolean leftMenuShowable = false;// 是否显示左菜单
	public boolean bottomMenuShowable = true;// 是否显示下菜单
	public boolean isLandScape = false;// 是否横屏
	private FragmentManager fragmentManager;
	private BaseFragment curFragment;
	private ListView navdrawer;
	private DrawerArrowDrawable drawerArrow;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private RadioGroup bottomMenu;
	private boolean switched;// 是否横竖切换

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR); // 设置无标题
		setContentView(R.layout.activity_main_layout);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		onResumeCheck();
	}

	// 如果有特殊需要要在刚进入主界面执行，写的这里
	private void onResumeCheck() {
	}


	private void initBottomMenu() {
		if (!bottomMenuShowable) {
			return;
		}
		findViewById(R.id.bottom_menu_layout).setVisibility(View.VISIBLE);
		bottomMenu = (RadioGroup) findViewById(R.id.main_tab_group);
		bottomMenu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (curFragment != null) {
					curFragment.menuChangeTo(checkedId);
				}
				switch (checkedId) {
				case R.id.main_tab_dispatch:
					otherFragment(new BaseNFCFragment());
					break;
				case R.id.main_tab_scanner:// 扫描
					break;
				case R.id.main_tab_about://
					otherFragment(new MoreFragment());
					break;
				default:
					break;
				}
			}
		});
	}

	// 左菜单
	private void initNavdrawer() {
		if (!leftMenuShowable) {
			return;
		}
		navdrawer.setVisibility(View.VISIBLE);
		LayoutParams lp = navdrawer.getLayoutParams();
		lp.width = (int) getResources().getDimension(R.dimen.navdrawer_width);
		navdrawer.setLayoutParams(lp);
		String[] items = getResources().getStringArray(R.array.main_left_menu);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		for (String item : items) {
			map.put("item", item);
		}
		data.add(map);
		navdrawer.setAdapter(new ArrayAdapter<String>(this,
				R.layout.activity_main_list_item_left_menu, R.id.item_text,
				items));
		navdrawer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> root, View view, int index,
					long position) {
				String text = ((TextView) view.findViewById(R.id.item_text))
						.getText().toString();
				if ("登陆".equals(text.trim())) {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				} else if (true) {

				}
			}
		});
		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			data.removeExtra(Constants.REQUEST_CODE);
			data.putExtra(Constants.REQUEST_CODE, requestCode);
			data.removeExtra(Constants.RESULT_CODE);
			data.putExtra(Constants.RESULT_CODE, resultCode);
			curFragment.onResult(data);
		}
	};

	AlertDialog dialog = null;
	private boolean isFullScreen;

	public void onClick(View view) {
		if (curFragment != null) {
			curFragment.doClick(view);
		}
		switch (view.getId()) {
		case R.id.btn_titleLeftButton:// 返回
			if (curFragment != null) {
				curFragment.doBack();
			}
			if (switched) {
				curFragment.onSwitch();
				switched = !switched;
			}
			if (fragmentManager.getBackStackEntryCount() > 0) {
				fragmentManager.popBackStack();
				if (fragmentManager.getBackStackEntryCount() > 1) {
					findViewById(R.id.btn_titleLeftButton).setVisibility(
							View.VISIBLE);
				} else {
					findViewById(R.id.btn_titleLeftButton).setVisibility(
							View.GONE);
				}
			} else {
				exit();
			}
			break;
		default:
			break;
		}
	}

	public void exit() {
		exitBy2Click();
	}

	/************************************************************/
	@Override
	protected void onNewIntent(Intent intent) {
		curFragment.doNewIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (curFragment != null) {
			curFragment.doSaveInstanceState(outState);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (curFragment != null) {
			curFragment.doRestoreInstanceState(savedInstanceState);
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void initView() {
		fragmentManager = getSupportFragmentManager();
		otherFragment(new BaseNFCFragment());
		navdrawer = (ListView) findViewById(R.id.navdrawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		initNavdrawer();
		initBottomMenu();
	}

	public BaseFragment getCurFragment() {
		return curFragment;
	}

	public void setCurFragment(BaseFragment curFragment) {
		this.curFragment = curFragment;
	}

	public void otherFragment(BaseFragment fragment) {
		findViewById(R.id.btn_titleLeftButton).setVisibility(View.GONE);
		fragmentManager.popBackStackImmediate();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if (curFragment!=null) {
			ft.detach(curFragment);
		}
		curFragment = fragment;
		ft.replace(android.R.id.tabcontent, fragment);
		ft.commit();
	}

	// public void otherActivity(Activity activity) {
	// findViewById(R.id.btn_titleLeftButton).setVisibility(View.GONE);
	// fragmentManager.popBackStackImmediate();
	// FragmentTransaction ft = fragmentManager.beginTransaction();
	// ft.replace(android.R.id.tabcontent, activity);
	// ft.commit();
	// }

	public void otherSwitchedFragment(BaseFragment fragment) {
		switched = true;
		curFragment.onSwitch();
		otherFragment(fragment);
	}

	public void otherBackableFragment(BaseFragment fragment) {
		findViewById(R.id.btn_titleLeftButton).setVisibility(View.VISIBLE);
		curFragment = fragment;
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(android.R.id.tabcontent, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void otherBackableSwitchedFragment(BaseFragment fragment) {
		switched = true;
		curFragment.onSwitch();
		otherBackableFragment(fragment);
	}

	public boolean isSwitched() {
		return switched;
	}

	@Override
	public void onBackPressed() {
		findViewById(R.id.btn_titleLeftButton).performClick();
	}

	// 切换全屏
	private void fullScreen() {
		findViewById(R.id.bottom_menu_layout).setVisibility(View.GONE);
		findViewById(R.id.title_layout).setVisibility(View.GONE);
	}

	// 切换窗口
	private void windows() {
		findViewById(R.id.bottom_menu_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void switchFullScreen() {
		if (isFullScreen) {
			windows();
		} else {
			fullScreen();
		}
		isFullScreen = !isFullScreen;
	}

	// 切换横竖屏
	public void switchScreenOrientation() {
		if (isLandScape) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		isLandScape = !isLandScape;
	}

	// 确认退出
	public void exitWithWin() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MainActivity.this);
		alertDialog.setTitle("退出");
		alertDialog.setPositiveButton("关闭",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		alertDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog = alertDialog.create();
		dialog.show();
	}

	private boolean isExit = false;

	// 双击退出
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}

	}

}
