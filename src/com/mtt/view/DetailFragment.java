package com.mtt.view;

import com.mtt.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	private TextView textView;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_detail, null);
		textView = (TextView) view.findViewById(R.id.textView);

		Bundle bundle = getArguments();
		int i = bundle.getInt("id");
		String[] data_content = bundle.getStringArray("data_content");
		textView.setText(data_content[i]);
		
		return view;
	}

}