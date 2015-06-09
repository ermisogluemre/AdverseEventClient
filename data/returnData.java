package edu.uams.ae.data;

import org.codehaus.jackson.annotate.JsonProperty;

public class returnData {
	
	@JsonProperty("Success")
	public int Success;
	
	@JsonProperty("Msg")
	public String Msg;
		
	public String getMsg() {
		return Msg;
	}	

	public int getSuccess()
	{
		return this.Success;
	}
	

	

}
