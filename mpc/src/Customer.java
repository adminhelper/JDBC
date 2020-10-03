import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class Customer {

	public boolean myInfo(String Id) {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		
		try {
//				3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

//				7) 데이터 검색
			sb.setLength(0);
			sb.append("SELECT * FROM member_info WHERE 회원ID='"+Id+"'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			System.out.println("==========================================================================");
			while (rs.next()) {
				System.out.printf("ID : %s\t",rs.getString(1));
				System.out.printf("이름 : %s\t",rs.getString(2));
				System.out.printf("좌석번호 : %s\t",rs.getString(3));
				System.out.printf("시작시간 : %s\t",rs.getString(4));
				System.out.println("종료시간 : "+rs.getString(5));
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

	public boolean seatMove(String Id,int[] seat) {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		int nSeat=0;
		
		try {
//				3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

			StringBuffer sb = new StringBuffer();
			int uCount = 0;

//				7) 데이터 검색
			while(true) {
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
			
			System.out.println("몇번자리로 바꾸시겠습니까?");
			nSeat=sc.nextInt();
			
			
			sb.append("UPDATE member_join SET seat_no=" + nSeat + " WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();			
			sb.setLength(0);
			sb.append("UPDATE seating SET seat_vacancy='X' WHERE seat_no=" + nSeat + "");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			sb.setLength(0);
			sb.append("UPDATE seating SET seat_vacancy='O' WHERE seat_no=" + seat[0] + "");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			
			seat[0]=nSeat;
				}catch(Exception e) {
					sc.nextLine();
					System.out.println("자리를 선택할 수 없습니다. 다시 선택해주세요");
					continue;
				}
			break;
			}
			
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

	
	
	public boolean orderFood(String Id,int[] seat,int a) {
//      2) 클래스 변수 준비
      Scanner sc = new Scanner(System.in);
      Connection con = null; // 오라클 서버 연결
      PreparedStatement pstmt = null; // SQL문 전달
      ResultSet rs = null; // SELECT문의 결과
      String YorN, Order;
      java.util.Date uDate = new java.util.Date();
      Timestamp sDate = new java.sql.Timestamp(uDate.getTime());
      int i=0;

      try {
//         3) 오라클 서버에 접속
         con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");
         StringBuffer sb = new StringBuffer();
         int uCount = 0;
         //메뉴판 보여주기 
         while (true) {
            System.out.println("주문 하시겠습니까? Y/N");
            YorN = sc.next();
            if(YorN.equals("Y")) {
               System.out.println("====================================================");
               System.out.println("  [라면류]            [음료수류]           [스낵류]      ");
               System.out.println("1.신라면 3000원           6.콜라 1500원              11.숏다리 1000원   ");
               System.out.println("2.왕뚜껑 2500원           7.파워에이드 2000원      12.나나콘 1000원   ");
               System.out.println("3.참깨라면 3000원        8.아리조나 8000원         13.홈런볼 2000원   ");
               System.out.println("4.짜파게티 2500원        9.스프라이트 1500원      14.쫄병 1500원      ");
               System.out.println("5.불닭볶음면 2000원     10.닥터페퍼 2000원                                   ");
               System.out.println("====================================================");
               
               sb.setLength(0);
               while (true) {
                  System.out.print("메뉴 선택 >>");
                  Order = sc.next();
                  sb.append("INSERT INTO menu VALUES(?,?,?,?,?,?,?)");
                  pstmt = con.prepareStatement(sb.toString());
                  pstmt.setInt(1,++i);
                  pstmt.setString(2,null);
                  pstmt.setInt(3,0);
                  pstmt.setTimestamp(4,sDate);
                  pstmt.setString(5,Id);
                  pstmt.setInt(6,seat[0]);
                  pstmt.setString(7,Order);
                  
                  rs = pstmt.executeQuery();
                  sb.setLength(0);
                  sb.append("UPDATE menu m SET pname =(SELECT pname FROM inventory i WHERE m.pno=i.pno),"
                        +" pprice=(SELECT pprice FROM inventory i WHERE m.pno=i.pno)"+ " WHERE m.pno="+Order);
                  pstmt = con.prepareStatement(sb.toString());
                  rs = pstmt.executeQuery();
                  sb.setLength(0);
                  sb.append("UPDATE inventory SET premain=premain-1 WHERE pno="+Order);
                  pstmt = con.prepareStatement(sb.toString());
                  rs = pstmt.executeQuery();
                  
                  sb.setLength(0);
                  sb.append("SELECT premain FROM inventory WHERE pno="+Order);
                  pstmt = con.prepareStatement(sb.toString());
                  rs = pstmt.executeQuery();
                  
                  if(rs.next()) {
                	  if(rs.getString(1).equals("-1")) {
                		  System.out.println("수량이 다 떨어졌습니다 다시 주문해 주세요");
                		  sb.setLength(0);
                          sb.append("UPDATE inventory SET premain=premain+1 WHERE pno="+Order);
                          pstmt = con.prepareStatement(sb.toString());
                          rs = pstmt.executeQuery();
                          sb.setLength(0);
                          sb.append("DELETE FROM menu WHERE pno="+i);
                          pstmt = con.prepareStatement(sb.toString());
                          rs = pstmt.executeQuery();
                          i=i-1;
                          
                		  break;
                	  }
                  }
                  
                  
                  
                  sb.setLength(0);
                  break;
               }
               
            }else if(YorN.equals("N")) {
               return true;
            }else {
               System.out.println("다시 입력해주세요.");
            }
         }
         
         

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

	public boolean myInfoUp(String Id) {
        String a = " ";
//        2) 클래스 변수 준비
        Scanner sc = new Scanner(System.in);
        Connection con = null; // 오라클 서버 연결
        PreparedStatement pstmt = null; // SQL문 전달
        ResultSet rs = null; // SELECT문의 결과

        try {
//              3) 오라클 서버에 접속
           con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");

           StringBuffer sb = new StringBuffer();
           int uCount = 0;
           
           System.out.println("변경 할 패스워드 : ");
           String cpw =sc.next();
           sb.setLength(0);
           sb.append("UPDATE member_join SET pw='" + cpw + "' WHERE mno_id ='" + Id + "'");
           pstmt = con.prepareStatement(sb.toString());
           rs = pstmt.executeQuery();
          
           System.out.println("변경 할 주소 : ");
           String caddr = sc.next();    
           sb.setLength(0);
           sb.append("UPDATE member_join SET addr='" + caddr + "' WHERE mno_id ='" + Id + "'");
           pstmt = con.prepareStatement(sb.toString());
           rs = pstmt.executeQuery();
        
           System.out.println("변경 할 휴대전화번호 : ");
           String cph = sc.next();
           sb.setLength(0);
           sb.append("UPDATE member_join SET mobile_no='" + cph + "' WHERE mno_id ='" + Id + "'");
           pstmt = con.prepareStatement(sb.toString());
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
	public boolean ccharge(String Id) {
		String a = " ";
//		2) 클래스 변수 준비
		Scanner sc = new Scanner(System.in);
		Connection con = null; // 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null; // SELECT문의 결과
		int money=0;
		try {
//			3) 오라클 서버에 접속
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");
			StringBuffer sb = new StringBuffer();

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
					sc.nextLine();
					System.out.println("숫자를 입력해주세요");
				}
			}
			sb.setLength(0);
			sb.append("UPDATE usage_fee SET usage_fee=usage_fee+" + money + " WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			sb.setLength(0);

			sb.append("SELECT usage_fee FROM usage_fee WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				System.out.println(money + "원이 충전되었습니다.");
			}
			
			
			sb.setLength(0);
			int uCount = 0;
			sb.append("UPDATE usage_fee SET logout_time=logout_Time + (("+money+"/1000)/24) WHERE mno_id='" + Id + "'");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			
//				} catch (Exception e) {
//					sc.nextLine();
//				System.out.println("문자를 입력하셨거나 자리를 선택할 수 없습니다. 다시 입력해주세요");
//					continue;
//				}
//			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
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
	
	   public boolean logOut(String Id,int seat) {
		      Connection con = null; // 오라클 서버 연결
		      PreparedStatement pstmt = null; // SQL문 전달
		      ResultSet rs = null; // SELECT문의 결과
		      String YN;
		      java.util.Date uDate = new java.util.Date();
		      Timestamp sDate = new java.sql.Timestamp(uDate.getTime());
		      int i = 0;

		      try {
//		         3) 오라클 서버에 접속
		         con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yhpk", "yhpk");
		         StringBuffer sb = new StringBuffer();
		         Scanner sc = new Scanner(System.in);
		         int uCount = 0;
		         // 메뉴판 보여주기
		         while (true) {
		            System.out.println("정말로 종료하시겠습니까?");
		            YN = sc.next();
		            if (YN.equals("Y")) {
		               sb.setLength(0);
		               //USAGE_FEE TABLE에는 로그아웃하는 사용자의 데이터 삭제
		               sb.append("DELETE FROM usage_fee WHERE mno_id='"+Id+"'");
		                  pstmt = con.prepareStatement(sb.toString());
		                  rs = pstmt.executeQuery();
		                  sb.setLength(0);
		               //MENU TABLE에 저장된 사용자가 주문한 금액 반환 
		                  sb.append("SELECT seat_no, mno_id, SUM(pprice) FROM menu GROUP BY seat_no, mno_id HAVING mno_id='"+Id+"'");
		                  pstmt = con.prepareStatement(sb.toString());
		                  rs = pstmt.executeQuery();
		                  while(rs.next()) { //1.seat_no 2.ID, 3. 총금액 rs.getString(#)
		                     System.out.println(seat+"번 자리의 "+Id+"님이 주문한 총 금액은"+rs.getString(3)+"입니다. 정산부탁드립니다.");
		                     System.out.println("이용해주셔서 감사합니다.");
		                  }
		                      sb.setLength(0);
		               //SEATING TABLE에 있는 사용한 seat_vacancy 자리 X->O로 반환
		                  sb.append("UPDATE seating SET seat_vacancy='O' WHERE seat_no="+seat);
		                  pstmt = con.prepareStatement(sb.toString());
		                  rs = pstmt.executeQuery();
		                  sb.setLength(0);
		               //MEMBER_JOIN TABLE에 있는 사용한 seat_no 반환
		                  sb.append("UPDATE member_join SET seat_no=NULL WHERE mno_id='"+Id+"'");
		                  pstmt = con.prepareStatement(sb.toString());
		                  rs = pstmt.executeQuery();
		                  sb.setLength(0);
		                  break;

		            } else if (YN.equals("N")) {
		               return true;
		            } else {
		               System.out.println("다시 입력해주세요.");
		               continue;
		            }
		         }

		      } catch (SQLException e) {
		         e.printStackTrace();
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
		      return false;
		   }
}
	
	
	
	
	