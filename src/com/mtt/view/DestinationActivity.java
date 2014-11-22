package com.mtt.view;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.mtt.R;
import com.mtt.util.ToastUtil;

/**
 * AMapV1地图中简单介绍poisearch搜索
 */
public class DestinationActivity extends FragmentActivity implements
		OnMarkerClickListener, InfoWindowAdapter, OnInfoWindowClickListener,TextWatcher,
		OnPoiSearchListener, OnClickListener, OnMapClickListener{
	
	private AMap aMap;
	private AutoCompleteTextView searchText;// 输入搜索关键字
	private String keyWord = "";// 要输入的poi搜索关键字
	private ProgressDialog progDialog = null;// 搜索时进度条
	private PoiResult poiResult; // poi返回的结果
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索

	/** 地图选点按钮*/
	private ImageButton mapChoseButton;
	/** 返回上一页*/
	private ImageButton mapReturn;
	/** 是否使用地图选点*/
	private boolean isClickTarget = false;
	/** 是否在地图上选好了点*/
	private boolean isMarked = false;
	/** 地图上选择的终点*/
	private Marker EndMarker;
	/** 获取Poi图层*/
	private PoiOverlay poiOverlay; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.destination_map)).getMap();
			setUpMap();
		}
	}

	/**
	 * 设置页面监听
	 */
	private void setUpMap() {
		// search按钮
		ImageButton searButton = (ImageButton) findViewById(R.id.destination_search);
		searButton.setOnClickListener(this);
		
		// 地图选点
		mapChoseButton = (ImageButton) findViewById(R.id.destination_mapchoose);
		mapChoseButton.setOnClickListener(this);
		
		// 搜索输入框
		searchText = (AutoCompleteTextView) findViewById(R.id.destination_input);
		searchText.addTextChangedListener(this);// 添加文本输入框监听事件
		
		// 返回
		mapReturn = (ImageButton) findViewById(R.id.destination_return);
		mapReturn.setOnClickListener(this);
		
		registerListener();
	}

	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		keyWord = checkEditText(searchText);
		if ("".equals(keyWord)) {
			ToastUtil.show(DestinationActivity.this, "请输入搜索关键字");
			return;
		} else {
			doSearchQuery();
		}
	}

	/**
	 * 判断edittext是否null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索:\n" + keyWord);
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		showProgressDialog();// 显示进度框
		currentPage = 0;
		query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);// 设置查询监听接口
		poiSearch.searchPOIAsyn();// 查询POI异步接口
	}

	/** 点击Mark后显示信息*/
	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	// 获取Marker点的信息窗口
	@Override
	public View getInfoWindow(final Marker marker) {
		View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
				null); 
		
		// Marker头部
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());

		// Marker片段
		TextView snippet = (TextView) view.findViewById(R.id.snippet);
		snippet.setText(marker.getSnippet());
		
		// 跳转按钮
		Button button = (Button) view
				.findViewById(R.id.start_amap_app);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LatLng mEndLatLng = marker.getPosition();
				double mlatitude = mEndLatLng.latitude;
				double mlongtitude = mEndLatLng.longitude;
				
				Intent intent = getIntent();
				Bundle mBundle = new Bundle();
				mBundle.putDouble("mlatitude", mlatitude);
				mBundle.putDouble("mlongtitude", mlongtitude);
				intent.putExtras(mBundle);
				
				DestinationActivity.this.setResult(1, intent);
				DestinationActivity.this.finish();
			}
		});
		return view;
	}


	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.show(DestinationActivity.this, infomation);
	}

	/** -------------搜索服务--------搜索框监听----------------------*/
	
	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	// 当输入框中的信息变化时
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		// 输入提示类（联网才能使用）
		Inputtips inputTips = new Inputtips(DestinationActivity.this,
				new InputtipsListener() {

					/** 输入提示回调方法
					 * @para tipList 输入提示接口回调的提示列表
					 * @para rCode 返回结果（0为成功，其他为失败） */
					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						if (rCode == 0) {// 正确返回
							List<String> listString = new ArrayList<String>();
							for (int i = 0; i < tipList.size(); i++) {
								listString.add(tipList.get(i).getName());
							}
							ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
									getApplicationContext(),
									R.layout.route_inputs, listString);
							// 在输入框下绑定提示list
							searchText.setAdapter(aAdapter);
							// 提示观察者，底层数据已经改变了
							aAdapter.notifyDataSetChanged();
						}
					}
				});
		try {
			// 发送输入提示请求
			inputTips.requestInputtips(newText, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号

		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	/** -------------搜索服务--------POI查询详情回调----------------------*/

	/**
	 * POI详情查询回调方法
	 */
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int rCode) {

	}

	/**
	 * POI信息查询回调方法
	 * @param result POI的搜索结果，POI搜索结果是分页显示的，从第0页开始，每页最多30个POI
	 * @param rCode 返回结果（0为成功，其他为失败）
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();// 隐藏对话框
		
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条查询结果
					poiResult = result;
					
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						// 获取Poi图层
						poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();// 去掉PoiOverlay上所有的Marker
						poiOverlay.addToMap();// 添加Marker到地图中
						poiOverlay.zoomToSpan();// 移动镜头到当前视角
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities); // 未搜索到兴趣点时展示有搜索关键字的城市信息
					} else {
						ToastUtil.show(DestinationActivity.this,
								"no result");
					}
				}
			} else {
				ToastUtil.show(DestinationActivity.this,
						"no result");
			}
		} else if (rCode == 27) {
			ToastUtil.show(DestinationActivity.this,
					"error network");
		} else if (rCode == 32) {
			ToastUtil.show(DestinationActivity.this, "error key");
		} else {
			ToastUtil.show(DestinationActivity.this, "error other" + rCode);
		}

	}

	/** 
	 * 在地图上选择终点
	 * */
	private void getEndPoint(){
		if(!isClickTarget){
			ToastUtil.show(DestinationActivity.this, "在地图上点击你的终点");
			isClickTarget = true;
			aMap.clear();
			registerListener();
		}else{
			ToastUtil.show(DestinationActivity.this, "通过搜索选择你的终点");
			isClickTarget = false;
			if(isMarked){
				EndMarker.destroy();
			}
		}
		
	}
	
	/** 注册监听*/
	private void registerListener(){
		aMap.setOnMapClickListener(this);
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
	}
	
	/**
	 * Button点击事件回调方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 点击搜索按钮
		 */
		case R.id.destination_search:
			if(isMarked){
				EndMarker.destroy();
			}
			if(isClickTarget){
				isClickTarget = false;
			}
			searchButton();
			break;
		/**
		 * 点击下一页按钮
		 */
		case R.id.destination_mapchoose:
			getEndPoint();
			break;
		/** 
		 * 点击返回按钮
		 * */
		case R.id.destination_return:
			Intent intent = new Intent(DestinationActivity.this,SubFunctionActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onMapClick(LatLng latLng) {
		// TODO Auto-generated method stub
		if(isClickTarget){
			EndMarker = aMap.addMarker(new MarkerOptions()
											.anchor(0.5f, 1).icon(BitmapDescriptorFactory
											.fromResource(R.drawable.point)).position(latLng)
											.title("点击选择为目的地"));
			EndMarker.showInfoWindow();
			isMarked = true;
		}
	}
	
}

