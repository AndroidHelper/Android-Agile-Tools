package cn.core.mobile.library.keyword;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 自定义显示密码的TextView,不能进行输入
 * 实现显示密码后,2秒后以*表示
 * @ClassName: PasswordTextView 
 * @author haoran.shu 
 * @date 2014年6月12日 上午10:52:27 
 * @version 1.0
 *
 */
public class PasswordTextView extends TextView {
	private final String sing = "*";//密文显示的内容
	private String content = "";//显示的内容
	//文本改变事件回调接口
	private OnTextChangedListener onTextChangedListener;
	/**
	 * Handler线程对象,用来更新密码框的显示内容
	 * 实现将输入的内容显示为密文
	 */
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//密文显示
			PasswordTextView.this.setText(sing);
			//回调改变事件接口
			if(onTextChangedListener != null){
				onTextChangedListener.textChanged(content);
			}
		};
	};
	
	/**
	 * 构造方法
	 * @param context
	 * @param attrs
	 */
	public PasswordTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 设置文本改变事件监听
	 * @param onTextChangedListener
	 */
	public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener){
		this.onTextChangedListener = onTextChangedListener;
	}
	
	/**
	 * 设置密码框显示的内容
	 * @param text
	 */
	public void setTextContent(String text){
		//设置显示的内容
		this.content = text;
		if(!TextUtils.isEmpty(text)){
			//设置初始显示的内容
			this.setText(text);
			//开启线程,等待2秒,2秒后更新显示
			new Thread(new UpdateRunnable()).start();
		}else{
			this.setText("");
		}
	}
	
	/**
	 * 获取显示的内容
	 * @return
	 */
	public String getTextContent(){
		return content;
	}
	
	
	/**
	 * 自定义线程实现向Handler发送消息,更新显示
	 * @ClassName: UpdateRunnable 
	 * @author haoran.shu 
	 * @date 2014年6月12日 上午10:59:17 
	 * @version 1.0
	 *
	 */
	private class UpdateRunnable implements Runnable{
		@Override
		public void run() {
			try {
				Thread.sleep(150);//等待150豪秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0);//向Handler发送消息
		}
	}
	
	/**
	 * 文本改变事件接口
	 * @ClassName: OnTextChangedListener 
	 * @author haoran.shu 
	 * @date 2014年6月12日 上午11:37:17 
	 * @version 1.0
	 *
	 */
	public interface OnTextChangedListener{
		/**
		 * 密码框文本改变时调用
		 * @param content
		 */
		public void textChanged(String content);
	}

}
