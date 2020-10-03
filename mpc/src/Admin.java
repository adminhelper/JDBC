import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class Admin {
	

	public boolean searchAll() {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		String Id;
		String Pw;

		try {
//				3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

//				7) 데이터 검색
			sb.setLength(0);
			sb.append("SELECT * FROM member_join");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			System.out.println(sb.toString());
			System.out.println("==========================================================================");
			while (rs.next()) {
				System.out.printf("ID : %s\t",rs.getString(1));
				System.out.printf("이름 : %s\t",rs.getString(2));
				System.out.printf("패스워드 : %s\t",rs.getString(3));
				System.out.printf("주소 : %s\t",rs.getString(4));
				System.out.println("휴대전화 : "+rs.getString(5));
				}
			System.out.println("==========================================================================");
			return true;

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

	public boolean searchMember() {
		String a = " ";	
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		String Id;
		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

			sb.setLength(0);
			System.out.print("id 입력>>");
			Id = sc.next();
			sb.append("SELECT * FROM member_join");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {

				if (Id.equals(rs.getString(1))) {
					System.out.println("==========================================================================");
					System.out.printf("ID : %s\t",rs.getString(1));
					System.out.printf("이름 : %s\t",rs.getString(2));
					System.out.printf("패스워드 : %s\t",rs.getString(3));
					System.out.printf("주소 : %s\t",rs.getString(4));
					System.out.println("휴대전화 : "+rs.getString(5));
					System.out.println("==========================================================================");
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

	public boolean seatInfo() {
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

			sb.append("SELECT * FROM seating");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			System.out.println("==========================================================================");
			while (rs.next()) {
				System.out.printf("좌석번호 : %s\t",rs.getString(1));
				System.out.println("공석여부 :" + rs.getString(2));
			}
			System.out.println("==========================================================================");
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
	
	
	public boolean orderInfo() {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과

		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

			sb.append("SELECT * FROM order_info");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			System.out.println("==========================================================================");
			while (rs.next()) {
				System.out.printf("상품주문번호 : %s\t", rs.getString(5));
				System.out.printf("회원 ID : %s\t" , rs.getString(1));
				System.out.printf("회원명 : %s\t" , rs.getString(2));
				System.out.printf("상품명 : %s\t" , rs.getString(3));
				System.out.println("상품가격 : " + rs.getString(4));
			}
			System.out.println("==========================================================================");
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
	
	public boolean employeeInfo() {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과

		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

			sb.append("SELECT * FROM employee_info");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			System.out.println("===============================================================================================================================================");
			while (rs.next()) {
				System.out.printf("회원 ID : %s\t" , rs.getString(1));
				System.out.printf("이름 : %s\t" , rs.getString(2));
				System.out.printf("비밀번호 : %s\t" , rs.getString(3));
				System.out.printf("주소 : %s\t" , rs.getString(4));
				System.out.printf("휴대전화 : %s\t" , rs.getString(5));
				System.out.printf("직책 : %s\t" , rs.getString(6));
				System.out.println("근무형태 : " + rs.getString(7));
			}
			System.out.println("==================================================================================================================================================");
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
	public boolean storeInfo() {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과

		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

			sb.append("SELECT * FROM store_info");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			System.out.println("=================================================================================================================================");
			while (rs.next()) {
				System.out.printf("사업장명 : %s\t" , rs.getString(1));
				System.out.printf("대표번호 : %s\t" , rs.getString(2));
				System.out.printf("주소 : %s\t" , rs.getString(3));
				System.out.println("사무실전화번호 :" + rs.getString(4));
			}
			System.out.println("=================================================================================================================================");
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
	public boolean invenInfo() {
	      String a = " ";
//	      2) 클래스 변수 준비
	      Scanner sc = new Scanner(System.in);
	      Connection con = null; // 오라클 서버 연결
	      PreparedStatement pstmt = null; // SQL문 전달
	      ResultSet rs = null; // SELECT문의 결과

	      try {
//	         3) 오라클 서버에 접속
	         con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

	         StringBuffer sb = new StringBuffer();
	         int uCount = 0;

	         sb.append("SELECT * FROM inventory");
	         pstmt = con.prepareStatement(sb.toString());
	         rs = pstmt.executeQuery();
	         System.out.println(sb.toString());
	         System.out.println("===============================================================================================================================================");
	         while (rs.next()) {
	        	System.out.printf("제품번호 : %s\t" , rs.getString(1));
				System.out.printf("제품이름 : %s\t" , rs.getString(2));
				System.out.printf("제품가격 : %s\t" , rs.getString(3));
				System.out.printf("제품수량 : %s\t" , rs.getString(4));
				System.out.println("제품남은갯수 : " + rs.getString(5));
	            
	         }
	         System.out.println("==================================================================================================================================================");
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
	
	
}