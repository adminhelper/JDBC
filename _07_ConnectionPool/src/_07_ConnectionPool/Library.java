package _07_ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class Library {
	static final String BOOK_NAME = "1";
	static final String WRITER = "2";
	static final String PUBLISHER = "3";
	static final String SEPARATE_NUMBER = "4";
	static Scanner sc = new Scanner(System.in);
	static Connection con = null; // 오라클 서버 연결
	static PreparedStatement pstmt = null; // SQL문 전달
	static ResultSet rs = null; // SELECT문의 결과
	static StringBuffer sb = new StringBuffer();

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	static void showMenu() {
		System.out.println(" ┌─────────────────────────────────────────┐");
		System.out.println(" │                  ┌──┐                   │");
		System.out.println(" │                  │■ │                   │");
		System.out.println(" │                  └──┘                   │");
		System.out.println(" └─────────────────────────────────────────┘");
		System.out.println("                   비트도서관에 오신걸 환영합니다.");
		System.out.println();
		System.out.print("1. 회원가입	");
		System.out.print("2. 회원정보조회	");
		System.out.println("3. 책 검색	");
		System.out.print("4. 책 대여	");
		System.out.print("5. 책 반납    ");
		System.out.println("            6. 시스템종료");
		System.out.println();
		System.out.print("입력 >> ");

	}

	// ex. x=6이면 여섯자리 뽑혀야함
	// ex. x=4면 네자리 뽑혀야함
	static int makeRd(int x) throws SQLException {
		Random rd = new Random();
		int y = (int) (Math.pow(10, x) - 1 - (Math.pow(10, x - 1)));
		int num = rd.nextInt(y) + (int) Math.pow(10, x - 1);
		pstmt = con.prepareStatement("SELECT * FROM bookmember WHERE member_number=" + num);
		rs = pstmt.executeQuery();
		if (rs.next() == false) {
			return num;
		} else
			return makeRd(x);
	}

	static void signUp() throws SQLException {
		sb.setLength(0);
		int memberNum = makeRd(4);
		System.out.print("회원명 입력 >> ");
		String nameInsert = sc.nextLine();
		nameInsert = sc.nextLine();
		System.out.print("주민등록번호 입력 >> ");
		String idnumberNumInsert = sc.next();
		System.out.print("주소 입력 >> ");
		String addressNumInsert = sc.nextLine();
		addressNumInsert = sc.nextLine();
		System.out.print("전화번호 입력>> ");
		String phoneNumInsert = sc.next();
		System.out.print("전자우편 입력 >> ");
		String e_mailInsert = sc.next();
		sb.append("INSERT INTO BOOKMEMBER(MEMBER_NUMBER,NAME,IDNUMBER,ADDRESS,PHONE,E_MAIL, late_fee) "
				+ "values(?,?,?,?,?,?,?)");
		pstmt = con.prepareStatement(sb.toString());
		int cnt = 0;
		pstmt.setInt(++cnt, memberNum);
		pstmt.setString(++cnt, nameInsert);
		pstmt.setString(++cnt, idnumberNumInsert);
		pstmt.setString(++cnt, addressNumInsert);
		pstmt.setString(++cnt, phoneNumInsert);
		pstmt.setString(++cnt, e_mailInsert);
		pstmt.setInt(++cnt, 0);
		System.out.println("회원가입을 축하드립니다.");
		pstmt.executeQuery();
		System.out.println(memberNum);
	}


	

	   static void editMemberInfo() throws SQLException {

	      pstmt = con.prepareStatement(
	            "SELECT MEMBER_NUMBER, NAME, ADDRESS, PHONE, E_MAIL FROM BOOKMEMBER WHERE MEMBER_NUMBER ="
	                  + rs.getString(1));
	      rs = pstmt.executeQuery();
	      rs.next();// 첫번째값이 컬럼명이아니라 데이터가 될수있게 한줄 내려줌
	      System.out.println("=======================");
	      System.out.println(">>> 회원정보수정");
	      System.out.println("이전주소>>");
	      System.out.println(rs.getString(3));
	      System.out.println("수정주소>>");
	      sc.nextLine();
	      String updateAdd = sc.nextLine();

	      System.out.println("이전전화번호>>");
	      System.out.println(rs.getString(4));
	      System.out.println("수정전화번호>>");
	      String updatePhone = sc.next();

	      System.out.println(updatePhone);
	      System.out.println("이전전자우편>>");
	      System.out.println(rs.getString(5));
	      System.out.println("수정전자우편>>");
	      String updateEmail = sc.next();
	      System.out.println("수정이 완료되었습니다.");

	      pstmt = con.prepareStatement("UPDATE BOOKMEMBER SET ADDRESS = '" + updateAdd + "', PHONE = '" + updatePhone
	            + "', E_MAIL = '" + updateEmail + "' WHERE MEMBER_NUMBER = '" + rs.getString(1) + "'");

	      pstmt.executeUpdate();
	      con.commit();

	   }
	   
	static void memberSearch() throws SQLException {
      while (true) {
         System.out.println("조회하실 이름을 입력하세요");
         System.out.println("(조회 취소: 0 입력)");
         String nameSearch = sc.nextLine();
         
         if (nameSearch.equals("0")) {
            break;
         }
         String nameSearchAdd = ("SELECT * FROM BOOKMEMBER WHERE NAME LIKE '%" + nameSearch + "%'");
         pstmt = con.prepareStatement(nameSearchAdd);
         rs = pstmt.executeQuery();
         if (rs.next() == true) {
            int memSel = 0;
            System.out.println("회원번호 : " + rs.getString(1));
            System.out.println("이름 : " + rs.getString(2));
            System.out.println("주민등록번호 : " + rs.getString(3));
            System.out.println("주소 : " + rs.getString(4));
            System.out.println("전화번호 : " + rs.getString(5));
            System.out.println("전자우편 : " + rs.getString(6));
            if (rs.getInt(7) == 0) {
               System.out.println("연체료 : 연체료없음");
               System.out.println("=======================");
               System.out.println("1. 회원정보수정");
               System.out.println("2. 이전메뉴로");
               memSel = sc.nextInt();
               if (memSel == 1) {
                  editMemberInfo();
                  break;

               } else {
                  break;
               }
            } else {
               System.out.println("연체료 : " + rs.getInt(7));
               System.out.println("=======================");
               System.out.println("1. 연체료 납부");
               System.out.println("2. 회원정보수정");
               System.out.println("3. 이전메뉴로");
               memSel = sc.nextInt();
               if (memSel == 1) {
                  getLateFee();
                  break;
               } else if (memSel == 2) {
                  editMemberInfo();
                  break;
               } else {
                  break;
               }
            }

         } else {
            System.out.println("존재하지 않는 회원입니다.");
            continue;
         }
      }
   }

   static void getLateFee() throws SQLException {
      pstmt = con.prepareStatement("SELECT * FROM BOOKMEMBER WHERE MEMBER_NUMBER =" + rs.getString(1));
      rs = pstmt.executeQuery();
      rs.next();
      System.out.println("=======================");
      System.out.println(">>> 연체료 납부");
      System.out.println("연체료 : " + rs.getInt(7));
      System.out.println(">>> 납부하실 금액을 입력해주세요.");
      System.out.print(">>> ");
      int getFee = sc.nextInt();
      int calcFee = ((rs.getInt(7)) - getFee);
      pstmt = con.prepareStatement(
            "UPDATE BOOKMEMBER SET LATE_FEE =" + calcFee + "WHERE MEMBER_NUMBER =" + rs.getString(1));
      pstmt.executeUpdate();
      con.commit();
 
   }
	static void bookSearch() throws SQLException {
		sb.setLength(0);
		sb.append("SELECT * FROM BOOKS");
		pstmt = con.prepareStatement(sb.toString());
		rs = pstmt.executeQuery();
		System.out.println("1. 책이름으로 찾기");
		System.out.println("2. 글쓴이로 찾기");
		System.out.println("3. 출판사로 찾기");
		System.out.println("4. 주제분류번호로찾기");
		System.out.print("입력 >> ");
		String stringChoice = sc.next();
		switch (stringChoice) {
		case BOOK_NAME:
			System.out.println("책이름을 입력하세요");
			sc.nextLine();
			String bookNameSearch = sc.nextLine();
			String bookSearchSql = "SELECT * FROM BOOKS " + "WHERE BOOK_NAME LIKE '%" + bookNameSearch + "%'";
			pstmt = con.prepareStatement(bookSearchSql, rs.TYPE_SCROLL_SENSITIVE, rs.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					System.out.println("도서번호: " + rs.getString(1));
					System.out.print("책이름 : " + rs.getString(2));
					System.out.print(", 글쓴이 : " + rs.getString(3));
					System.out.print(", 출판사 : " + rs.getString(4));
					System.out.println(", 전자분류기호 : " + rs.getString(5));
					System.out.println();
				}
			} else {
				System.out.println("존재하지 않는 책입니다.");
			}
			break;
		case WRITER:
			System.out.println("글쓴이를 입력하세요");
			sc.nextLine();
			String writerSearch = sc.nextLine();
			String writerSearchSql = "SELECT * FROM BOOKS " + "WHERE WRITER LIKE '%" + writerSearch + "%'";
			pstmt = con.prepareStatement(writerSearchSql, rs.TYPE_SCROLL_SENSITIVE, rs.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					System.out.println("도서번호: " + rs.getString(1));
					System.out.print("책이름 : " + rs.getString(2));
					System.out.print(", 글쓴이 : " + rs.getString(3));
					System.out.print(", 출판사 : " + rs.getString(4));
					System.out.println(", 전자분류기호 : " + rs.getString(5));
					System.out.println();
				}
			} else {
				System.out.println("존재하지 않는 글쓴이입니다.");
			}
			break;
		case PUBLISHER:
			System.out.println("출판사을 입력하세요");
			sc.nextLine();
			String publisherSearch = sc.nextLine();
			String publisherSearchSql = "SELECT * FROM BOOKS " + "WHERE PUBLISHER LIKE '%" + publisherSearch + "%'";
			pstmt = con.prepareStatement(publisherSearchSql, rs.TYPE_SCROLL_SENSITIVE, rs.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					System.out.println("도서번호: " + rs.getString(1));
					System.out.print("책이름 : " + rs.getString(2));
					System.out.print(", 글쓴이 : " + rs.getString(3));
					System.out.print(", 출판사 : " + rs.getString(4));
					System.out.println(", 전자분류기호 : " + rs.getString(5));
					System.out.println();
				}
			} else {
				System.out.println("존재하지 않는 출판사입니다.");
			}
			break;
		case SEPARATE_NUMBER:
			System.out.println("분류번호을 입력하세요");
			System.out.println("1:철학, 2:종교, 3:사회과학, 4:자연과학");
			System.out.println("5:기술과학, 6:예술, 7:연어, 8:문학, 9:역사");
			sc.nextLine();
			String serparateNumberSearch = sc.nextLine();
			String serparateNumberSearchSql = "SELECT * FROM BOOKS " + "WHERE SEPERATE_NUMBER LIKE '"
					+ serparateNumberSearch + "%'";
			pstmt = con.prepareStatement(serparateNumberSearchSql, rs.TYPE_SCROLL_SENSITIVE, rs.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					System.out.println("도서번호: " + rs.getString(1));
					System.out.print("책이름 : " + rs.getString(2));
					System.out.print(", 글쓴이 : " + rs.getString(3));
					System.out.print(", 출판사 : " + rs.getString(4));
					System.out.println(", 전자분류기호 : " + rs.getString(5));
					System.out.println();
				}
			} else {
				System.out.println("존재하지 않는 분류번호입니다.");
			}
			break;
		default:
			System.out.println("잘못된 입력입니다");
			break;
		}
	}

	static void returnBook() throws SQLException {
		while (true) {

			System.out.println("반납하실 책의 관리번호를 입력하세요.");
			System.out.println("(반납 취소: 0 입력)");
			String answer1 = sc.next();
			if (answer1.equals("0")) {
				break;
			}
			System.out.println("반납하시는 회원의 회원번호를 입력하세요.");
			System.out.println("(반납 취소: 0 입력)");
			String answer2 = sc.next();
			if (answer2.equals("0")) {
				break;
			}
			pstmt = con.prepareStatement("SELECT * FROM rental WHERE book_number='" + answer1+"' "
					+ "AND member_number='"+answer2+"'");
			rs = pstmt.executeQuery();
			if (rs.next() == false) {
				System.out.println("유효하지 않은 대출기록입니다.");
				continue;
			}
			else {
				System.out.print("대여번호 : " + rs.getString(1));
				System.out.print(", 대여일자 : " + rs.getString(4));
				System.out.println(", 반납예정일자 : " + rs.getString(5));
				System.out.println("반납하시겠습니까? (y/n)");
				String answer3=sc.next();
				
				if (answer3.equals("y") || answer3.equals("Y")) {
					PreparedStatement pstmt2=con.prepareStatement("SELECT TRUNC(sysdate, 'DD')-TRUNC(return_day, 'DD')"
							+"FROM rental WHERE book_number='" + answer1+"'AND member_number='"
							+answer2+"'");
					ResultSet rs2=pstmt2.executeQuery();
					if(rs2.next()&&rs2.getInt(1)>0) {
						System.out.println("연체비용이 발생했습니다. 반납 완료 후 카운터에 방문하여 납부해주세요.");
						System.out.println("발생한 연체비용: "+(rs2.getInt(1)*200)+"원");
					}
						pstmt2=con.prepareStatement("UPDATE bookmember "
								+"SET late_fee= "+(rs2.getInt(1)*200)+" WHERE member_number='"+answer2+"'");
						pstmt2.executeQuery();
						pstmt2=con.prepareStatement("DELETE rental WHERE rental_number='"
								+rs.getString(1)+"'");
						pstmt2.executeQuery();
						PreparedStatement pstmt3=con.prepareStatement("SELECT * FROM BOOKS "
								+"WHERE book_number='"+rs.getString(2)+"'");
						rs2=pstmt3.executeQuery();
						rs2.next();
						pstmt2=con.prepareStatement("UPDATE BOOKS SET number_of_left="+(rs2.getInt(11)+1)
								+" WHERE book_number='"+rs.getString(2)+"'");
						pstmt2.execute();
						con.commit();
						System.out.println("도서 반납이 완료되었습니다.");
					}
				else if (answer3.equals("n") || answer3.equals("N")) {
					System.out.println("반납이 취소되었습니다.");
					System.out.println();
					continue;
				} else {
					System.out.println("잘못된 입력입니다.");
					System.out.println();
					continue;
				}
			}
		}
	}

	static void rentBook() throws SQLException {
		while (true) {
			sb.setLength(0);
			System.out.println("대여하실 책의 관리번호를 입력하세요.");
			System.out.println("(대여 취소: 0 입력)");
			String num = sc.next();
			if (num.equals("0")) {
				break;
			}
			sb.append("SELECT * FROM BOOKS WHERE book_number=" + num);
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			if (rs.next() == false) {
				System.out.println("잘못된 번호입니다.");
				continue;
			} else {
				System.out.print("책이름 : " + rs.getString(2));
				System.out.print(", 글쓴이 : " + rs.getString(3));
				System.out.println(", 출판사 : " + rs.getString(4));
				System.out.println("찾으시는 책이 맞습니까? (y/n)");
				String answer = sc.next();
				if (answer.equals("y") || answer.equals("Y")) {
					int left = rs.getInt(11);
					if (left >= 1) {
						System.out.println("대여하시는 회원의 회원번호를 입력하세요.");
						String m_num = sc.next();
						System.out.println("대여하시는 회원의 이름을 입력하세요.");
						String m_name = sc.nextLine(); 
						m_name = sc.nextLine();
						pstmt = con.prepareStatement("SELECT * FROM bookmember WHERE name='" + m_name + "'");
						rs = pstmt.executeQuery();
						if (rs.next() == false || !rs.getString(1).equals(m_num)) {
							System.out.println("유효하지 않은 회원입니다.");
							continue;
						}
						Random rd = new Random();
						int r_num = makeRd(6);

						pstmt = con.prepareStatement("INSERT INTO rental (rental_number, book_number,"
								+ "member_number, rent_day, return_day, extend_date)" + "VALUES (" + (r_num + "") + ", "
								+ num + ", " + m_num + ", sysdate, sysdate+7, sysdate+14)");
						pstmt.executeQuery();
						pstmt = con.prepareStatement(
								"UPDATE books SET number_of_left=" + (left - 1) + "WHERE book_number='" + num + "'");
						pstmt.executeQuery();
						con.commit();

						pstmt = con.prepareStatement("SELECT * FROM rental WHERE rental_number='" + (r_num + "") + "'");
						rs = pstmt.executeQuery();
						rs.next();
						System.out.println("도서 대출이 완료되었습니다.");
						System.out.println("대여 번호: " + rs.getString(1));
						System.out.println("반납 기한: " + rs.getString(5));
						System.out.println("대여하신 책의 관리번호는 : " + num + " 입니다");
					} else {
						System.out.println("해당 도서는 이미 전부 대출중입니다.");
						continue;
					}
				} else if (answer.equals("n") || answer.equals("N")) {
					System.out.println("대여가 취소되었습니다.");
					System.out.println();
					continue;
				} else {
					System.out.println("잘못된 입력입니다.");
					System.out.println();
					continue;
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp", "bitcamp");
			for (;;) {
				Thread.sleep(1500);
				showMenu();
				String menuChoice = sc.next();
				if (menuChoice.equals("6")) {
					System.out.println("이용해주셔서감사합니다.");
					break;
				}
				switch (menuChoice) {
				case "1":
					signUp();
					break;
				case "2":
					sc.nextLine();
					memberSearch();
					break;
				case "3":
					bookSearch();
					break;
				case "4":
					rentBook();
					break;
				case "5":
					returnBook();
					break;
				
				default:
					System.out.println("잘못된 입력입니다");
					continue;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}