package wechat.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wechat.message.resp.Article;
import wechat.message.resp.NewsMessage;
import wechat.util.MessageUtil;

public class NewsMessageUtil {
	public static String createXMLs(int index, String content,
			String fromUserName, String toUserName) {
		String xml = "";
		List<Article> articleList = new ArrayList<Article>();
		
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setArticleCount(articleList.size());
		newsMessage.setArticles(articleList);

		xml = MessageUtil.messageToXml(newsMessage);

		return xml;
	}
	
	private static Article createArticle(String title, String description,
			String picUrl, String url) {
		Article article = new Article();
		article.setTitle(title);
		article.setDescription(description);
		article.setPicUrl(picUrl);
		article.setUrl(url);
		return article;
	}
}
