package com.esp.socket.util;

import java.util.Date;

public class DateUtil {
	public static void main( String[] args ){
		mm(new Date(),new Date("2015/06/24 14:39:01"));
	}
	
	/**
	 * 
	 * @param date2   主
	 * @param date	副
	 * @return
	 */
	public static long mm(Date date2,Date date){
		 
        long temp = date2.getTime() - date.getTime();    //相差毫秒数
        long hours = temp / 1000 / 3600;                //相差小时数
        long temp2 = temp % (1000 * 3600);
        long mins = temp2 / 1000 / 60;                    //相差分钟数
        long mm = mins * 60;                    //相差秒
        System.out.println("date2 与 date 相差" + hours + "小时"+ mins + "分钟"+mm);
		return mm;
	}
}
