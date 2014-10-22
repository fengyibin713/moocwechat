package wechat.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import wechat.message.resp.TextMessage;
import wechat.session.Operation;
import wechat.session.Session;
import wechat.session.SessionItem;
import wechat.util.MsgUtil;

/**
 * 核心服务类
 * 
 * @author 
 * @date 
 */
public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return xml
	 */
	private static Logger logger = Logger.getLogger(CoreService.class); 
	
	public static String processRequest(HttpServletRequest request) {
		// xml格式的消息数据
		String respXml = null;

		try {
			// 调用parseXml方法解析请求消息
			Map<String, String> requestMap = MsgUtil.parseXml(request);
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
//			String msgType = requestMap.get("MsgType");
//			String content = requestMap.get("Content");
//			String createTime = requestMap.get("CreateTime");
			
			if (!Session.contains(fromUserName)){
				SessionItem si = new SessionItem(Operation.OPER0, Operation.STAGE0);
				Session.set(fromUserName, si);
			}
			
			if (isTeacher(fromUserName)){
				logger.info("(TeacherRequest)"+fromUserName+"---->"+toUserName);
				respXml = TeacherService.processRequest(requestMap);
			}else if(isStudent(fromUserName)){
				logger.info("(StudentRequest)"+fromUserName+"---->"+toUserName);
				respXml = StudentService.processRequest(requestMap);
			}else {
				logger.info("(Unknown)"+fromUserName+"---->"+toUserName);
				String respContent = "未知的消息类型！";
				// 回复文本消息
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MsgUtil.RESP_MESSAGE_TYPE_TEXT);
				// 设置文本消息的内容
				textMessage.setContent(respContent);
				// 将文本消息对象转换成xml
				respXml = MsgUtil.messageToXml(textMessage);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respXml;
	}
	
	public static ArrayList<String> teacher_list = new ArrayList<String>();
	public static void addTeacher(String openId){
		if (!isTeacher(openId)) {
			teacher_list.add(openId);
		}
	}
	public static void removeTeacher(String openId){
		if (isTeacher(openId)) {
			teacher_list.remove(openId);
		}
	}
	public static boolean isTeacher(String openId){
		if (teacher_list.contains(openId)){
			return true;
		}else {
			return false;
		}
	}
	public static boolean isStudent(String openId){
		return true;
	}
}