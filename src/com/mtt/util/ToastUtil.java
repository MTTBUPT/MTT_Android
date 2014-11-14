
package com.mtt.util;

import android.content.Context;
import android.widget.Toast;
/**
 * Toast 显示工具类
 */
public class ToastUtil {

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}
}
