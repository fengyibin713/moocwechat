package wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import wechat.message.resp.TextMessage;
import wechat.session.Operation;
import wechat.session.Session;
import wechat.session.SessionItem;
import wechat.util.MessageUtil;

public class TeacherService {
	/**
	 * 处理教师请求
	 * 
	 * @param request
	 * @return xml
	 */
	private static Logger logger = Logger.getLogger(StudentService.class); 
	
	public static String processRequest(Map<String, String> requestMap) {
		// xml格式的消息数据
		String respXml = "teacher";
		// 默认返回的文本消息内容
		String respContent = "未知的消息类型！";
		try {
			// 调用parseXml方法解析请求消息
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			String content = requestMap.get("Content");
			String createTime = requestMap.get("CreateTime");
			
			logger.info("(TeacherRequest)["+msgType+"]"+createTime+":"+fromUserName+"---->"+toUserName);
			
			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

			//客服消息处理
			if (Session.get(fromUserName).getOper().equals(Operation.OPER3)){
				if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT) && content.equals("#")) {
					Session.get(fromUserName).setOper(Operation.OPER0);
					textMessage.setContent("您已终止客服消息发送");
					return MessageUtil.messageToXml(textMessage);
				}else{
					CustomerService.process(requestMap);
					textMessage.setContent("已接受客服消息，请继续输入，如需手动终止客服消息发送，请直接回复“#”");
					return MessageUtil.messageToXml(textMessage);
				}
			}
			
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				logger.info("(Text)"+createTime+":"+fromUserName+"---->"+toUserName+":"+content);
				respContent = "您发送的是文本消息！";
				
				//Session处理
				SessionItem si = Session.get(fromUserName);
				if (si.getOper().equals(Operation.OPER1)){
					if (Pattern.compile("\\d").matcher(content).matches()){
						int exam_id = Integer.parseInt(content);
						respContent = "考试"+exam_id+"的学生密码列表";
						si.setOper(Operation.OPER0);
					}else{
						respContent = "请输入正确的考试序号";
					}
				}
				if (si.getOper().equals(Operation.OPER2)){
					if (Pattern.compile("\\d").matcher(content).matches()){
						int exam_id = Integer.parseInt(content);
						respContent = "考试"+exam_id+"成绩统计报表";
						si.setOper(Operation.OPER0);
					}else{
						respContent = "请输入正确的考试序号";
					}
				}
				
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
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 语音消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是语音消息！";
			}
			// 视频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				respContent = "您发送的是视频消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 关注
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "您好！欢迎关注软件测试MOOC(STMOOC)! 软件测试MOOC课程将于2015年2月在Coursera平台发布。在这期间我们将陆续发布课程录制进展和内测版本（包括视频录像和练习题）。欢迎试用！";
				}
				// 取消关注
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
				}
				// 扫描带参数二维码
				else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
					// TODO 处理扫描带参数二维码事件
				}
				// 上报地理位置
				else if (eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {
					// TODO 处理上报地理位置事件
				}
				// 自定义菜单
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("exam_password")) {
						if (isBinded(fromUserName)){
							if (!Session.get(fromUserName).getOper().equals(Operation.OPER1)) {
								respContent = "7天内结束的所有考试列表，输入数字进行选择：";
								respContent += "\n 1.考试1";
								respContent += "\n 2.考试2";
								respContent += "\n 3.考试3";
								respContent += "\n 4.考试4";
								SessionItem si = new SessionItem(Operation.OPER1, Operation.STAGE1);
								Session.set(fromUserName, si);
							}else{
								// 在文字回复部分进行处理
							}
						}else{
							respContent = "提示需先关联";
						}
					}else if (eventKey.equals("exam_score")) {
						if (isBinded(fromUserName)){
							if (!Session.get(fromUserName).getOper().equals(Operation.OPER1)) {
								respContent = "正在进行的即时考试列表，输入数字进行选择：";
								respContent += "\n 1.考试1";
								respContent += "\n 2.考试2";
								respContent += "\n 3.考试3";
								respContent += "\n 4.考试4";
								SessionItem si = new SessionItem(Operation.OPER2, Operation.STAGE1);
								Session.set(fromUserName, si);
							}else{
								// 在文字回复部分进行处理
							}
						}else{
							respContent = "提示需先关联";
						}
					}else if (eventKey.equals("quiz_statistics")) {
						respContent = "最新一次小测统计报表：学生人数，提交人数，A-F答案人数；回复‘A’，返回答案为A的学生姓名列表；回复‘BDE’, 返回答案为BDE的学生姓名列表；回复文字，返回答案包含该文字的学生姓名列表。或者提示需先注册/关联";
					}else if (eventKey.equals("quiz_single")) {
						respContent = "该教师名下的最新5个班级名单列表，教师填写一个小测题目[单选]，选择班级和小测时长[1-10]分钟（默认3分钟），然后提交。或者提示需先注册/关联";
					}else if (eventKey.equals("quiz_multiple")) {
						respContent = "该教师名下的最新5个班级名单列表，教师填写一个小测题目[多选]，选择班级和小测时长[1-10]分钟（默认3分钟），然后提交。或者提示需先注册/关联";
					}else if (eventKey.equals("quiz_other")) {
						respContent = "该教师名下的最新5个班级名单列表，教师填写一个小测题目[其他]，选择班级和小测时长[1-10]分钟（默认3分钟），然后提交。或者提示需先注册/关联";
					}else if (eventKey.equals("register")) {
						respContent = "教师要求web注册，暂不提供微信注册";
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
			respXml = MessageUtil.messageToXml(textMessage);
		} catch (Exception e) {
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
