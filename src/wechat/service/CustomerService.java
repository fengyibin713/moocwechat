package wechat.service;

import java.util.Map;

import org.apache.log4j.Logger;

import wechat.util.HttpRequestUtil;
import wechat.util.MsgUtil;

public class CustomerService {
	private static Logger logger = Logger.getLogger(CustomerService.class); 
	public static void process(Map<String, String> requestMap) {

		String msgId = requestMap.get("MsgId");
		String fromUserId = requestMap.get("FromUserName");
		String toUserId = requestMap.get("ToUserName");
		String createTime = requestMap.get("CreateTime");
		String msgtype = requestMap.get("MsgType");
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

		if (msgtype.equals(MsgUtil.REQ_MESSAGE_TYPE_TEXT)) {
			content = requestMap.get("Content");
		} else if (msgtype.equals(MsgUtil.REQ_MESSAGE_TYPE_IMAGE)) {
			mediaId = requestMap.get("MediaId");
		} else if (msgtype.equals(MsgUtil.REQ_MESSAGE_TYPE_VOICE)) {
			mediaId = requestMap.get("MediaId");
//			recognition = requestMap.get("Recognition");
		}

		param = param.replace("MSG_ID", msgId);
		param = param.replace("FROM_USER_ID", fromUserId);
		param = param.replace("TO_USER_ID", toUserId);
		param = param.replace("CREATE_TIME", createTime);
		param = param.replace("MSG_TYPE", msgtype);
		param = param.replace("CONTENT", content);
		param = param.replace("MEDIA_ID", mediaId);
		param = param.replace("RECOGNITION", recognition);

		logger.info("param:"+param);
		
		// 发送 POST 请求
//		String str = HttpRequestUtil.sendPost(
//				"http://115.29.242.187/wibug/Wibug/winet/insertMessage.php",
//				param);
//		System.out.println(str);
	}
}
