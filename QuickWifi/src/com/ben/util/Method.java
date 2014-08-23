package com.ben.util;


public class Method {
	public static String xproc1(String str) {
		String EChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String out;
		int c1, c2, c3;
		int len = str.length();
		int i = 0;
		out = "";
		while (i < len) {
			c1 = charCodeAt(i++, str) & 0xff;
			if (i == len) {
				out += EChars.charAt(c1 >> 2);
				out += EChars.charAt((c1 & 0x3) << 4);
				out += "==";
				break;
			}
			c2 = charCodeAt(i++, str);
			if (i == len) {
				out += EChars.charAt(c1 >> 2);
				out += EChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
				out += EChars.charAt((c2 & 0xF) << 2);
				out += "=";
				break;
			}
			c3 = charCodeAt(i++, str);
			out += EChars.charAt(c1 >> 2);
			out += EChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
			out += EChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
			out += EChars.charAt(c3 & 0x3F);
		}
		return out;
	}

	public static int charCodeAt(int index, String str) {
		char[] c = str.toCharArray();
		return (int) c[index];
	}
}
