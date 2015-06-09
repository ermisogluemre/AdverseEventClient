package edu.uams.ae.data;

public class aeData {
	private String[] irb;
	private String vDate;
	private String hbo;
	private String[] title;
	private String grade;
	private String FN;
	private String LN;
	private String mrn;
	private String sid;
	private String value;
	private String HighValue;
	private String LowValue;
	private String summary;
	private String testCode;
	private String unit;
	private String noctcae;
	private String db_id;
	private String orderDate;
	private String labTest;
	private String epicId;
	
	public String getEpicId() {
		return epicId;
	}
	public void setEpicId(String epicId) {
		this.epicId = epicId;
	}
	public String getLabTest() {
		return labTest;
	}
	public void setLabTest(String labTest) {
		this.labTest = labTest;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getDb_id() {
		return db_id;
	}
	public void setDb_id(String db_id) {
		this.db_id = db_id;
	}
	public String getNoctcae() {
		return noctcae;
	}
	public void setNoctcae(String noctcae) {
		this.noctcae = noctcae;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTestCode() {
		return testCode;
	}
	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getHighValue() {
		return HighValue;
	}
	public void setHighValue(String highValue) {
		HighValue = highValue;
	}
	public String getLowValue() {
		return LowValue;
	}
	public void setLowValue(String lowValue) {
		LowValue = lowValue;
	}
	public String getsid() {
		return sid;
	}
	public void setsid(String sid) {
		sid = sid;
	}
	public String[] getIrb() {
		return irb;
	}
	public void setIrb(String[] irb) {
		this.irb = irb;
	}
	public String getvDate() {
		return vDate;
	}
	public void setvDate(String vDate) {
		this.vDate = vDate;
	}
	public String getHbo() {
		return hbo;
	}
	public void setHbo(String hbo) {
		this.hbo = hbo;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getFN() {
		return FN;
	}
	public void setFN(String fN) {
		FN = fN;
	}
	public String getLN() {
		return LN;
	}
	public void setLN(String lN) {
		LN = lN;
	}
	public String getMrn() {
		return mrn;
	}
	public void setMrn(String mrn) {
		this.mrn = mrn;
	}
	
	public void createSummary()
	{
		//this.summary = "On "+this.vDate+", Lab test ( "+this.testCode+" ) with value "+this.value+" "+this.unit+" was received. Upper value for this test is "+this.HighValue+" "+this.unit+" and lower value for this test is "+this.LowValue+" "+this.unit+" .";
		this.summary = this.testCode+" : "+this.value+" "+this.unit+" ULN : "+this.HighValue+" "+this.unit+" LLN : "+this.LowValue+" "+this.unit;
	}
	
	public boolean gradeCheck()
	{
		boolean control = true;
		this.noctcae = "";
		if((this.grade == null) ||(this.grade.equals("")))
		{
			control = false;
			this.grade = "none";
			this.title[0] = "10022891";
			
			if(this.testCode.equals(""))
				this.testCode = this.labTest;
			
			this.noctcae = this.testCode+" out of range";
			
			this.value = this.value.replaceAll("<", "");
			this.value = this.value.replaceAll(">", "");
			
			if(Double.valueOf(this.LowValue) > Double.valueOf(this.value))
			{
				this.summary = this.testCode +": "+this.value +" < LLN ( "+this.LowValue+" )";
			}
			else if(Double.valueOf(this.HighValue) < Double.valueOf(this.value))
			{
				this.summary = this.testCode +": "+this.value+" > ULN ( "+this.HighValue+" )";
			}
		}
			
			
		return control;	
	}
	
	public String toJson()
	{
		if(gradeCheck())
		{
			createSummary();
		}
		String JSON_String = "aeData={\"irb\":[";
		boolean control = false;
		for(String temp : this.irb)
		{
			if(control)
			{
				JSON_String = 	JSON_String + ",";
			}
			
			JSON_String = JSON_String + "\"" + temp + "\"";
			
			control = true;
		}
		JSON_String = JSON_String + "],\"title\":[";
		boolean control1 = false;
		
		for(String tmp:this.title)
		{
			if(control1)
			{
				JSON_String = JSON_String + ",";
			}
			
			JSON_String = JSON_String + "\"" + tmp + "\"";
			control1 = true;
		}
				
		return JSON_String + "],\"vDate\":\""+this.vDate+"\",\"hbo\":\""+this.hbo+"\",\"grade\":\""+this.grade+"\",\"FN\":\""+this.FN+"\",\"LN\":\""+this.LN+"\",\"mrn\":\""+this.mrn+"\",\"note\":\""+this.summary+"\",\"noctcae\":\""+this.noctcae+"\",\"db_id\":\""+this.db_id+"\",\"order_date\":\""+this.orderDate+"\",\"epic_id\":\""+this.epicId+"\"}";
	}

}
