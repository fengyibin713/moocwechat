package wechat.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import wechat.service.CoreService;
import wechat.util.SignUtil;

/**
 * 请求处理的核心类
 * 
 * @author 
 * @date 
 */
public class CoreServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(CoreServlet.class); 
	/**
	 * 
	 */
	private static final long serialVersionUID = 892403490426217658L;

	/**
	 * 初始化
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}
	/**
	 * 请求校验（确认请求来自微信服务器）
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		
		PrintWriter out = response.getWriter();
		// 请求校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			logger.info("doGet{signature:"+signature+" timestamp:"+timestamp+" nonce:"+nonce+" echostr"+echostr+"}");
			out.print(echostr);
		}else{
			logger.error("doGet{signature:"+signature+" timestamp:"+timestamp+" nonce:"+nonce+" echostr"+echostr+"}");
		}
		out.close();
		out = null;
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 接收参数微信加密签名、 时间戳、随机数
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		
		logger.info("doPost{signature:"+signature+" timestamp:"+timestamp+" nonce:"+nonce+"}");
		
		PrintWriter out = response.getWriter();
		// 请求校验
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			// 调用核心服务类接收处理请求
			String respXml = CoreService.processRequest(request);
			out.print(respXml);
			logger.info("respXml:\n"+respXml);
		}else{
			logger.error("doPost{signature:"+signature+" timestamp:"+timestamp+" nonce:"+nonce+"}");
		}
		out.close();
		out = null;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
}
