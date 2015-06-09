package edu.uams.ae.history;

import edu.uams.ae.db.dbconnector;

public class aeHist {
	
	//Database connection information
	private String DbType="";
	private String DbUrl="";
	private String DbUsername="";
	private String DbPassword="";
	private String DbName="";
	
	
	private String mrn;
	private String test_code;
	private int group_id;
	private int nextOrderID;
	
	private int grade;
	private int lab_result_id;
	
	public void setDBConInfo(String Dbtype, String DbUrl, String DbName, String DbUsername, String DbPassword)
	{
		this.DbType = Dbtype;
		this.DbUrl = DbUrl;
		this.DbName = DbName;
		this.DbUsername = DbUsername;
		this.DbPassword = DbPassword;
	}
	
	public void loadData(String mrn, String test_code, int grade, int lab_result_id)
	{
		this.mrn = mrn;
		this.test_code = test_code;
		this.grade = grade;
		this.lab_result_id = lab_result_id;
	}
	
	public boolean getGroup()
	{
		boolean result = false;
		
		dbconnector conn = new dbconnector(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		
		String SQL = "Select a_h_i.group_id, a_h_i.ae_order_id "+
		"FROM ae_hist_group a_h_g "+
		"JOIN ae_hist_item a_h_i ON a_h_g.id = a_h_i.group_id and a_h_i.ae_order_id = (Select max(ae_order_id) from ae_hist_item ahi where ahi.group_id = a_h_g.id) "+
		"where a_h_g.mrn LIKE '"+this.mrn+"' and a_h_g.test_code LIKE '"+this.test_code+"' and a_h_g.resolved_flag = false";
		
		conn.executeQuery(SQL);
		
		if(conn.Next())
		{
			result = true;
			this.group_id = (int) conn.getInt("group_id");
			this.nextOrderID = ((int) conn.getInt("ae_order_id"))+1;
		}

		
		conn.Close();
		return result;
	}
	
	private void createGroup()
	{
		dbconnector conn = new dbconnector(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		
		String SQL = "INSERT INTO public.ae_hist_group(test_code, mrn) VALUES ('"+this.test_code+"', '"+this.mrn+"')";
		
		conn.executeUpdate(SQL);
		//New group was created...
		
		String Next_SQL = "Select id from  ae_hist_group where mrn LIKE '"+this.mrn+"' and test_code LIKE '"+this.test_code+"' and   resolved_flag = false";
		conn.executeQuery(Next_SQL);
		
		if(conn.Next())
		{
			this.group_id = (int) conn.getInt("id");
			this.nextOrderID = 1;
		}
		
		conn.Close();
	}
	
	private void closeGroup()
	{
		dbconnector conn = new dbconnector(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		
		String SQL = "UPDATE ae_hist_group SET resolved_flag=true, resolved_date=now() WHERE id = "+this.group_id;
		
		conn.executeUpdate(SQL);
		
		conn.Close();
	}
	
	private void insertGrade()
	{
		dbconnector conn = new dbconnector(this.DbType, this.DbUrl, this.DbName, this.DbUsername, this.DbPassword);
		
		String SQL = "INSERT INTO ae_hist_item(group_id, grade, lab_result_id, ae_order_id) VALUES ("+this.group_id+", "+this.grade+", "+this.lab_result_id+", "+this.nextOrderID+")";
		
		conn.executeUpdate(SQL);
		
		conn.Close();
	}
	
	public boolean process()
	{
		
		if(this.getGroup()) //Exist
		{
			//System.out.println("Group Exists...");
			if(this.grade == 0) // Grade = 0
			{
				//System.out.println("Insert garde and close the group");
				this.insertGrade();
				this.closeGroup();
				return false;
			}
			else // Grade > 0			
			{
				//System.out.println("Insert garde");
				this.insertGrade();
			}			
			
		}
		else // Not exist
		{
			//System.out.println("Group does not Exist...");
			if(this.grade == 0) // Grade = 0
			{
				//System.out.println("Do nothing...");
				// Do nothing
			}
			else // Grade > 0			
			{
				//System.out.println("Create group and insert grade");
				this.createGroup();
				this.insertGrade();
			}					
			
		}
		
		return true;
	}

}
