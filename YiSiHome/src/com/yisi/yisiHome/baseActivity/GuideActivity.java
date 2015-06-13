package com.yisi.yisiHome.baseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseEntity.EntityUser;
import com.yisi.yisiHome.baseServer.DataBaseUpdateServer;
import com.yisi.yisiHome.entity.EntityICCard;
import com.yisi.yisiHome.utils.AESHelper;
import com.yisi.yisiHome.utils.Constants;

@SuppressLint("HandlerLeak")
public class GuideActivity extends Activity {
	/** 开始初始化 */
	private static final int START_INIT_GUIDE = 0;
	/** 开始登陆 */
	private static final int START_LOGIN = 1;
	/** 当前页面 */
	private static final int CUR_PAGE = 2;
	private int[] pageViewImages;
	private boolean isUpdating=false;
	private ViewPager pager;
	private ArrayList<View> pageViews;
	private RelativeLayout guideWelcom;
	boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_layout);
		startWelcomeAnmi();
		initView();
	}

	private void initData() {
		DbUtils utils=DbUtils.create(this);
		EntityUser user=new EntityUser(0, "15034010946", AESHelper.encrypt("hyq"),Constants.ADD_OR_UPDATE,System.currentTimeMillis());
		try {
			utils.save(user);
			System.out.println(utils.findAll(EntityUser.class).get(0).getName());
		} catch (DbException e) {
			e.printStackTrace();
		}
//		DBManager<EntityUser> dbm=new DBManager<EntityUser>(this);
//		try {
//			dbm.createTables(EntityUser.class,EntityICCard.class,DBTimeStamp.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//		UserServer userServer=new UserServer(this);
//		userServer.saveUser(new EntityUser(0, "15034010946", AESHelper.encrypt("hyq"),Constants.ADD_OR_UPDATE,System.currentTimeMillis()));
//		update();
//		if(NetUtils.getConnectedType(this)>0){//有网
//			update();
//		}else{
//			
//		}
	}

	private void update() {
		DataBaseUpdateServer dbUpdateServer=new DataBaseUpdateServer();
		
		Map<String, Class> classMap=new HashMap<String, Class>();
		classMap.put(EntityICCard.class.getSimpleName(), EntityICCard.class);
		Map<String, String> urls=new HashMap<String, String>();
		urls.put(EntityICCard.class.getSimpleName(), Constants.URL_CRASH_SEND);
		dbUpdateServer.update(this, classMap, urls);
//		AsyncTask<Void, Void, String> task=new AsyncTask<Void, Void, String>(){
//			@Override
//			protected void onPreExecute() {
//				isUpdating=true;
//				super.onPreExecute();
//			}
//			@Override
//			protected String doInBackground(Void... params) {
////				new UserServer(GuideActivity.this).upDateUser();
////				new ICCardServer(GuideActivity.this).update();
//				return null;
//			}
//			@Override
//			protected void onPostExecute(String result) {
//				isUpdating=false;
//				super.onPostExecute(result);
//			}
//			
//		};
//		task.execute();
	}

	private void initView() {
		pageViews = new ArrayList<View>();
		pageViewImages = new int[] { R.drawable.welcome_help_01,
				R.drawable.welcome_help_02 };
		pager = (ViewPager) findViewById(R.id.guideViewPager);
		guideWelcom = (RelativeLayout) findViewById(R.id.guide_welcom);
		// if(guideWelcom!=null){//测试模式，要加引导时切换
		if (Boolean.valueOf(YisiApp.getValue(Constants.HAS_INITED, "false"))) {// TODO
			handler.sendEmptyMessageDelayed(START_LOGIN, 3000);
		} else {
			findViewById(R.id.animation_layout).setVisibility(View.GONE);
			handler.sendEmptyMessageDelayed(START_INIT_GUIDE, 3000);
			for (int i = 0; i < pageViewImages.length; i++) {
				LinearLayout layout = new LinearLayout(this);
				layout.setBackgroundResource(pageViewImages[i]);
				pageViews.add(layout);
			}
			GuidePagerAdapter adapter = new GuidePagerAdapter(pageViews);
			pager.setAdapter(adapter);
			pager.setOnPageChangeListener(new OnPageChangeListener() {
				private int curPage;
				private boolean locked=false;

				@Override
				public void onPageSelected(int index) {
					curPage = index;
				}

				@Override
				public void onPageScrolled(int paramInt1, float paramFloat,
						int paramInt2) {

				}

				@Override
				public void onPageScrollStateChanged(int state) {
					if (state == ViewPager.SCROLL_STATE_IDLE
							&& curPage == pager.getAdapter().getCount() - 1&&!locked) {
						locked=true;
						if (isUpdating) {
							YisiApp.showProgressDialog(GuideActivity.this, "正在更新数据，请稍候", "数据更新中……");
						}
						while (isUpdating) {}
						YisiApp.setValue(Constants.HAS_INITED, true + "");
						handler.sendEmptyMessageDelayed(START_LOGIN, 100);
					}
				}
			});
			initData();
		}
	}
/************************************************************/
	/**
	 * 进入登陆界面
	 */
	public void goLogin(View v) {
		YisiApp.disMissProgressDialog();
		Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	class GuidePagerAdapter extends PagerAdapter {
		ArrayList<View> myLayouts;

		public GuidePagerAdapter(ArrayList<View> layouts) {
			myLayouts = layouts;
		}

		@Override
		public int getCount() {
			return myLayouts.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(myLayouts.get(position), 0);
			return myLayouts.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(myLayouts.get(position));
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START_LOGIN: {
				goLogin(null);
				break;
			}
			case START_INIT_GUIDE: {
				guideWelcom.setVisibility(View.GONE);
				break;
			}
			case CUR_PAGE: {
				pager.setCurrentItem(msg.arg1);
				break;
			}
			}
		}
	};

	private void startWelcomeAnmi() {
		Drawable draw = ((ImageView) findViewById(R.id.start_animation))
				.getBackground();
		AnimationDrawable ad = (AnimationDrawable) draw;
		ad.setOneShot(false);
		ad.start();
	}
}
