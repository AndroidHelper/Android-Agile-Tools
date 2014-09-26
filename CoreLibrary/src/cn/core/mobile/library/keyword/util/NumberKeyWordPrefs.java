package cn.core.mobile.library.keyword.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences管理类--单例模式
 * @ClassName: MyPrefs 
 * @author haoran.shu 
 * @date 2014年6月12日 下午9:33:41 
 * @version 1.0
 *
 */
public class NumberKeyWordPrefs {
	public static final String PREF_NAME = "numberlock";
	private static NumberKeyWordPrefs myPrefs;//私有化
	private SharedPreferences sp;
	//提供私有的构造方法
	private NumberKeyWordPrefs(){}
	/**
	 * 对外提供的初始化方法
	 * @return
	 */
	public static NumberKeyWordPrefs getInstance(){
		//初始化自身对象
		if(myPrefs == null){
			myPrefs = new NumberKeyWordPrefs();
		}
		return myPrefs;
	}
	
	/**
	 * 初始化SharedPreferences对象
	 * @param context
	 */
	public NumberKeyWordPrefs initSharedPreferences(Context context){
		//获取SharedPreferences对象
		if(sp == null){
			sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		return myPrefs;
	}
	
	/**
	 * 向SharedPreferences中写入String类型的数据
	 * @param text
	 */
	public void writeString(String key, String value){
		//获取编辑器对象
		Editor editor = sp.edit();
		//写入数据
		editor.putString(key, value);
		editor.commit();//提交写入的数据
	}
	
	/**
	 * 根据key读取SharedPreferences中的String类型的数据
	 * @param key
	 * @return
	 */
	public String readString(String key){
		return sp.getString(key, "");
	}
	
	public void cleanString(){
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();//提交写入的数据
	}
}
