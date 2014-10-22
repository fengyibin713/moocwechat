package test;

import org.apache.log4j.Logger;

public class TestLog {

	/**
	 * @param args
	 */
	private static Logger logger = Logger.getLogger(TestLog.class);  
	public static void main(String[] args) {
        // 记录info级别的信息  
        logger.info("This is info message.");  
        // 记录error级别的信息  
        logger.error("This is error message.");
        String tmp = "dfadf";
        tmp=tmp.replaceAll("a", "A");
        System.out.println(tmp);
	}

}
