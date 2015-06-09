package edu.uams.ae.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;



import edu.uams.ae.data.aeData;
import edu.uams.ae.data.returnData;
import edu.uams.ae.db.dbconnector;
import edu.uams.ae.history.aeHist;

public class AdverseEventClient {
	
	//Database connection information
	private String DbType="";
	private String DbUrl="";
	private String DbUsername="";
	private String DbPassword="";
	private String DbName="";
	
	//Database connection information
	private String c3pr_DbType="";
	private String c3pr_DbUrl="";
	private String c3pr_DbUsername="";
	private String c3pr_DbPassword="";
	private String c3pr_DbName="";
	
	
	private String urlParameters;
	private String targetURL;
	
	private boolean SSLEnabled;
	
	//Parameters
	private String survey_id;
	private String meddra_v12_code;
	private String IRB;
	private String HBO;
	private String mrn;
	private String Patient_Name;
	private String Patient_Lastname;
	private String ResultDate;
	private String ToxicityGrade;
	private int LabOrderID;
	private String System_Name;
	private String numeric_result;
	private String range_high;
	private String range_low;
	private String unit;
	private String orderDate;
	private String epicId;
	
	//Inputs
	private int ResultId;
	private String order_id;
	private String testCode;
	private String defaultIRB;
	private String SystemCode;
	private String labTest;
	
	
	

	private aeHist Hist;
	private int flag;
	
	public String getLabTest() {
		return labTest;
	}

	public void setLabTest(String labTest) {
		this.labTest = labTest;
	}
	
	public int getFlag()
	{
		return this.flag;
	}
	
	public aeHist getHist() {
		return Hist;
	}

	public void setHist(aeHist hist) {
		Hist = hist;
	}
	
	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public void setC3pr_DbType(String c3pr_DbType) {
		this.c3pr_DbType = c3pr_DbType;
	}

	public void setC3pr_DbUrl(String c3pr_DbUrl) {
		this.c3pr_DbUrl = c3pr_DbUrl;
	}

	public void setC3pr_DbUsername(String c3pr_DbUsername) {
		this.c3pr_DbUsername = c3pr_DbUsername;
	}

	public void setC3pr_DbPassword(String c3pr_DbPassword) {
		this.c3pr_DbPassword = c3pr_DbPassword;
	}

	public void setC3pr_DbName(String c3pr_DbName) {
		this.c3pr_DbName = c3pr_DbName;
	}

	public AdverseEventClient()
	{
		this.SSLEnabled = false;
	}
	
	public void enableSSL()
	{
		this.SSLEnabled = true;
	}
	
	public void setDbType(String dbType) {
		DbType = dbType;
	}


	public void setDbUrl(String dbUrl) {
		DbUrl = dbUrl;
	}




	public void setDbUsername(String dbUsername) {
		DbUsername = dbUsername;
	}


	public void setDbPassword(String dbPassword) {
		DbPassword = dbPassword;
	}


	public void setDbName(String dbName) {
		DbName = dbName;
	}


	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}


	public void setResultId(int resultId) {
		ResultId = resultId;
	}


	public void setDefaultIRB(String defaultIRB) {
		this.defaultIRB = defaultIRB;
	}

	
	public void getIRBfromC3PR()
	{
		this.IRB = "";
		dbconnector conn = new dbconnector(this.c3pr_DbType, this.c3pr_DbUrl, this.c3pr_DbName, this.c3pr_DbUsername, this.c3pr_DbPassword); // It should be C3PR db
		
		String SQL_old = "Select Distinct stu.id,  ident.value,  prt.first_name,  prt.last_name,  stu_ver.version_status,  stu.status, stu_irb.value AS irb, stu_pid.value AS stu_p_id "+
				"From   studies stu "+
				"Join identifiers stu_irb ON stu_irb.stu_id=stu.id AND stu_irb.dtype='OAI' AND stu_irb.type='SITE_IRB_IDENTIFIER' "+
				"Join identifiers stu_pid ON stu_pid.stu_id=stu.id AND stu_pid.dtype='OAI' AND stu_pid. primary_indicator=true "+
				"Inner Join study_versions stu_ver On stu_ver.study_id = stu.id "+
				"Inner Join study_site_versions stu_sit_ver On stu_sit_ver.stu_version_id = stu_ver.id "+
				"Inner Join study_subject_versions stu_sub_ver On stu_sub_ver.study_site_ver_id = stu_sit_ver.id "+
				"Inner Join study_subjects stu_sub On stu_sub.id = stu_sub_ver.spa_id "+
				"Inner Join stu_sub_demographics demog On demog.id = stu_sub.stu_sub_dmgphcs_id "+
				"Inner Join participants prt On demog.prt_id = prt.id "+
				"Inner Join identifiers ident On prt.id = ident.prt_id "+
				"Where stu_ver.version_status Like 'AC' And stu.status Like 'OPEN' And ident.type Like 'MRN' And ident.dtype Like 'OAI' And SubString(ident.value From Length(ident.value) - 6 For 7 ) Like '"+this.mrn+"' And stu_sub.reg_workflow_status Not In ('OFF_STUDY', 'NOT_REGISTERED', 'INVALID')";
		
		String SQL = "Select study_subject_versions.study_site_ver_id, study_subjects.reg_workflow_status, study_versions.version_status, studies.status, mymrns.mymrn, myirbs.myirb, studies.id "+
				"From  study_subject_versions "+ 
				"Inner Join study_subjects On study_subject_versions.spa_id = study_subjects.id "+ 
				"Inner Join stu_sub_demographics On study_subjects.stu_sub_dmgphcs_id = stu_sub_demographics.id "+ 
				"Inner Join study_site_versions On study_subject_versions.study_site_ver_id = study_site_versions.id "+ 
				"Inner Join study_versions On study_site_versions.stu_version_id = study_versions.id "+
				"Inner Join studies On study_versions.study_id = studies.id "+ 
				"Inner Join "+ 
				"(Select identifiers.prt_id, identifiers.value As mymrn, identifiers.dtype, identifiers.type "+
				"From identifiers "+
				"Where "+
				"Not (identifiers.prt_id Is Null) And identifiers.dtype = 'OAI' And  identifiers.type = 'MRN') mymrns "+
				"On mymrns.prt_id = stu_sub_demographics.prt_id Left Join "+
				"(Select identifiers.stu_id, identifiers.value As myirb, identifiers.type, identifiers.dtype "+ 
				"From identifiers "+
				"Where "+
				"Not (identifiers.stu_id Is Null) And identifiers.type = 'SITE_IRB_IDENTIFIER' And identifiers.dtype = 'OAI') myirbs On myirbs.stu_id = studies.id "+
				"Where "+
				"(NOT(study_subjects.reg_workflow_status In ('OFF_STUDY','NOT_REGISTERED','INVALID'))) And "+
				"study_versions.version_status = 'AC' And studies.status = 'OPEN' AND (lpad(mymrns.mymrn, 15, '0') = lpad('"+this.mrn+"', 15, '0')) AND (NOT(myirbs.myirb IS NULL)) ";
				
		System.out.println(SQL);
		conn.executeQuery(SQL);
		
		while(conn.Next())
		{
			if(!conn.getString("myirb").equals(""))
			{
				if(this.IRB.equals(""))
				{
					this.IRB = conn.getString("myirb");
				}
				else
				{
					this.IRB = this.IRB+","+conn.getString("myirb");
				}
			}
			/*else
			{
				if(this.IRB.equals(""))
				{
					this.IRB = conn.getString("stu_p_id");
				}
				else
				{
					this.IRB = this.IRB+","+conn.getString("stu_p_id");
				}
			}*/
		}
		
		conn.Close();
	}

	public void getLookUpInformation(String rule)
	{
		//System.out.println("getLookUpInformation()");
		dbconnector conn = new dbconnector(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		String SQL = "Select survey_id, meddra_v12_code from lab_test_lookup where test_code = '"+this.testCode+"' and rule like '"+rule+"'"; 
		System.out.println("--> "+SQL);
		conn.executeQuery(SQL);
		
		if(rule.equals("%"))
		{
			while(conn.Next())
			{
				if( this.meddra_v12_code == null || this.meddra_v12_code.equals(""))
				{
					this.meddra_v12_code = conn.getString("meddra_v12_code");
					
					if(!conn.getString("survey_id").equals("") && conn.getString("survey_id") != null)
					{
						this.survey_id = conn.getString("survey_id");
					}
				}
				else
				{
					this.meddra_v12_code = this.meddra_v12_code + "," + conn.getString("meddra_v12_code");
					
					if(conn.getString("survey_id") != null && !conn.getString("survey_id").equals(""))
					{
						this.survey_id = conn.getString("survey_id");
					}
				}
			}
		}
		else
		{
			if(conn.Next())
			{
				this.survey_id = conn.getString("survey_id");
				this.meddra_v12_code = conn.getString("meddra_v12_code");
			}
			else
			{
				this.meddra_v12_code = "";
			}
			
			/*if(this.meddra_v12_code.equals(""))
			{
				this.meddra_v12_code = "10022891";
			}*/
		}
		
		conn.Close();
	}
	
	
	public void getIRBandHBO()
	{
		//System.out.println("getIRBandHBO()");
		
		//if(this.order_id.equals(""))
		if((this.System_Name == null) || (!this.System_Name.equals("PSC")))
		{
			this.getIRBfromC3PR();
			return;
		}
		
		dbconnector conn = new dbconnector(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		String SQL = "Select irb, hbo_id from get_activities_detailed where order_id = "+this.order_id;
		
		conn.executeQuery(SQL);
		
		if(conn.Next())
		{
			this.IRB = conn.getString("irb");
			this.HBO = conn.getString("hbo_id");
		}
		else
		{
			 this.getIRBfromC3PR();
		}
		
		conn.Close();
	}
	
	public String RemoveLeadingZeros(String inp)
	{
		//String res = "";
		
		while(inp.charAt(0)=='0' && inp.length() > 7)
		{
			inp = inp.substring(1);
		}
		
		return inp;
	}
	
	public void getPatientData()
	{
		
		//dbconnector conn = new dbconnector(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		dbconnector conn = new dbconnector(this.c3pr_DbType, this.c3pr_DbUrl, this.c3pr_DbName, this.c3pr_DbUsername, this.c3pr_DbPassword); // It should be C3PR db
		//String SQL = "Select first_name,last_name from subjects where person_id = '"+this.mrn+"'";
		
		String SQL = "Select substring(ident.value from length(ident.value) -6 for 7) as mrn,prt.first_name, prt.last_name "+ 
					 "from participants prt "+
					 "JOIN identifiers ident ON prt.id = ident.prt_id "+
					 "WHERE ident.type LIKE 'MRN' and ident.dtype LIKE 'OAI' "+
					 "and substring(ident.value from length(ident.value) -6 for 7) = '"+RemoveLeadingZeros(this.mrn)+"'";
		
		/*System.out.println("-------------------------");
		System.out.println(SQL);
		System.out.println("-------------------------");*/
		
		conn.executeQuery(SQL);
		
		if(conn.Next())
		{
			this.Patient_Name = conn.getString("first_name");
			this.Patient_Lastname = conn.getString("last_name");
		}
		
		conn.Close();
	}
	
	
	public void getLabData()
	{
		//System.out.println("getLabData()");
		dbconnector conn = new dbconnector(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		String SQL = "Select id,order_id, mrn, result_date, lab_test_code, system, toxicity_grade, hbo_id, system, range_low, range_high, numeric_result, unit, order_date, lab_test, epic_beaker_id from lab_results where id = "+this.ResultId;
		//String SQL = "Select id,order_id, mrn, result_date, lab_test_code, system, toxicity_grade, system, range_low, range_high, numeric_result, unit from lab_results where id = "+this.ResultId;
		
		conn.executeQuery(SQL);
		
		if(conn.Next())
		{
			this.order_id = conn.getString("order_id");
			if(this.order_id == null)
			{
				this.order_id = "";
			}
			this.mrn = conn.getString("mrn");
			this.ResultDate = conn.getString("result_date");
			this.orderDate = conn.getString("order_date");
			
			this.testCode = conn.getString("lab_test_code");
			this.SystemCode = conn.getString("system");
			this.ToxicityGrade = conn.getString("toxicity_grade");
			//this.ToxicityGrade = "0";
			this.HBO = conn.getString("hbo_id");
			this.unit = conn.getString("unit");
			this.LabOrderID = (int) conn.getInt("id");
			
			this.numeric_result = conn.getString("numeric_result");
			this.range_high = ""+conn.getDouble("range_high");
			this.range_low = ""+conn.getDouble("range_low");
			
			this.labTest = conn.getString("lab_test");
			this.epicId = conn.getString("epic_beaker_id");
			
			if(Double.parseDouble(conn.getString("numeric_result")) > conn.getDouble("range_high"))
			{
				this.getLookUpInformation(">ULN");
			}
			else if(Double.parseDouble(conn.getString("numeric_result")) < conn.getDouble("range_low"))
			{
				this.getLookUpInformation("<LLN");
			}
			else
			{
				this.getLookUpInformation("%");
			}
			
			this.getPatientData();
			
			String System = conn.getString("system");
			this.System_Name = System;
			
			//if(System.equals("PSC"))
			this.getIRBandHBO();
			/*
			if(this.SystemCode.equals("PSC"))
			{
				this.getIRBandHBO();
			}
			else
			{
				this.IRB = this.defaultIRB;
				//this.HBO = "";
			}*/
		}
		
		
		conn.Close();
		
		
	}
	
	
	
	public String send()
	{
		aeHist Hist = new aeHist();		
		//Hist.setDBConInfo(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		
		//if(this.ToxicityGrade.equals("0") && !Hist.getGroup())
		//{
			// Do nothing
		//}
		//else
		//{
		
		
		
			//System.out.println("send()");
			getLabData();
			String response = "";
			try {
				urlParameters = "irb="+URLEncoder.encode(this.IRB,"UTF-8");
				urlParameters += "&";
				urlParameters += "vDate="+URLEncoder.encode(this.ResultDate,"UTF-8") ;
				urlParameters += "&";
				urlParameters += "hbo="+URLEncoder.encode(this.HBO,"UTF-8");	
				urlParameters += "&";
				//urlParameters += "surveyid="+URLEncoder.encode(this.survey_id,"UTF-8") ;
				//urlParameters += "&";
				urlParameters += "title="+URLEncoder.encode(this.meddra_v12_code,"UTF-8");	
				urlParameters += "&";
				urlParameters += "grade="+URLEncoder.encode(this.ToxicityGrade,"UTF-8") ;
				urlParameters += "&";
				urlParameters += "FN="+URLEncoder.encode(this.Patient_Name,"UTF-8") ;
				urlParameters += "&";
				urlParameters += "LN="+URLEncoder.encode(this.Patient_Lastname,"UTF-8") ;
				urlParameters += "&";
				urlParameters += "mrn="+URLEncoder.encode(this.mrn,"UTF-8") ;
	
				System.out.println(urlParameters);	
				System.out.println("");
			URL myurl = new URL(targetURL);
			
			
			
			
			HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-length", String.valueOf(urlParameters.length())); 
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)"); 
			con.setDoOutput(true); 
			con.setDoInput(true); 
	
			DataOutputStream output = new DataOutputStream(con.getOutputStream());  
	
	
			output.writeBytes(urlParameters);
	
			output.close();
	
			DataInputStream input = new DataInputStream( con.getInputStream() ); 
	
	
	
			for( int c = input.read(); c != -1; c = input.read() ) 
			System.out.print( (char)c ); 
			input.close(); 
	
			System.out.println("Resp Code:"+con.getResponseCode()); 
			System.out.println("Resp Message:"+ con.getResponseMessage()); 
			response = con.getResponseMessage();
			
			con.disconnect();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return response;	
		//}
		//return "";
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public String Jsend()
	{
		aeHist Hist = new aeHist();	
		
		String response_text = "";
		//Hist.setDBConInfo(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		
		//if(this.ToxicityGrade.equals("0") && !Hist.getGroup())
		//{
			// Do nothing
			// response_text = "It is now a valid AE!";
		//}
		//else
		//{
		
		
		aeData AED = new aeData();
		getLabData();
		
		AED.setFN(this.Patient_Name);
		AED.setLN(this.Patient_Lastname);
		AED.setGrade(this.ToxicityGrade);
		AED.setHbo(this.HBO);
		AED.setIrb(this.IRB.split(","));
		AED.setMrn(this.mrn);
		AED.setTitle(this.meddra_v12_code.split(","));
		AED.setvDate(this.ResultDate);
		AED.setTestCode(this.testCode);
		AED.setValue(this.numeric_result);
		AED.setHighValue(this.range_high);
		AED.setLowValue(this.range_low);
		AED.setDb_id(this.LabOrderID+"");
		AED.setOrderDate(this.orderDate);
		
		Gson g = new Gson();
		
		
		//HashMap hm = new HashMap(); 
		//hm.put("aeData", AED);
		String json = g.toJson(AED);
		System.out.println(json);
		//aeData json = g.toJson(AED,aeData.class);
		
		
		//json = "{\"ae\":"+json+"}";
		
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(this.targetURL+"?aeData=");
			StringEntity stringEntity = new StringEntity(json);
			stringEntity.setContentType("application/json");
			//httppost.setParams(HttpParams.class.setParameter("aeData", json));
			httppost.setEntity(stringEntity);
			System.out.println("executing request " + httppost.getRequestLine());
         	HttpResponse response = httpclient.execute(httppost);
         	HttpEntity resEntity = response.getEntity();

         	//System.out.println("------");
         	//System.out.println(response.getStatusLine());
         	//System.out.println("------");
         	if (resEntity != null) {
         		//System.out.println("###------");
         		//System.out.println(convertStreamToString(resEntity.getContent()));
         		//System.out.println("###------");
         		//System.out.println("Response content length: " + resEntity.getContentLength());
         		//System.out.println("Chunked?: " + resEntity.isChunked());
         	}
         	EntityUtils.consume(resEntity);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return  json;
		
		//}
		//return "";
		
	}
	
	public String JCall()
	{
			//aeHist Hist = new aeHist();
			String response_text = "";
			//Hist.setDBConInfo(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
			//Hist.setDBConInfo("postgresql", "server", "database", "username", "password");
			getLabData();
			//loadData(String mrn, String test_code, int grade, int lab_result_id)
			//Hist.loadData(this.mrn, this.testCode, Integer.parseInt(this.ToxicityGrade), this.LabOrderID);
			//Hist.process();
			
			boolean a1 = Hist.process();
			a1 = false; //Remove this later Dont put it in server.
			boolean a2 = false;
			if(this.ToxicityGrade != null)
			{
				a2 = this.ToxicityGrade.equals("0");
			}
			boolean a3 = !this.Hist.getGroup();
			
			boolean a4 = false;
			
			if(this.meddra_v12_code == null || this.meddra_v12_code == "")
			{
				a4 = true;
			}
			
			if((a1 && (a2 && a3)))
			{
				// Do nothing
				 response_text = "It is not a valid AE!";
				 this.flag = 2;
				 
			}
			/*else if(a4)
			{
				 response_text = "No AE is defined for this condition!";
				 this.flag = 2;
			}*/
			else
			{
			//System.out.println("Sending");
			HttpClient httpClient = new DefaultHttpClient();
		
			HttpParams params_ = httpClient.getParams();
			//HttpConnectionParams.setConnectionTimeout(params_, 9000);
			HttpConnectionParams.setSoTimeout(params_, 30000);
			
		    try {
		        HttpPost request = new HttpPost(this.targetURL);
		        
		        
		        aeData AED = new aeData();

				
				AED.setFN(this.Patient_Name);
		        //AED.setFN("Marie");
				AED.setLN(this.Patient_Lastname);
		        //AED.setLN("Test");
				//AED.setGrade("0");
				AED.setGrade(this.ToxicityGrade);
				AED.setHbo(this.HBO);
				AED.setTestCode(this.testCode);
				AED.setValue(this.numeric_result);
				AED.setHighValue(this.range_high);
				AED.setLowValue(this.range_low);
				AED.setUnit(this.unit);
				AED.setDb_id(this.LabOrderID+"");
				
				//AED.setHbo("");
				if(this.IRB != null)
					AED.setIrb(this.IRB.split(","));
				else
				{
					String[] a = {};
					AED.setIrb(a);
				}
				/*String[] a = {"110396"};
				AED.setIrb(a);*/
				AED.setMrn(this.mrn);
				//AED.setMrn("2006223");
				AED.setTitle(this.meddra_v12_code.split(","));
				AED.setvDate(this.ResultDate);
				AED.setOrderDate(this.orderDate);
				AED.setLabTest(this.labTest);
				AED.setEpicId(this.epicId);
		        
				String json = AED.toJson();
				System.out.println(json);

				
		        StringEntity params =new StringEntity(json);
		        request.addHeader("content-type", "application/x-www-form-urlencoded");
		        request.setEntity(params);
				
		        System.out.println("Made the call...");
		        HttpResponse response = httpClient.execute(request);	
		        System.out.println("Got the response...");
		        
		        
		        HttpEntity resEntity = response.getEntity();

	         		//	System.out.println("------");
	         		//	System.out.println(response.getStatusLine());
	         		//	System.out.println("------");
	         	if (resEntity != null) {
	         			//System.out.println("###------");
	         		String RetVal = convertStreamToString(resEntity.getContent());
	         			System.out.println(RetVal);
	         			//System.out.println("^^^^^");
	         		ObjectMapper objectMapper = new ObjectMapper();
	         			//"{\"Success\":\"1\",  \"Msg\":\"Success!\"}"
	         		returnData RD = objectMapper.readValue(RetVal , returnData.class);
	         		//System.out.println("Result : "+RD.getMsg());
	         		response_text = "Result : "+RD.getMsg();
	         		this.flag = RD.getSuccess();
	         		
	         			//System.out.println("###------");
	         			//System.out.println("Response content length: " + resEntity.getContentLength());
	         			//System.out.println("Chunked?: " + resEntity.isChunked());
	         	}
	         	EntityUtils.consume(resEntity);

		        // handle response here...
		    }catch (Exception ex) {
		        // handle exception here
		    	this.flag = 0;
		    	ex.printStackTrace();
		    } finally {
		        httpClient.getConnectionManager().shutdown();
		    }
		    
		    
		  }
		
		
		  return response_text;
	}

}
