package cn.core.mobile.library.keyword.util;

import cn.core.library.mobile.R;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 数字键盘管理类 -- 单例模式
 * @ClassName: NkManager 
 * @author haoran.shu 
 * @date 2014年6月16日 上午11:43:53 
 * @version 1.0
 *
 */
public class NkManager implements OnClickListener {
	private static NkManager nkManager;
	//数字键盘中的每一个按键
	private Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six,
				    btn_seven, btn_eight, btn_neigh, btn_zero;
	private OnNumberClickListener onNumberClickListener;
	/**
	 * 私有的构造方法
	 */
	private NkManager(){
		
	}
	
	/**
	 * 对外提供的初始化方法
	 * @return
	 */
	public static NkManager getInstance(){
		if(nkManager == null){
			nkManager = new NkManager();
		}
		return nkManager;
	}
	
	/**
	 * 初始化布局文件
	 * @param ac
	 */
	public void initView(Activity ac, OnNumberClickListener onNumberClickListener){
		//初始化按钮
		btn_one = (Button) ac.findViewById(R.id.btn_one);//1
		btn_two = (Button) ac.findViewById(R.id.btn_two);//2
		btn_three = (Button) ac.findViewById(R.id.btn_three);//3
		btn_four = (Button) ac.findViewById(R.id.btn_four);//4
		btn_five = (Button) ac.findViewById(R.id.btn_five);//5
		btn_six = (Button) ac.findViewById(R.id.btn_six);//6
		btn_seven = (Button) ac.findViewById(R.id.btn_seven);//7
		btn_eight = (Button) ac.findViewById(R.id.btn_eight);//8
		btn_neigh = (Button) ac.findViewById(R.id.btn_neigh);//9
		btn_zero = (Button) ac.findViewById(R.id.btn_zero);//0
		//设置点击事件回调
		if(onNumberClickListener != null){
			this.onNumberClickListener = onNumberClickListener;
			btn_one.setOnClickListener(this);//1
			btn_two.setOnClickListener(this);//2
			btn_three.setOnClickListener(this);//3
			btn_four.setOnClickListener(this);//4
			btn_five.setOnClickListener(this);//5
			btn_six.setOnClickListener(this);//6
			btn_seven.setOnClickListener(this);//7
			btn_eight.setOnClickListener(this);//8
			btn_neigh.setOnClickListener(this);//9
			btn_zero.setOnClickListener(this);//0
		}
	}
	
	/**
	 * 数字按钮点击回调接口
	 * @ClassName: onNumberClickListener 
	 * @author haoran.shu 
	 * @date 2014年6月16日 上午11:58:12 
	 * @version 1.0
	 *
	 */
	public interface OnNumberClickListener{
		/**
		 * 点击的数字按钮
		 * @param number	返回点击的数字
		 */
		void clickedNumber(int number);
	}
	
	/**
	 * 按钮的点击事件回调
	 */
	@Override
	public void onClick(View v) {
		// 判断点击的按钮
		int id = v.getId();
		
		if(id==R.id.btn_one){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(1);
			}
		}else if(id==R.id.btn_two){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(2);
			}
		}else if(id==R.id.btn_three){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(3);
			}
		}else if(id==R.id.btn_four){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(4);
			}
		}else if(id==R.id.btn_five){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(5);
			}
		}else if(id==R.id.btn_six){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(6);
			}
		}else if(id==R.id.btn_seven){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(7);
			}
		}else if(id==R.id.btn_eight){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(8);
			}
		}else if(id==R.id.btn_neigh){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(9);
			}
		}else if(id==R.id.btn_zero){
			if(onNumberClickListener != null){
				onNumberClickListener.clickedNumber(0);
			}
		}
		
//		switch (v.getId()) {
//		case R.id.btn_one://1
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(1);
//			}
//			break;
//		case R.id.btn_two://2
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(2);
//			}		
//			break;
//		case R.id.btn_three://3
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(3);
//			}
//			break;
//		case R.id.btn_four://4
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(4);
//			}
//			break;
//		case R.id.btn_five://5
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(5);
//			}
//			break;
//		case R.id.btn_six://6
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(6);
//			}
//			break;
//		case R.id.btn_seven://7
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(7);
//			}
//			break;
//		case R.id.btn_eight://8
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(8);
//			}
//			break;
//		case R.id.btn_neigh://9
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(9);
//			}
//			break;
//		case R.id.btn_zero://0
//			if(onNumberClickListener != null){
//				onNumberClickListener.clickedNumber(0);
//			}
//			break;
//		default:
//			break;
//		}
	}
	
}
