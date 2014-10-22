package wechat.session;

public class SessionItem {
	private String Oper;
	private String Stage;
	private String OpenId;

	public SessionItem() {
		Oper = Operation.OPER1;
		Stage = Operation.STAGE1;
	}

	public SessionItem(String oper, String stage) {
		Oper = oper;
		Stage = stage;
	}

	public String getOper() {
		return Oper;
	}

	public void setOper(String oper) {
		Oper = oper;
	}

	public String getStage() {
		return Stage;
	}

	public void setStage(String stage) {
		Stage = stage;
	}

	public String getOpenId() {
		return OpenId;
	}

	public void setOpenId(String openId) {
		OpenId = openId;
	}

}
