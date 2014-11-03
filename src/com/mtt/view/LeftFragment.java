package com.mtt.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mtt.R;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class LeftFragment extends ListFragment {

	private FragmentManager manager;
	private FragmentTransaction transaction;
	private ArrayAdapter<String> adapter;
	private List<String> data;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b1 = getArguments();
		data = (List<String>)b1.getStringArrayList("data");
		
//		data = new ArrayList<String>();
		
//		data.add(getString(R.string.path0));
//		data.add(getString(R.string.path1));
//		data.add(getString(R.string.path2));
//		data.add(getString(R.string.path3));
		manager = getFragmentManager();
		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.fragment_list_item
				, data);
		setListAdapter(adapter);
	}

	// 当该Fragment被添加、显示到Activity时，回调该方法
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		// 如果Activity没有实现Callbacks接口，抛出异常
		if (!(activity instanceof Callbacks))
		{
			throw new IllegalStateException(
				"LeftFragment所在的Activity必须实现Callbacks接口!");
		}
		// 把该Activity当成Callbacks对象
		mCallbacks = (Callbacks)activity;
	}
	
	// 当该Fragment从它所属的Activity中被删除时回调该方法
	@Override
	public void onDetach()
	{
		super.onDetach();
		// 将mCallbacks赋为null。
		mCallbacks = null;
	}
	private Callbacks mCallbacks;
	// 定义一个回调接口，该Fragment所在Activity需要实现该接口
	// 该Fragment将通过该接口与它所在的Activity交互
	public interface Callbacks
	{
		public void onItemSelected(Integer id);
	}
	// 当用户点击某列表项时激发该回调方法
	@Override
	public void onListItemClick(ListView listView
		, View view, int position, long id)
	{
		super.onListItemClick(listView, view, position, id);
		// 激发mCallbacks的onItemSelected方法
		mCallbacks.onItemSelected(position);
	}
//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//		
//		super.onListItemClick(l, v, position, id);
//		String str = adapter.getItem(position);
//		transaction = manager.beginTransaction();
//
//		DetailFragment detailFragment = new DetailFragment();
//		/**
//		 * 使用Bundle类存储传递数据
//		 */
//		Bundle bundle = new Bundle();
//		bundle.putInt("id", position);
//		detailFragment.setArguments(bundle);
//		transaction.replace(R.id.ll_right, detailFragment, "right");
//		transaction.commit();
//		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
//	}
}
