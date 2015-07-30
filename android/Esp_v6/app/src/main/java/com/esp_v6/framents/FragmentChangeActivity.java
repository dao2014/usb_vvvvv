package com.esp_v6.framents;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.esp_v6.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class FragmentChangeActivity extends BaseActivity {
	
	private Fragment mContent;
	
	public FragmentChangeActivity() {
		super(R.string.changing_fragments);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContent = new ChargerMainFrament(R.color.white);
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new ColorMenuFragment())
		.commit();
		//设置 右边菜单
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setSecondaryMenu(R.layout.right_frame);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.right_frame, new RightFragment())
				.commit();
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		sm.setShadowDrawable(R.drawable.shadow);

		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	public void switchContent(Fragment fragment) {
		if(fragment!=null) {
			mContent = fragment;
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, fragment)
					.commit();
		}
		getSlidingMenu().showContent();
	}

}
