
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Scanner;

public class Join {

//	1) OracleDriver 클래스를 객체화한다
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean join() {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		String Id;
		String name;
		try {
//				3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");
			StringBuffer sb = new StringBuffer();
			//int uCount = 0;
			java.util.Date uDate = new java.util.Date();
			Timestamp sDate = new java.sql.Timestamp(uDate.getTime());
			while (true) {
				try {
					sb.append("INSERT INTO member_join VALUES(?,?,?,?,?,?,?)");
					pstmt = con.prepareStatement(sb.toString());
					System.out.print("id 입력 >>");
					Id = sc.nextLine();
					pstmt.setString(1, Id);
					System.out.print("이름 입력 >>");
					name = sc.nextLine();
					pstmt.setString(2, name);
					System.out.print("PW 입력 >>");
					a = sc.nextLine();
					pstmt.setString(3, a);
					System.out.print("주소 입력 >>");
					a = sc.nextLine();
					pstmt.setString(4, a);
					System.out.print("휴대전화 입력 >>");
					a = sc.nextLine();
					pstmt.setString(5, a);
					pstmt.setNull(6, Types.NULL);
					pstmt.setTimestamp(7, sDate);
					rs = pstmt.executeQuery();
					//uCount = pstmt.executeUpdate();
					break;
				}catch (Exception e) {
					System.out.println("중복된 ID입니다. 다시 가입해주세요.");
					continue;
				}
			}
			sb.setLength(0);
			sb.append("SELECT * FROM member_join WHERE mno_ID='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			System.out.println("===============================================================================");
			while (rs.next()) {
				System.out.printf("ID : %s\t",rs.getString(1));
				System.out.printf("이름 : %s\t",rs.getString(2));
				System.out.printf("패스워드 : %s\t",rs.getString(3));
				System.out.printf("주소 : %s\t",rs.getString(4));
				System.out.println("휴대전화 : "+rs.getString(5));
			}
			System.out.println("===============================================================================");
			
			sb.setLength(0);
			sb.append("INSERT INTO usage_fee VALUES(?,?,?,?,?)");
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, Id);
			pstmt.setString(2, name);
			pstmt.setDate(3, null);
			pstmt.setDate(4, null);
			pstmt.setInt(5, 0);
			rs = pstmt.executeQuery();

		} catch (

		SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean Login() {
		View vi = new View();
		String a = " ";
		int[] seating = new int[1];
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		String Id;
		String Pw;
		int seat;
		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

			sb.setLength(0);
			System.out.print("id 입력 >>");
			Id = sc.next();
			System.out.print("pw 입력 >>");
			Pw = sc.next();
			sb.append("SELECT * FROM member_join");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {

				if (Id.equals(rs.getString(1))) {
					if (Pw.equals(rs.getString(3))) {
						if (rs.getString(1).equals("admin")) {
							vi.loop2();
						} else {
							seat = seatSelect(Id);
							if (seat < 0) {
								return true;
							} else {
								vi.loop3(Id, seat);
							}
						}

						return true;
					}
					System.out.println("비밀번호가 틀렸습니다.");
					return true;
				}

			}
			System.out.println("존재하지 않는 아이디입니다.");

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean Idpw() {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		String Id;
		String Pw;

		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

			sb.setLength(0);
			System.out.print("id 입력 >>");
			Id = sc.next();
			System.out.print("이름 입력 >>");
			String name = sc.next();
			System.out.print("휴대폰번호 입력 >>");
			String phone = sc.next();
			sb.append("SELECT * FROM member_join WHERE mno_id='" + Id + "' AND mname='" + name + "' AND mobile_no='"
					+ phone + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.print("Id :" + rs.getString(1) + "		");
				System.out.println("Pw :" + rs.getString(3));
			}
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;

	}

	public boolean charge() {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		String Id;
		int money;

		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

			sb.setLength(0);
			while (true) {
				System.out.print("id 입력 >>");
				Id = sc.next();
				sb.append("SELECT mno_id FROM usage_fee WHERE mno_id='" + Id + "'");
				pstmt = con.prepareStatement(sb.toString());
				rs = pstmt.executeQuery();

				if (rs.next()) {

					break;
				}
				System.out.println("다시 입력해주세요");

				sb.setLength(0);
			}
			while (true) {
				try {
					System.out.print("충전 금액 입력 >>");
					money = sc.nextInt();
					if (money < 0) {
						System.out.println("금액을 제대로 입력해주세요");
					} else {
						break;
					}
				} catch (Exception e) {
					sc.hasNextLine();
					System.out.println("숫자를 입력해주세요");
				}
			}
			sb.setLength(0);
			sb.append("UPDATE usage_fee SET usage_fee=" + money + " WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			sb.setLength(0);

			sb.append("SELECT usage_fee FROM usage_fee WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString(1) + "원이 충전되었습니다.");
			}
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;

	}

	public int seatSelect(String Id) {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		int seat = 0;

		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");
			StringBuffer sb = new StringBuffer();

			sb.append("SELECT usage_fee FROM usage_fee WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getString(1).equals("0")) {
				seat = -1;
				System.out.println("요금 충전 후 이용하시기 바랍니다");
				return seat;
			}

			sb.setLength(0);
			int uCount = 0;
			while (true) {
				try {
					sb.setLength(0);
					sb.append("SELECT * FROM seating");
					pstmt = con.prepareStatement(sb.toString());
					rs = pstmt.executeQuery();
					System.out.println("==========================================================================");
					while (rs.next()) {
						System.out.printf("좌석번호 : %s\t", rs.getString(1));
						System.out.println("공석여부 :" + rs.getString(2));
					}
					System.out.println("==========================================================================");
					sb.setLength(0);
					System.out.print("몇번 자리를 선택하시겠습니까? >>");
					seat = sc.nextInt();

					sb.append("UPDATE member_join SET seat_no=" + seat + " WHERE mno_id='" + Id + "'");
					pstmt = con.prepareStatement(sb.toString());
					rs = pstmt.executeQuery();
					sb.setLength(0);
					
					sb.append("UPDATE seating SET seat_vacancy='X' WHERE seat_no=" + seat + "");
					pstmt = con.prepareStatement(sb.toString());
					rs = pstmt.executeQuery();
					sb.setLength(0);
					break;
				} catch (Exception e) {
					sc.nextLine();
					System.out.println("자리를 선택할 수 없습니다. 다시 선택해주세요");
					continue;
				}
			}
			sb.append("UPDATE usage_fee SET login_time=SYSDATE WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			sb.setLength(0);
			sb.append("UPDATE usage_fee SET logout_time=SYSDATE + ((usage_fee/1000)/24) WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			return seat;
//				} catch (Exception e) {
//					sc.nextLine();
//				System.out.println("문자를 입력하셨거나 자리를 선택할 수 없습니다. 다시 입력해주세요");
//					continue;
//				}
//			}
		} catch (SQLException e) {
			e.printStackTrace();
			return seat;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}