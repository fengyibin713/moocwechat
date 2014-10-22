package wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import wechat.message.resp.TextMessage;
import wechat.session.Operation;
import wechat.session.Session;
import wechat.session.SessionItem;
import wechat.util.MsgUtil;

public class StudentService {
	/**
	 * 处理学生请求
	 * 
	 * @param request
	 * @return xml
	 */
	private static Logger logger = Logger.getLogger(StudentService.class); 
	public static String processRequest(Map<String, String> requestMap) {
		// xml格式的消息数据
		String respXml = "student";
		// 默认返回的文本消息内容
		String respContent = "未知的消息类型！";
		try {
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			String content = requestMap.get("Content");
			String createTime = requestMap.get("CreateTime");
			
			logger.info("(StudentRequest)["+msgType+"]"+createTime+":"+fromUserName+"---->"+toUserName);
			
			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MsgUtil.RESP_MESSAGE_TYPE_TEXT);
			
			//客服消息处理
			if (Session.get(fromUserName).getOper().equals(Operation.OPER3)){
				if (msgType.equals(MsgUtil.REQ_MESSAGE_TYPE_TEXT) && content.equals("#")) {
					Session.get(fromUserName).setOper(Operation.OPER0);
					textMessage.setContent("您已终止客服消息发送");
					return MsgUtil.messageToXml(textMessage);
				}else{
					CustomerService.process(requestMap);
					textMessage.setContent("已接受客服消息，请继续输入，如需手动终止客服消息发送，请直接回复“#”");
					return MsgUtil.messageToXml(textMessage);
				}
			}
			
			// 文本消息
			if (msgType.equals(MsgUtil.REQ_MESSAGE_TYPE_TEXT)) {
				logger.info("(StudentText)"+createTime+":"+fromUserName+"---->"+toUserName+":"+content);
				respContent = "您发送的是文本消息！";
				
				//测试使用，转换用户身份
				if (content.equals("teacher")) {
					CoreService.addTeacher(fromUserName);
					respContent = "转换成老师身份！";
				}
				if (content.equals("student")) {
					CoreService.removeTeacher(fromUserName);
					respContent = "转换成学生身份！";
				}
				
				//测试使用，清除测试用的绑定数据
				if (content.equals("clear")) {
					clearBindingRegister();
					respContent = "清除所有绑定数据！";
				}
				
				//将session状态设为初始值
				if (content.equals("#")){
					Session.get(fromUserName).setOper(Operation.OPER0);
				}
			}
			// 图片消息
			else if (msgType.equals(MsgUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 语音消息
			else if (msgType.equals(MsgUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是语音消息！";
			}
			// 视频消息
			else if (msgType.equals(MsgUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				respContent = "您发送的是视频消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MsgUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MsgUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 事件推送
			else if (msgType.equals(MsgUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 关注
				if (eventType.equals(MsgUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "您好！欢迎关注软件测试MOOC(STMOOC)! 软件测试MOOC课程将于2015年2月在Coursera平台发布。在这期间我们将陆续发布课程录制进展和内测版本（包括视频录像和练习题）。欢迎试用！";
				}
				// 取消关注
				else if (eventType.equals(MsgUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
				}
				// 扫描带参数二维码
				else if (eventType.equals(MsgUtil.EVENT_TYPE_SCAN)) {
					// TODO 处理扫描带参数二维码事件
				}
				// 上报地理位置
				else if (eventType.equals(MsgUtil.EVENT_TYPE_LOCATION)) {
					// TODO 处理上报地理位置事件
				}
				// 自定义菜单
				else if (eventType.equals(MsgUtil.EVENT_TYPE_CLICK)) {
					String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("exam_password")) {
						if (isBinded(fromUserName)){
							respContent = "正在进行的考试的考试密码";
						}else {
							respContent = "提示需先关联";
						}
					}else if (eventKey.equals("exam_score")) {
						if (isBinded(fromUserName)){
							respContent = "7天内结束的所有考试成绩";
						}else{
							respContent = "提示需先关联";
						}
					}else if (eventKey.equals("quiz_statistics")) {
						respContent = "这项功能仅提供给教师使用";
					}else if (eventKey.equals("quiz_single")) {
						respContent = "请输入您的回答(单选)并提交，或者提示需先注册/关联";
					}else if (eventKey.equals("quiz_multiple")) {
						respContent = "请输入您的回答(多选)并提交，或者提示需先注册/关联";
					}else if (eventKey.equals("quiz_other")) {
						respContent = "请输入您的回答并提交，或者提示需先注册/关联";
					}else if (eventKey.equals("register")) {
						respContent = "跳转到注册页面Web";
					}else if (eventKey.equals("binding")) {
						if (isBinded(fromUserName)) {
							respContent = "您已绑定"; 
						}else{
							respContent = "跳转到登录页面，输入用户名密码:"+binding(fromUserName);
						}
					}else if (eventKey.equals("service")) {
						if (isBinded(fromUserName)){
							if (!Session.get(fromUserName).getOper().equals(Operation.OPER1)) {
								respContent = "您好，现在您可以在5分钟之内发送文字、语音以及图片来描述您想要提问的内容，或者点击<a href='http://115.29.242.187/wibug/Wibug/winet/submitProblem.php?openid="+fromUserName+"'>网页提交</a>来提交。如果您要手动终止客服消息发送，请直接回复“#”";
								SessionItem si = new SessionItem(Operation.OPER3, Operation.STAGE1);
								Session.set(fromUserName, si);
							}else{
								// 在文字回复部分进行处理
							}
						}else{
							respContent = "提示需先关联";
						}
					}
				}
			}
			// 设置文本消息的内容
			textMessage.setContent(respContent);
			// 将文本消息对象转换成xml
			respXml = MsgUtil.messageToXml(textMessage);
		} catch (Exception e) {
			logger.error(e.getStackTrace());
			e.printStackTrace();
		}
		return respXml;
	}


	private static ArrayList<String> BindedList = new ArrayList<String>();
	private static boolean isBinded (String openid) {
		if (BindedList.contains(openid)) {
			return true;
		}
		return false;
	}
	private static String binding (String openid){
			BindedList.add(openid);
			return "绑定成功";
	}
	private static void clearBindingRegister(){
		BindedList.clear();
	}
}
