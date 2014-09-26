package cn.core.mobile.library.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.ConnectionClosedException;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import cn.core.mobile.library.exception.BaseException;
import cn.core.mobile.library.http.callback.BaseResponseCallback;

/**
 * @author Wu.Will </br> Create at 2014/6/6
 * @version 1.1
 */
public class HttpUtils {
	private static final String TAG = "HttpUtilsV2";

	public static final int STATUS_CODE_BASE = 20000;
	public static final int STATUS_CODE_CONNECTION_CANCELED = STATUS_CODE_BASE + 1;
	public static final int STATUS_CODE_URL_ERROR = STATUS_CODE_BASE + 2;
	public static final int STATUS_CODE_SERVER_ERROR = STATUS_CODE_BASE + 3;
	public static final int STATUS_CODE_404_ERROR = STATUS_CODE_BASE + 4;
	public static final int STATUS_CODE_CONNECTION_ERROR = STATUS_CODE_BASE + 5;
	public static final int STATUS_CODE_TIMEOUT = STATUS_CODE_BASE + 6;
	public static final int STATUS_CODE_JSON_FORMAT_ERROR = STATUS_CODE_BASE + 7;
	public static final int STATUS_CODE_NET_ERROR = STATUS_CODE_BASE + 8;

	private static final String ERR_MSG_URL_ERROR = "访问地址错误";
	private static final String ERR_MSG_SERVER_ERROR = "服务器异常";
	private static final String ERR_MSG_404_ERROR = "访问地址不存在(404)";
	private static final String ERR_MSG_TIMEOUT = "服务器连接超时";
	private static final String ERR_MSG_JSON_FORMAT = "返回报文格式错误";
	private static final String ERR_MSG_NET_ERROR = "网络异常,请检查网络连接!";

	private static Gson json = new Gson();

	private static <T> void request(Context context, String method, String url, List<NameValuePair> params, final TypeToken<T> typeToken, final BaseResponseCallback<T> callback) {
		if (Config.sEnableLogging) {
			Ion.getDefault(context).configure().setLogging(TAG, Log.DEBUG);
		}
		Ion.with(context).load(method, url)
                .setTimeout(Config.sTimeout)
                .setHeader("User-Agent" ,"Mozilla/4.04")
                .setBodyParameters(buildParametersForIon(params))
                .asByteArray()
                .withResponse().
				setCallback(new FutureCallback<Response<byte[]>>() {
					@Override
					public void onCompleted(Exception error, Response<byte[]> response) {
						if (error != null) {
							BaseException e = buildException(error);
							e.printStackTrace();
							callback.onCompleted(e, null);
						} else {
							int statusCode = response.getHeaders().getResponseCode();
							if (statusCode == 200) {
								String responseBody = new String(response.getResult());
								String resultData=decodeUnicode(responseBody) ;
								if (Config.sEnableLogging) {
									Log.d("responseBody", "responseBody="+responseBody);
									Log.d("resultData", "resultData="+resultData);
								}
								T result = null;
								try {
									result=json.fromJson(resultData, typeToken.getType());
//									result=json.fromJson(responseBody, typeToken.getType());
								} catch (Exception e) {
									e.printStackTrace();
								}
								if(result!=null){
									String code = ((cn.core.mobile.library.remote.response.Response) result).getCode();
									if (code != null && code.equalsIgnoreCase("ok")) {
										callback.onCompleted(null, result);
										Log.d(TAG, "result"+result);
									} else {
										BaseException e = new BaseException(code+":"+((cn.core.mobile.library.remote.response.Response) result).getMsg());
										e.printStackTrace();
										callback.onCompleted(e, null);
									}
								}else{
									String msg = "返回数据格式错误";
									BaseException e = new BaseException(statusCode, msg);
									e.printStackTrace();
									callback.onCompleted(e, null);
								}
							} else if (statusCode == 404) {
								BaseException e = new BaseException(STATUS_CODE_404_ERROR, ERR_MSG_404_ERROR);
								e.printStackTrace();
								callback.onCompleted(e, null);
							} else if (statusCode >= 500) {
								String msg = ERR_MSG_SERVER_ERROR + "(" + statusCode + ")";
								BaseException e = new BaseException(statusCode, msg);
								e.printStackTrace();
								callback.onCompleted(e, null);
							} else {
								String msg = "未知错误" + "(" + statusCode + ")";
								BaseException e = new BaseException(statusCode, msg);
								e.printStackTrace();
								callback.onCompleted(e, null);
							}
						}
					}
				});
	}

	/**
	 * 同步请求,非异步操作
	 * 
	 * @param context
	 * @param urlString
	 * @param params
	 * @return 请求结果的json字串
	 */
	private static <T> Response<T> syncRequest(Context context, String method, String urlString, List<NameValuePair> params, final TypeToken<T> typeToken) {
		try {
			if (Config.sEnableLogging) {
				Ion.getDefault(context).configure().setLogging(TAG, Log.DEBUG);
			}
			Response<T> ret = Ion.with(context)
					.load(method, urlString)
					.setTimeout(Config.sTimeout)
					.setHeader("User-Agent" ,"Mozilla/4.04")
					.setBodyParameters(buildParametersForIon(params))
					.as(typeToken).withResponse().get();
			return ret;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * post请求
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param typeToken
	 * @param callback
	 * @param <T>
	 */
	public static <T> void post(final Context context, String url, List<NameValuePair> params, final TypeToken<T> typeToken, final BaseResponseCallback<T> callback) {
		request(context, AsyncHttpPost.METHOD, url, params, typeToken, callback);
	}

	/**
	 * get请求
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param typeToken
	 * @param callback
	 * @param <T>
	 */
	public static <T> void get(final Context context, String url, List<NameValuePair> params, final TypeToken<T> typeToken, final BaseResponseCallback<T> callback) {
		request(context, AsyncHttpGet.METHOD, url, params, typeToken, callback);
	}

	public static <T> Response<T> syncPost(Context context, String urlString, List<NameValuePair> params, final TypeToken<T> typeToken) {
		return syncRequest(context, AsyncHttpPost.METHOD, urlString, params, typeToken);
	}

	public static <T> Response<T> syncGet(Context context, String urlString, List<NameValuePair> params, final TypeToken<T> typeToken) {
		return syncRequest(context, AsyncHttpGet.METHOD, urlString, params, typeToken);
	}

	/**
	 * 终止请求
	 * 
	 * @param context
	 *            context
	 * @author Will.Wu
	 */
	public static void cancelRequests(Context context) {
		Ion.getDefault(context).cancelAll(context);
	}

	/**
	 * 检查网络连接
	 * 
	 * @return
	 * @author Will.Wu
	 */
	public static boolean checkInternetConnection(Context context) {
		NetworkInfo info;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		info = manager.getActiveNetworkInfo();
		return info != null && info.isAvailable();
	}

	private static Map<String, List<String>> buildParametersForIon(List<NameValuePair> pairs) {
		Map<String, List<String>> newParams = new HashMap<String, List<String>>();
		if (pairs != null) {
			for (NameValuePair pair : pairs) {
				newParams.put(pair.getName(), Arrays.asList(pair.getValue() == null ? "" : pair.getValue()));
			}
		}
		return newParams;
	}

	private static BaseException buildException(Throwable err) {
		BaseException exception;
		// 取消请求异常
		if (err instanceof CancellationException) {
			exception = new BaseException(STATUS_CODE_CONNECTION_CANCELED, "");
		} else if (err instanceof TimeoutException) {
			// 链接查实异常
			exception = new BaseException(STATUS_CODE_TIMEOUT, ERR_MSG_TIMEOUT);
		} else if (err instanceof JsonSyntaxException) {
			// Json解析异常
			exception = new BaseException(STATUS_CODE_JSON_FORMAT_ERROR, ERR_MSG_JSON_FORMAT);
		} else if (err.getMessage() != null && err.getMessage().equals("Invalid URI")) {
			// 无效链接地址异常
			exception = new BaseException(STATUS_CODE_URL_ERROR, ERR_MSG_URL_ERROR);
		} else if (err instanceof ConnectionClosedException) {
			// 无可用的的网络连接
			exception = new BaseException(STATUS_CODE_NET_ERROR, ERR_MSG_NET_ERROR);
		} else {
			// 其他异常
			exception = new BaseException(err.getMessage());
		}
		return exception;
	}

	public static class Config {
		static int sTimeout = 30000;
		static List<NameValuePair> myHeaders = new ArrayList<NameValuePair>();
		static boolean sEnableLogging = true;

		/**
		 * 设置所有请求的连接超时
		 * 
		 * @param millsocn
		 */
		public static void setTimeout(int millsocn) {
			sTimeout = millsocn;
		}

		/**
		 * 设置所有请求的请求头信息
		 */
		public static void setHeader(String name, String value) {
			if (name == null || value == null) {
				throw new IllegalArgumentException("name == null or value == null");
			}
			myHeaders.add(new BasicNameValuePair(name, value.trim()));
		}

		/**
		 * 是否显示log信息
		 * 
		 * @param enable
		 */
		public static void setEnableLogging(boolean enable) {
			sEnableLogging = enable;
		}
	}
	private static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
}
