package org.ogai.util;

import org.ogai.core.Application;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Utiltites for interactions with Web layer
 *
 * @author Побединский Евгений
 *         23.03.14 17:54
 */
public class WebUtil {
	public static final String WEB_CODING = "UTF-8";

	private static final String URL_PARAM_FIRST_PATTERN = "%s=%s";
	private static final String URL_PARAM_PATTERN = "&%s=%s";

	/**
	 *
	 * @param request
	 * @return key value map of request params
	 */
	public static Map<String, String> getParams(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		Enumeration en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			result.put(key, request.getParameter(key));
		}
		return result;
	}

	/**
	 *
	 * @param request
	 * @return Строка запроса восстановленная по request
	 */
	public static String getUrlString(HttpServletRequest request) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder(request.getRequestURL());
		Enumeration en = request.getParameterNames();
		boolean isParamsStarted = false;
		while (en.hasMoreElements()) {
			String pattern = URL_PARAM_FIRST_PATTERN;
			if (!isParamsStarted) {
				sb.append("?");
				isParamsStarted = true;
			}  else {
				pattern = URL_PARAM_PATTERN;
			}
			String key = (String) en.nextElement();
			sb.append(String.format(pattern, key, encodeURL(request.getParameter(key))));
		}
		return sb.toString();
	}

	/**
	 *
	 * @param params
	 * @param isEncode - кодировать ли для url
	 * @return Строка с параметрами подготовленная для передачи в URL, не закодированная и без ? в начале
	 */
	public static String getUrlParamsString(Map<String,String> params, boolean isEncode) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		boolean isParamsStarted = false;
		for (String param : params.keySet()) {
			String pattern = URL_PARAM_FIRST_PATTERN;
			if (!isParamsStarted) {
				isParamsStarted = true;
			}  else {
				pattern = URL_PARAM_PATTERN;
			}
			String paramValue = params.get(param);
			paramValue = isEncode ? encodeURL(paramValue) : paramValue;
			sb.append(String.format(pattern, param, paramValue));
		}
		return sb.toString();
	}

	/**
	 *
	 * @param params
	 * @return Строка с параметрами подготовленная для передачи в URL, не закодированная и без ? в начале
	 */
	public static String getUrlParamsString(Map<String,String> params)  {
		try {
			return getUrlParamsString(params, false);
		} catch (UnsupportedEncodingException e) {
			//Это исключение никогда не должно возникнуть
			Application.log.error("Wtf, encoding error:" + e.getMessage(), e);
			return null;
		}
	}

	/**
	 *
	 * @param params
	 * @return Строка с параметрами подготовленная для передачи в URL, закодированная и c ? в начале
	 */
	public static String getParamsUrl(Map<String,String> params) throws UnsupportedEncodingException {
		return getUrlParamsString(params, true);
	}


	private static String encodeURL(String parameter) throws UnsupportedEncodingException {
		return URLEncoder.encode(parameter, "UTF-8");
	}

	/**
	 *
	 * @param s - source string
	 * @param s1 - coding (for example UTF-8)
	 * @return decoded URL
	 */
	public static String decodeURL(String s, String s1) {
		boolean flag = false;
		StringBuilder stringbuffer = new StringBuilder();
		int i = s.length();
		int j = 0;
		try {
			do {
				if (j >= i)
					break;
				char c = s.charAt(j);
				switch (c) {
					case 43: // '+'
						stringbuffer.append(' ');
						j++;
						flag = true;
						break;

					case 37: // '%'
						try {
							byte abyte0[] = new byte[(i - j) / 3];
							int k = 0;
							do {
								if (j + 2 >= i || c != '%')
									break;
								abyte0[k++] = (byte) Integer.parseInt(s.substring(j + 1, j + 3), 16);
								if ((j += 3) < i)
									c = s.charAt(j);
							} while (true);
							if (j < i && c == '%')
								throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
							stringbuffer.append(new String(abyte0, 0, k, s1));
						} catch (NumberFormatException numberformatexception) {
							throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - " + numberformatexception.getMessage());
						}
						flag = true;
						break;

					default:
						stringbuffer.append(c);
						j++;
						break;
				}
			} while (true);
		} catch (Exception e) {
			//log.error("Error on decodeURL", e);
		}
		return flag ? stringbuffer.toString() : s;
	}
}
