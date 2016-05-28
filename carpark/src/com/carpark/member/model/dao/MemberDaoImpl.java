package com.carpark.member.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carpark.admin.model.ParkingDto;
import com.carpark.db.DBClose;
import com.carpark.db.DBConnection;
import com.carpark.member.model.MemberDto;

public class MemberDaoImpl implements MemberDao {
	private static MemberDao memberDao;
	
	private MemberDaoImpl(){}
	
	static{
		memberDao = new MemberDaoImpl();
	}
	
	public static MemberDao getMemberDao() {
		return memberDao;
	}

	@Override
	public int register(MemberDto memberDto) {

		int count=0;
		Connection conn=null;
		PreparedStatement pstmt =null;
		try {
			conn=DBConnection.makeConnection();
			String sql="";
			sql += "insert into member(user_name,user_id,user_pass,email,tel,grade_id,user_avgpoint,coin,host_flag,login_key) \n";
			sql += "values(?,?,?,?,?,3,0,0,0,null)";//치환변수 
			pstmt = conn.prepareStatement(sql);//미리 sql 문장을 가져가서 검사하고 틀린게 없을 때 실행
			int idx =1;//중간에 없어지거나 추가될때 필요
			pstmt.setString(idx++, memberDto.getUser_name());
			pstmt.setString(idx++, memberDto.getUser_id());
			pstmt.setString(idx++, memberDto.getUser_pass());
			pstmt.setString(idx++, memberDto.getEmail());
			pstmt.setString(idx++, memberDto.getTel());
			
			System.out.println("id"+memberDto.getUser_id());
			System.out.println("pass"+memberDto.getUser_pass());
			
			count=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBClose.close(conn, pstmt);
		}	
		return count;
	}

	@Override
	public int idcheck(String id) {
		int count=1;
		Connection conn =null;
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		
		try {
			conn=DBConnection.makeConnection();
			String sql="";

			sql+="select user_id \n";
			sql+="from member \n";
			sql+="where user_id=? \n";
		
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1,id);
			rs=pstmt.executeQuery();
			if(rs.next()){
				//이름,이메일,보유코인 필요	
				count = 0;	
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBClose.close(conn, pstmt, rs);
		}
		return count;
	}

	@Override
	public MemberDto getMember(String id) {
		MemberDto memberDto = null;
		Connection conn =null;
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		
		try {
			conn=DBConnection.makeConnection();
			String sql="";

			sql+="select user_name,user_id,coin,user_avgpoint,penalty,user_name,email,tel,grade_id \n";
			sql+="from member \n";
			sql+="where user_id=? \n";
		
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1,id);
			rs=pstmt.executeQuery();
			if(rs.next()){
				//이름,이메일,보유코인 필요
				memberDto =new MemberDto();		
				memberDto.setUser_name(rs.getString("user_name"));
				memberDto.setUser_id(rs.getString("user_id"));
				memberDto.setCoin(rs.getInt("coin"));
				memberDto.setGrade_id(rs.getInt("grade_id"));
				memberDto.setUser_avgPoint(rs.getInt("user_avgpoint"));
				memberDto.setPenalty(rs.getInt("penalty"));
				memberDto.setUser_name(rs.getString("user_name"));
				memberDto.setEmail(rs.getString("email"));
				memberDto.setTel(rs.getString("tel"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBClose.close(conn, pstmt, rs);
		}
		return memberDto;
	}

	@Override
	public int modify(MemberDto memberDto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MemberDto login(Map<String, String> map) {
		MemberDto memberDto = null;
		Connection conn =null;
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		
		try {
			conn=DBConnection.makeConnection();
			String sql="";

			sql+="select user_id,coin,grade_id,user_name,user_avgpoint,email,tel \n";
			sql+="from member \n";
			sql+="where user_id=? and user_pass=? \n";
		
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1,map.get("userid"));
			pstmt.setString(2,map.get("userpwd"));
			rs=pstmt.executeQuery();
			if(rs.next()){
				//이름,이메일,보유코인 필요
				memberDto =new MemberDto();				
				memberDto.setUser_id(rs.getString("user_id"));
				memberDto.setCoin(rs.getInt("coin"));
				memberDto.setGrade_id(rs.getInt("grade_id"));
				memberDto.setUser_name(rs.getString("user_name"));
				memberDto.setUser_avgPoint(rs.getInt("user_avgpoint"));
				memberDto.setEmail(rs.getString("email"));
				memberDto.setTel(rs.getString("tel"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBClose.close(conn, pstmt, rs);
		}
		return memberDto;
	}

	@Override
	public List<ParkingDto> list(Map<String, String> map) {
		//지역정보,시작일,종료일 map에 담아옴
		List<ParkingDto> list=new ArrayList<ParkingDto>();
		ParkingDto parkingDto=null;
		Connection conn =null;
		PreparedStatement pstmt =null;
		ResultSet rs =null;
	
		try {
			conn=DBConnection.makeConnection();
			String sql="";
			sql +="select a.park_id, a.park_name,a.city, a.park_avgPoint, a.park_capacity, a.latitude, a.longitude\n";
			sql +="from (select p.park_id, p.park_name,c.sgg_name ||' '|| c.emd_name as city, pd.park_avgPoint,p.park_capacity,p.latitude,p.longitude\n";
			sql +="			from parking p , cities c, parking_detail pd\n";
			sql +="			where p.emd_code = c.emd_code\n";
			sql +="					and p.park_id = pd.park_id) a\n";
			sql +="where a.city like '%'||?||'%'\n";


			int idx=1;
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(idx++,map.get("city").trim());
//			pstmt.setString(idx++,map.get("from"));
//			pstmt.setString(idx++,map.get("to"));

			rs=pstmt.executeQuery();
			while(rs.next()){
				parkingDto = new ParkingDto();
				parkingDto.setPark_id(rs.getInt("park_id"));
				parkingDto.setPark_name(rs.getString("park_name"));
				parkingDto.setLocation(rs.getString("city")); 		
				parkingDto.setPark_capacity(rs.getInt("park_capacity"));
				parkingDto.setLongitude(rs.getDouble("longitude"));
				parkingDto.setLatitude(rs.getDouble("latitude"));
				list.add(parkingDto);	
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBClose.close(conn, pstmt, rs);
		}
		return list;
	}

	@Override
	public ParkingDto parkingDetail(String parkingId) {
		ParkingDto parkingDto=null;
		Connection conn =null;
		PreparedStatement pstmt =null;
		ResultSet rs =null;
	
		try {
			conn=DBConnection.makeConnection();
			String sql="";
			sql+="select p.park_id, p.park_name,c.sgg_name ||' '|| c.emd_name as city, pd.park_avgPoint,p.park_capacity, \n";
			sql+="p.latitude,p.longitude,p.owner_id \n";
			sql+="from parking p , cities c, parking_detail pd \n";
			sql+="where p.emd_code = c.emd_code \n";
			sql+="and p.park_id = pd.park_id \n";
			sql+="and p.park_id=? ";
			int idx=1;
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(idx++,parkingId);
//			pstmt.setString(idx++,map.get("from"));
//			pstmt.setString(idx++,map.get("to"));

			rs=pstmt.executeQuery();
			while(rs.next()){
				parkingDto = new ParkingDto();
				parkingDto.setPark_id(rs.getInt("park_id"));
				parkingDto.setPark_name(rs.getString("park_name"));
				parkingDto.setLocation(rs.getString("city")); 		
				parkingDto.setPark_capacity(rs.getInt("park_capacity"));
				parkingDto.setLongitude(rs.getDouble("longitude"));
				parkingDto.setLatitude(rs.getDouble("latitude"));
				parkingDto.setOwner_id(rs.getString("owner_id"));
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBClose.close(conn, pstmt, rs);
		}
		
		return parkingDto;
	}

	@Override
	public int addNewCar(String user_id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
