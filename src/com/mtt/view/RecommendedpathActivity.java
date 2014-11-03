package com.mtt.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mtt.R;

public class RecommendedpathActivity extends Activity implements LeftFragment.Callbacks{

	private FragmentManager fmanager;
	private FragmentTransaction ftransaction;
	private List<String> data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommendedpath);
		data = new ArrayList<String>();
		data.add(getString(R.string.path0));
		data.add(getString(R.string.path1));
		data.add(getString(R.string.path2));
		data.add(getString(R.string.path3));
		Bundle b1 = new Bundle();
		b1.putStringArrayList("data", (ArrayList<String>) data);
		
		fmanager = getFragmentManager();
		ftransaction = fmanager.beginTransaction();
		LeftFragment lf = new LeftFragment();
		lf.setArguments(b1);
		ftransaction.add(R.id.ll_left, lf, "left");		
		ftransaction.commit();
	}
	/**
	 * 返回更多页面
	 */
	public void backmore(View v)
	{
		Intent i = new Intent();
		i.setClass(RecommendedpathActivity.this, MoreActivity.class);
		RecommendedpathActivity.this.startActivity(i);		
	}
	
	// 实现Callbacks接口必须实现的方法
		@Override
		public void onItemSelected(Integer id)
		{
			// 创建Bundle，准备向Fragment传入参数
			Bundle arguments = new Bundle();
			arguments.putInt("id", id);
			String[] data_content = {getString(R.string.path0_content),
					 getString(R.string.path1_content),
					 getString(R.string.path2_content),
					 getString(R.string.path3_content)};
			arguments.putStringArray("data_content", data_content);
			// 创建BookDetailFragment对象
			DetailFragment df = new DetailFragment();
			// 向Fragment传入参数
			df.setArguments(arguments);
			// 使用fragment替换book_detail_container容器当前显示的Fragment
			getFragmentManager().beginTransaction()
				.replace(R.id.ll_right, df, "right")
				.commit();  //①
		}
	
}
