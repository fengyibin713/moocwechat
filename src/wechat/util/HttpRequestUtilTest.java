package wechat.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class HttpRequestUtilTest {
	public static void main(String[] args) {
		test();
	}

	public static void test() {
		// 发送 GET 请求
		String s = HttpRequestUtil
				.sendGet("http://112.124.1.3:8088/MOOCWechat/index.jsp");
		System.out.println("----");
		System.out.println(s);

		// 发送 POST 请求
		// String
		// sr=HttpRequestUtil.sendPost("http://115.29.242.187/wibug/Wibug/winet/insertMessage.php",
		// "key=123&v=456");
		// System.out.println(sr);
		String msgId = "";
		String fromUserId = "";
		String toUserId = "";
		String createTime = "";
		String msgtype = "";
		String content = "";
		String mediaId = "";
		String recognition = "";
		String param = "";

		param += "msgId=MSG_ID";
		param += "&fromUserId=FROM_USER_ID";
		param += "&toUserId=TO_USER_ID";
		param += "&createTime=CREATE_TIME";
		param += "&msgtype=MSG_TYPE";
		param += "&content=CONTENT";
		param += "&mediaId=MEDIA_ID";
		param += "&recognition=RECOGNITION";
		
		param.replace("MSG_ID", msgId);
		param.replace("FROM_USER_ID", fromUserId);
		param.replace("TO_USER_ID", toUserId);
		param.replace("CREATE_TIME", createTime);
		param.replace("MSG_TYPE", createTime);
		param.replace("MSG_ID", msgtype);
		param.replace("CONTENT", content);
		param.replace("MEDIA_ID", mediaId);
		param.replace("RECOGNITION", recognition);

	}

}
