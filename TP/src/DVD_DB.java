import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Savepoint;
import java.sql.Statement;

public class DVD_DB {
	private Connection connection = null;
	private Statement statement = null;
	
	public DVD_DB(Connection connection) {
		this.connection = connection;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//시퀀스 이용하기, 한 사람당 대출 개수 제한 하기 구현 아직 안함.. return 값에 따라 출력하는 것도오오오오오오ㅗㅗㅗ
	
	
	
	//DVD_INFO
	public ResultSet DVDList(String choice) {
		ResultSet resultSet= null;
		String sql = null;
		if(choice.equals("1")) {//이름순서
			sql = "SELECT dvd_info.dvd_no, dvd_name, isrent, rating, genre, NVL(return_date, '-') "
				+ "FROM dvd_info "
			    + "LEFT JOIN rental ON dvd_info.dvd_no=rental.dvd_no "
			    + "ORDER BY dvd_name";
		}
		else if(choice.equals("2")) {//장르별
			sql = "SELECT dvd_info.dvd_no, dvd_name, isrent, rating, genre, NVL(return_date, '-')\r\n" + 
				  "FROM dvd_info\r\n" + 
				  "LEFT JOIN rental ON dvd_info.dvd_no=rental.dvd_no\r\n" + 
				  "ORDER BY genre";
		}
		try {
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	} 
	
	public ResultSet searchDVD(String searchName) {
		ResultSet resultSet= null;
		String sql = "SELECT dvd_info.dvd_no, dvd_name, isrent, rating, genre, NVL(return_date, '-')\r\n" + 
					 "FROM dvd_info\r\n" + 
					 "LEFT JOIN rental ON dvd_info.dvd_no=rental.dvd_no\r\n" + 
					 "WHERE dvd_name Like '%"+searchName+"%'";
		try {			
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	//dvd_no 부분은 SEQUENCE로 처리할 것.
	public void insertDVD(String insertedDVD[]) {
		String sql = "INSERT INTO dvd_info (dvd_no, dvd_name, isrent, rating, genre)\r\n" + 
					 "VALUES(dvd_info_no_seq.nextval,'"+insertedDVD[0]+"','N',"+insertedDVD[1]+",'"+insertedDVD[2]+"')";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int updateDVD(String update[]) {//밖에서 미리 dvd_name, rating, genre입력 된 어레이를 받아와야 한다.
		String sql = "UPDATE dvd_info SET ";
		boolean isChange[] = new boolean[3];
		isChange[0] = !update[0].equals("dvd_name='-'");
		isChange[1] = !update[1].equals("rating=-");
		isChange[2] = !update[2].equals("genre='-'");
		boolean isBefore = false;
		for (int i = 0; i < isChange.length; i++) {
			if(isChange[i]) {
				if(isBefore)
					sql += ", ";
				sql += update[i];
			}
			isBefore |= isChange[i];
		}
		sql +="\r\nWHERE dvd_no="+update[3];
		try {
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int deleteDVD(String dvd_no) {
		String sql = "DELETE FROM dvd_info\r\n" + 
					 "WHERE dvd_no="+dvd_no;
		try {
			return statement.executeUpdate(sql);
		} catch(SQLIntegrityConstraintViolationException e1) {
			return 2;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0; //0 = 삭제 할 데이터가 없음, 1=삭제 완료 2=삭제 불가
	}//DVD_INFO end
	
	//RENTAL
	public int rentDVD(String rentalInfo[]) {
		String checkDVDsql = "SELECT isrent, rating FROM dvd_info WHERE dvd_no=" + rentalInfo[1];
		String checkMembersql = "SELECT member_id, (TO_CHAR(sysdate,'YYYY')-member_birth) FROM member WHERE member_id='" + rentalInfo[0] + "'";
		String checkOverduesql = "SELECT return_date FROM rental WHERE member_id='" + rentalInfo[0] + "' AND TO_CHAR(sysdate, 'YYYY/MM/DD')>return_date";
		String updateDVDsql = "UPDATE dvd_info SET isrent='Y' \r\n"
							+ "WHERE dvd_no=" + rentalInfo[1];
		String insertRentalsql = "INSERT INTO rental\r\n"
							   + "VALUES('"+rentalInfo[1]+"', '"+rentalInfo[0]+"',TO_CHAR(sysdate, 'YYYY/MM/DD'), TO_CHAR(sysdate + 13, 'YYYY/MM/DD'))";
		Savepoint save = null;
		try {
			connection.setAutoCommit(false);
			save = connection.setSavepoint();
			ResultSet resultSet = statement.executeQuery(checkDVDsql);
			int rating = 0;
			if(!resultSet.next())
				return 0;//0 그런 자료가 없음
			else if(resultSet.getString(1).equals("Y"))
				return 1;//1 이미 대출중인 자료
			rating = Integer.parseInt(resultSet.getString(2));
			resultSet = statement.executeQuery(checkMembersql);
			if(!resultSet.next())
				return 2;//2 그런 회원 없음
			else {
				if((Integer.parseInt(resultSet.getString(2)) - rating) < 0)
					return 5;//5대출 불가한 연령임
			}
			resultSet = statement.executeQuery(checkOverduesql);
			resultSet.last();
			int rowCnt = resultSet.getRow();
			if(rowCnt > 0)
				return 3;//3연체중인 회원
			resultSet = memberRentalList(rentalInfo[0]);
			resultSet.last();
			rowCnt = resultSet.getRow();
			if(rowCnt == 3)
				return 4;//4이미 3개을 대여 중
			statement.executeUpdate(updateDVDsql);
			statement.executeUpdate(insertRentalsql);
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback(save);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 6;//대출 완료
	}
	
	public int returnDVD(String dvd_no) {
		String checksql = "SELECT isrent FROM dvd_info WHERE dvd_no="+dvd_no;
		String updateDVDsql = "UPDATE dvd_info SET isrent='N' \r\n" +
							  "WHERE dvd_no=" + dvd_no;
		String deleteRentalsql = "DELETE FROM rental \r\n" + 
				 				 "WHERE dvd_no="+dvd_no;
		Savepoint save = null;
		try {
			connection.setAutoCommit(false);
			save = connection.setSavepoint();
			ResultSet resultSet = statement.executeQuery(checksql);
			if(!resultSet.next())
				return 0;//0 DVD존재x
			else if(resultSet.getString(1).equals("N"))
				return 1;//1 대여중인 DVD가 아님
			statement.executeUpdate(deleteRentalsql);
			statement.executeUpdate(updateDVDsql);
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback(save);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 2;//2 정상 반납 완료
	}
	
	public ResultSet overduedDVDList() {
		String sql = "SELECT rental.dvd_no, dvd_name, rental.member_id, member_name, member_phone, return_date\r\n" + 
					 "FROM rental\r\n" + 
					 "JOIN dvd_info ON rental.dvd_no=dvd_info.dvd_no\r\n" + 
					 "JOIN member ON rental.member_id=member.member_id\r\n" + 
					 "WHERE TO_CHAR(sysdate, 'YYYY/MM/DD')>return_date";
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}//RENTAL end
	
	//MEMBER
	public ResultSet memberList() {
		ResultSet resultSet= null;
		String sql = null;
		sql = "SELECT * \r\n"
			+ "FROM member \r\n"
			+ "ORDER BY member_name";
		try {
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	} 
	
	public ResultSet memberRentalList(String member_id) {
		ResultSet resultSet= null;
		String sql = "SELECT rental.dvd_no, dvd_name, rental_date, return_date \r\n" + 
					 "FROM member\r\n" + 
					 "JOIN rental ON member.member_id=rental.member_id\r\n" +
					 "JOIN dvd_info ON dvd_info.dvd_no=rental.dvd_no \r\n" +
					 "WHERE rental.member_id='" + member_id + "' \r\n" +
					 "ORDER BY return_date";
		try {
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	//member_id 부분은 SEQUENCE로 처리할 것.
	public void insertMember(String insertedMember[]) {
		String sql = "INSERT INTO member \r\n" + 
					 "VALUES(TO_CHAR(SYSDATE, 'YYYY')||TRIM(TO_CHAR(member_id_seq.nextval,'000')),'"+insertedMember[0]+"','"+insertedMember[1]+"','"+insertedMember[2]+"','"+insertedMember[3]+"', ' "+insertedMember[4]+"')";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int updateMember(String update[]) {//밖에서 미리 데이터가 입력 된 어레이를 받아와야 한다.
		String sql = "UPDATE member SET ";
		boolean isChange[] = new boolean[5];
		isChange[0] = !update[0].equals("member_name='-'");
		isChange[1] = !update[1].equals("member_sex='-'");
		isChange[2] = !update[2].equals("member_birth='-'");
		isChange[3] = !update[3].equals("member_phone='-'");
		isChange[4] = !update[4].equals("member_addr='-'");
		boolean isBefore = false;
		for (int i = 0; i < isChange.length; i++) {
			if(isChange[i]) {
				if(isBefore)
					sql += ", ";
				sql += update[i];
			}
			isBefore |= isChange[i];
		}
		sql +="\r\nWHERE member_id='"+update[5]+"'";
		try {
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int deleteMember(String member_id) {
		String sql = "DELETE FROM member\r\n" + 
					 "WHERE member_id="+member_id;
		try {
			return statement.executeUpdate(sql);
		} catch(SQLIntegrityConstraintViolationException e1) {
			return 1;//2 자료를 대여중인 회원
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 2; //0 = 삭제 할 데이터가 없음, 2=삭제 완료
	}
}