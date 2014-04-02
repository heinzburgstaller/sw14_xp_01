package com.appsrox.messenger.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {

	public static String generateUID(int len) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random(System.currentTimeMillis());
		for (int i=0; i<len; i++) {
			if (i%2 != len%2) {//alphabet
				sb.append((char)(random.nextInt(26)+65));
				
			} else {//digit
				sb.append(String.valueOf(random.nextInt(10)));
			}
		}
		return sb.toString();
	}
	
	public static List<String> strToList(String str) {
		List<String> lst = new ArrayList<String>();
		if (str == null || "".equals(str)) 
			return lst;
		
		String[] arr = str.split(",");
		for(String item : arr) {
			lst.add(item);
		}
		return lst;
	}
	
	public static String listToStr(List<String> lst) {
		if (lst == null || lst.isEmpty()) 
			return "";		
		
		StringBuilder sb = new StringBuilder();
		for(String item : lst) {
			sb.append(",").append(item);
		}
		return sb.substring(1).toString();
	}
}
