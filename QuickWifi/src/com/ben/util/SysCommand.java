package com.ben.util;

import java.io.IOException;

public class SysCommand {
	public static int pingHost(String str) {
		
//		  -d   使用Socket的SO_DEBUG功能。
//		  -c<完成次数>   设置完成要求回应的次数。
//		  -f   极限检测。
//		  -i<间隔秒数>   指定收发信息的间隔时间。
//		  -I<网络界面>   使用指定的网络界面送出数据包。
//		  -l<前置载入>   设置在送出要求信息之前，先行发出的数据包。
//		  -n   只输出数值。
//		  -p<范本样式>   设置填满数据包的范本样式。
//		  -q   不显示指令执行过程，开头和结尾的相关信息除外。
//		  -r   忽略普通的Routing Table，直接将数据包送到远端主机上。
//		  -R   记录路由过程。
//		  -s<数据包大小>   设置数据包的大小。
//		  -t<存活数值>   设置存活数值TTL的大小。
//		  -v   详细显示指令的执行过程。
		
		
		int result = 200;
		try {
			System.out.println("开始测试！");

			Process p = Runtime.getRuntime().exec("ping -c 1 -i 0.2 -w 5 " + str);
			System.out.println("测试等待中！");

			int status = p.waitFor();
			if (status == 0) {
				System.out.println("连接成功！");
			} else {
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}

}
