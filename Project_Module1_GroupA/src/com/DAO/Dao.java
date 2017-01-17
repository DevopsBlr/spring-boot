package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.DBUtil.DBUtil;
import com.bean.Transaction;









public class Dao {
	Connection con = null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	public long getBalance(String accId) {
		// TODO Auto-generated method stub
		long n = 0;
		
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select balance from tbl_account_tja58 where account_id=?");
			ps.setString(1, accId);
			rs=ps.executeQuery();
			while(rs.next()){
				n=rs.getLong(1);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}
		return n;
	}
	public void updateBalance(long u, String accId) {
		// TODO Auto-generated method stub
		con=DBUtil.getConnection();
		try {
			ps=con.prepareStatement("update tbl_account_tja58 set balance=? where account_id=? ");		
			ps.setLong(1, u);
			ps.setString(2,  accId);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		finally {
			DBUtil.closeStatement(ps);
			DBUtil.closeConnection(con);
		}
	
	}
	
		
		
	public long insertTransaction(String accId, String action, long amount,long curr) {
		// TODO Auto-generated method stub
	    long g = 0;
		con = DBUtil.getConnection();
		try {
			
			ps=con.prepareStatement("insert into tbl_Transaction_tja58 values (seq_dep_tra.nextval,?,?,?,?,null,?)");
			ps.setString(1, accId);
			ps.setString(2, action);
			java.sql.Date sqldate=new java.sql.Date(new java.util.Date().getTime());
			ps.setDate(3,sqldate);
			ps.setLong(4, amount);
			ps.setLong(5, curr);
		    ps.executeUpdate();
		    String query ="select max(transaction_id) from tbl_Transaction_tja58";
		    ps=con.prepareStatement(query);
		    rs=ps.executeQuery();
		    while(rs.next()){
		    	
		    	g=rs.getLong(1);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}
		return g;
		
	}
	public long insertTransaction(String accId, String action, long tamount,
			String taccId,long curr) {
		// TODO Auto-generated method stub
		
		con = DBUtil.getConnection();
		int i=0;
		long g=0;
		try {
			ps=con.prepareStatement("insert into tbl_Transaction_tja58 values (seq_dep_tra.nextval,?,?,?,?,?,?)");
			ps.setString(1, accId);
			ps.setString(2, action);
			java.sql.Date sqldate=new java.sql.Date(new java.util.Date().getTime());
			ps.setDate(3,sqldate);
			ps.setLong(4, tamount);
			ps.setString(5, taccId);
			ps.setLong(6, curr);
			i = ps.executeUpdate();
			String query ="select max(transaction_id) from tbl_Transaction_tja58";
		    ps=con.prepareStatement(query);
		    rs=ps.executeQuery();
		    while(rs.next()){
		    	
		    	g=rs.getLong(1);
		    }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}
		return g;
		
	}
	
	public ArrayList<String> getcustomer() {
		// TODO Auto-generated method stub
		ArrayList<String> customerid = new ArrayList<String>();
		con = DBUtil.getConnection();
	    try {
			ps = con.prepareStatement("select customerid from tbl_customer_tja58 where status='active'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
                customerid.add(rs.getString(1));
                }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}

		return customerid;
	}
	
	
	public ArrayList<String> getaccounts() {
		ArrayList<String> accountid = new ArrayList<String>();
		con = DBUtil.getConnection();
		try {
			ps = con.prepareStatement("select account_id from tbl_account_tja58 ");
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
                accountid.add(rs.getString(1));
                }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}

		return accountid;
		
	}
	
	
	public Transaction view(String accountid) {
		Transaction a=new Transaction();
		con = DBUtil.getConnection();
		try {
			ps=con.prepareStatement("select customer_id,account_id,account_type,balance from tbl_account_tja58 where account_id=?");
			ps.setString(1, accountid);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				a.setCustid(rs.getString(1));
				a.setAcc_id(rs.getString(2));
				a.setAcctype(rs.getString(3));
				a.setBalance(rs.getLong(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}

		
		return a;
	}
	
	
	   public ArrayList<Transaction> getstatement(String accountid,String start_date, String end_date)
	   {
		   ArrayList<Transaction> a=new ArrayList<Transaction>();
		 
		   con = DBUtil.getConnection();
		   try {
			ps=con.prepareStatement("select a.transaction_id, a.account_id, a.action ,a.transaction_date," +
					"a.amount,a.cur_balance,a.target_account_id  from tbl_Transaction_tja58 a  " +
					" where a.account_id=?  and transaction_date between to_date(?,'dd-mm-yyyy')and  to_date(?,'dd-mm-yyyy')  order by a.transaction_id desc");
          ps.setString(1, accountid);
          ps.setString(2,start_date );
          ps.setString(3, end_date);
           ResultSet rs=ps.executeQuery();

       
            while (rs.next()) {
            	 Transaction t=new Transaction();
                  t.setTran_id(rs.getLong(1));
                  t.setAcc_id(rs.getString(2));
                  t.setAction(rs.getString(3));
                  t.setDate(rs.getDate(4));
                  t.setAmount(rs.getLong(5));
                  t.setCurr_balance(rs.getLong(6));
                  t.setTar_acc_id(rs.getString(7));
                  a.add(t);
                }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}
		   
		   return a;
		   
	   }
	public ArrayList<Transaction> gettenstatement(String accountid) {
		
		 ArrayList<Transaction> a=new ArrayList<Transaction>();
		 int i=0;
		   con = DBUtil.getConnection();
		   try {
			ps=con.prepareStatement("select a.transaction_id, a.account_id, a.action ,a.transaction_date," +
					"a.amount,a.cur_balance,a.target_account_id from tbl_Transaction_tja58 a " +
					" where a.account_id=? order by a.transaction_id desc");
     
			ps.setString(1, accountid);
       
         ResultSet rs=ps.executeQuery();
       
          while (rs.next()) {
          	 Transaction t=new Transaction();
                t.setTran_id(rs.getLong(1));
                t.setAcc_id(rs.getString(2));
                t.setAction(rs.getString(3));
                t.setDate(rs.getDate(4));
                t.setAmount(rs.getLong(5));
                t.setCurr_balance(rs.getLong(6));
                t.setTar_acc_id(rs.getString(7));
                a.add(t);
                i++;
                if(i==10)
                {
                	break;
                }
              }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}
		
		return a;
	}
	public ArrayList<String> getacctsofcust(String customer_id) {
		ArrayList<String> accountid = new ArrayList<String>();
		   con = DBUtil.getConnection();
		try {
			ps = con.prepareStatement("select account_id from tbl_account_tja58 where customer_id=? and status='Active'");
			ps.setString(1, customer_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
                accountid.add(rs.getString(1));
                }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
             DBUtil.closeStatement(ps);
			
			 DBUtil.closeConnection(con);
		}

		return accountid;
		
	}
	public void updateDate(long tran, String accId) {
		// TODO Auto-generated method stub
		 con = DBUtil.getConnection();
			
				try {
					ps = con.prepareStatement("update tbl_account_tja58 set credited_lastdate=credited_date where account_id=?");
					ps.setString(1,  accId);
					ps.executeUpdate();
					String query="update tbl_account_tja58 set credited_date=(select transaction_date from tbl_Transaction_tja58 where transaction_id =?) where account_id=?";
				    ps=con.prepareStatement(query);
				    ps.setLong(1,  tran);
				    ps.setString(2, accId);
				    ps.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
		             DBUtil.closeStatement(ps);		 			
					 DBUtil.closeConnection(con);
				}
			
		
		
	}
	public void numberoftrans(String a)
	{
		  int b=0;
		 con = DBUtil.getConnection();
		 try {
			ps=con.prepareStatement("select count(transaction_id) from tbl_Transaction_tja58 where account_id=?");
			ps.setString(1, a);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				b=rs.getInt(1);
			}
			ps=con.prepareStatement("update TBL_ACCOUNT_TJA58  set noofTranscation=? where account_id=? ");
			ps.setInt(1, b);
			ps.setString(2, a);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally {
             DBUtil.closeStatement(ps);		 			
			 DBUtil.closeConnection(con);
		
	}
			
	

	}

	public ArrayList<Transaction> getalltransatcions()
	{
		ArrayList<Transaction> a=new ArrayList<Transaction>();
		 
		   con = DBUtil.getConnection();
		   try {
			ps=con.prepareStatement("select a.transaction_id, a.account_id, a.action ,a.transaction_date," +
					"a.amount,a.cur_balance,a.target_account_id  from tbl_Transaction_tja58 a  ");

			
      
        ResultSet rs=ps.executeQuery();

    
         while (rs.next()) {
         	 Transaction t=new Transaction();
               t.setTran_id(rs.getLong(1));
               t.setAcc_id(rs.getString(2));
               t.setAction(rs.getString(3));
               t.setDate(rs.getDate(4));
               t.setAmount(rs.getLong(5));
               t.setCurr_balance(rs.getLong(6));
               t.setTar_acc_id(rs.getString(7));
               a.add(t);
             }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeStatement(ps);
			
			DBUtil.closeConnection(con);
		}
		   
		   return a;
		   
	   
		
		
	}
}
