import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class DVDViewer {
	private Scanner sc = new Scanner(System.in);
	
	public String startMenu() {
		String sel = null;
		System.out.println("-----------------------------------------------");
		System.out.println("DVD 관리 시스템");
		System.out.println("1.DVD 관리");
		System.out.println("2.회원 관리");
		System.out.println("3.대출/반납");
		System.out.println("4.시스템 종료");
		System.out.print("무엇을 하시겠습니까 >> ");
		sel = sc.nextLine();
		while(!(sel.equals("1")||sel.equals("2")||sel.equals("3")||sel.equals("4"))) {
			System.out.print("잘못된 입력입니다. 다시 입력해주세요. >> ");
			sel = sc.nextLine();
		}
		return sel;
	}
	
	public String DVDMenu() {
		String sel = null;
		System.out.println("-----------------------------------------------");
		System.out.println("DVD 관리");
		System.out.println("1.DVD 등록");
		System.out.println("2.DVD 정보 수정");
		System.out.println("3.DVD 삭제");
		System.out.println("4.DVD 목록");
		System.out.println("5.DVD 검색");
		System.out.println("6.이전 메뉴");
		System.out.print("무엇을 하시겠습니까 >> ");
		sel = sc.nextLine();
		while(!(sel.equals("1")||sel.equals("2")||sel.equals("3")||sel.equals("4")||sel.equals("5")||sel.equals("6"))) {
			System.out.print("잘못된 입력입니다. 다시 입력해주세요. >> ");
			sel = sc.nextLine();
		}
		return sel;
	}
	
	public String memberMenu() {
		String sel = null;
		System.out.println("-----------------------------------------------");
		System.out.println("회원 관리");
		System.out.println("1.회원 등록");
		System.out.println("2.회원 정보 수정");
		System.out.println("3.회원 삭제");
		System.out.println("4.회원 목록");
		System.out.println("5.회원별 대여 목록 검색");
		System.out.println("6.이전 메뉴");
		System.out.print("무엇을 하시겠습니까 >> ");
		sel = sc.nextLine();
		while(!(sel.equals("1")||sel.equals("2")||sel.equals("3")||sel.equals("4")||sel.equals("5")||sel.equals("6"))) {
			System.out.print("잘못된 입력입니다. 다시 입력해주세요. >> ");
			sel = sc.nextLine();
		}
		return sel;
	}
	
	public String rentalMenu() {
		String sel = null;
		System.out.println("-----------------------------------------------");
		System.out.println("회원 관리");
		System.out.println("1.대여");
		System.out.println("2.반납");
		System.out.println("3.연체 DVD 목록");
		System.out.println("4.이전 메뉴");
		System.out.print("무엇을 하시겠습니까 >> ");
		sel = sc.nextLine();
		while(!(sel.equals("1")||sel.equals("2")||sel.equals("3")||sel.equals("4"))) {
			System.out.print("잘못된 입력입니다. 다시 입력해주세요. >> ");
			sel = sc.nextLine();
		}
		return sel;
	}
	
	//실험용 안씀
	public void ResultView(ResultSet resultSet) {
		ResultSetMetaData rsmd;
		try {
			rsmd = resultSet.getMetaData();
			int cCnt = rsmd.getColumnCount();
			for (int i = 1; i <= cCnt; i++) {
				System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			while(resultSet.next()) {
				for (int i = 1; i <= cCnt; i++) {
					System.out.print(resultSet.getString(i) + "\t");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//1.DVD 등록(insertDVD-info)
	public String[] insertedDVD_Info() {
		String insertedDVD[] = new String[3];
		System.out.println();
		System.out.print("DVD 제목  >> ");
		insertedDVD[0]=sc.nextLine();
		System.out.print("관람 등급(0, 7, 12, 15, 19)  >> ");
		insertedDVD[1]=sc.nextLine();
		System.out.print("장르 (공포, 드라마, 로맨스, 모험, 범죄, 스릴러, 애니메이션, 액션, 코미디, 판타지) >> ");
		insertedDVD[2]=sc.nextLine();
		return insertedDVD;
	}
	
	//2.DVD 정보 수정(updatedDVD_Info)
	public String[] updatedDVD_Info() {
		String updatedDVD[] = new String[4];
		updatedDVD[0] = "dvd_name='";
		updatedDVD[1] = "rating=";
		updatedDVD[2] = "genre='";
		System.out.println();
		System.out.print("수정 할 DVD 번호 >>");
		updatedDVD[3] = sc.nextLine();
		System.out.println("업데이트 하지 않을 항목은 -을 입력해 주세요.");
		System.out.print("새 이름 >>");
		updatedDVD[0] = updatedDVD[0] + sc.nextLine() + "'";
		System.out.print("새 관람 등급 >>");
		updatedDVD[1] += sc.nextLine();
		System.out.print("새 장르 >>");
		updatedDVD[2] = updatedDVD[2] + sc.nextLine() + "'";
		return updatedDVD;
	}
	
	//3.DVD 삭제(deletedDVD_no, deleteDVDResult)
	public String deletedDVD_no() {
		System.out.println();
		System.out.print("삭제할 DVD 번호 >> ");
		return sc.nextLine();
	}
	
	public void deleteDVDResult(int result) {
		if(result == 0)
			System.out.println("잘못된 번호입니다.");
		else if(result == 1)
			System.out.println("삭제 완료");
		else
			System.out.println("대여 중인 DVD는 삭제 할 수 없습니다.");
	}
	
	//4.DVD 목록(DVDList, choice)
	public void DVDList(ResultSet resultSet) {
		try {
			System.out.printf("DVD번호\t%-14s\t대출여부\t관람등급\t장르\t반납예정일\n","DVD이름");
			while(resultSet.next()) {
				System.out.printf("%s\t%-14s\t%s\t%d\t%s\t%s\n",
						  resultSet.getString(1),
						  resultSet.getString(2),
						  resultSet.getString(3),
						  resultSet.getInt(4),
						  resultSet.getString(5),
						  resultSet.getString(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	public String choice() {
		String sel= null;
		System.out.println();
		System.out.println("1.이름순 검색");
		System.out.println("2.장르별 검색");
		sel = sc.nextLine();
		while(!(sel.equals("1")||sel.equals("2"))) {
			System.out.print("잘못된 입력입니다. 다시 입력해주세요. >> ");
			sel = sc.nextLine();
		}
		return sel;
	}
	
	//5.DVD 검색(searchDVD, 4번의DVDList 공유)
	public String searchDVD() {
		System.out.print("검색어 입력 >> ");
		return sc.nextLine();
	}
	
	//1.회원 등록(insertMemberInfo)
	public String[] insertedMemberInfo() {
		String insertedMember[] = new String[5];
		System.out.println();
		System.out.print("회원 이름  >> ");
		insertedMember[0]=sc.nextLine();
		System.out.print("성별(M, F) >> ");
		insertedMember[1]=sc.nextLine();
		System.out.print("생년 (YYYY) >> ");
		insertedMember[2]=sc.nextLine();
		System.out.print("전화번호 >> ");
		insertedMember[3]=sc.nextLine();
		System.out.print("주소 >> ");
		insertedMember[4]=sc.nextLine();
		return insertedMember;
	}
	
	//2.회원 정보 수정(updatedMemberInfo)
	public String[] updatedMemberInfo() {
		String updatedMember[] = new String[6];
		updatedMember[0] = "member_name='";
		updatedMember[1] = "member_sex='";
		updatedMember[2] = "member_birth='";
		updatedMember[3] = "member_phone='";
		updatedMember[4] = "member_addr='";
		System.out.println();
		System.out.print("수정 할 회원 번호 >>");
		updatedMember[5] = sc.nextLine();
		System.out.println("업데이트 하지 않을 항목은 -을 입력해 주세요.");
		System.out.print("새 이름 >>");
		updatedMember[0] = updatedMember[0] + sc.nextLine() + "'";
		System.out.print("새 성별 >>");
		updatedMember[1] = updatedMember[1] + sc.nextLine() + "'";
		System.out.print("새 생년 >>");
		updatedMember[2] = updatedMember[2] + sc.nextLine() + "'";
		System.out.print("새 전화번호 >>");
		updatedMember[3] = updatedMember[3] + sc.nextLine() + "'";
		System.out.print("새 주소 >>");
		updatedMember[4] = updatedMember[4] + sc.nextLine() + "'";
		return updatedMember;
	}
	
	//3.DVD 삭제(deletedMemberID, deleteMemberResult)
	public String deletedMemberID() {
		System.out.println();
		System.out.print("삭제할 회원 번호 >> ");
		return sc.nextLine();
	}
	
	public void deleteMemberResult(int result) {
		if(result == 0)
			System.out.println("해당 회원이 없습니다.");
		else if(result == 1)
			System.out.println("자료를 대여 중인 회원은 삭제 할 수 없습니다.");
		else
			System.out.println("삭제 완료");
	}
	
	//4.DVD 목록(DVDList)
	public void memberList(ResultSet resultSet) {
		try {
			System.out.printf("회원번호\t이름\t성별\t생년\t%-14s\t주소\n","전화번호");
			while(resultSet.next()) {
				System.out.printf("%s\t%s\t%s\t%d\t%-14s\t%s\n",
						  		  resultSet.getString(1),
						  		  resultSet.getString(2),
						  		  resultSet.getString(3),
						  		  resultSet.getInt(4),
						  		  resultSet.getString(5),
						  		  resultSet.getString(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	//5.회원별 대출 자료 검색(memberRentalList, memberID)
	public void memberRentalList(ResultSet resultSet) {
		//rental.dvd_no, dvd_name, rental_date, return_date
		try {
			System.out.printf("DVD번호\t%-14s\t%-14s\t반납예정일\n","DVD이름","대여일");
			while(resultSet.next()) {
				System.out.printf("%d\t%-14s\t%-14s\t%s\n",
						  resultSet.getInt(1),
						  resultSet.getString(2),
						  resultSet.getString(3),
						  resultSet.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	public String memberID() {
		String id = null;
		System.out.print("회원번호 입력 >> ");
		id = sc.nextLine();
		System.out.println(id + "님의 대여 목록");
		return id;
	}
	
	//1.대여(rentalInfo, rentalResult)
	public String[] rentalInfo() {
		String info[] = new String[2];
		System.out.print("회원 번호 입력 >> ");
		info[0] = sc.nextLine();
		System.out.print("DVD 번호 입력 >> ");
		info[1] = sc.nextLine();
		return info;
	}
	
	public void rentalResult(int code) {
		if(code == 0)
			System.out.println("해당 DVD번호가 없습니다.");
		else if(code == 1)
			System.out.println("이미 대여중인 DVD입니다.");
		else if(code == 2)
			System.out.println("해당 회원번호가 없습니다.");
		else if(code == 3)
			System.out.println("연체중인 DVD가 있습니다.");
		else if(code == 4)
			System.out.println("이미 3개를 대여중입니다.");
		else if(code == 5)
			System.out.println("관람 등급보다 나이가 적습니다.");
		else
			System.out.println("대여가 완료되었습니다.");
	}
	
	//2.반납(rentalInfo, rentalResult)
	public String returnInfo() {
		System.out.print("DVD 번호 입력 >> ");
		return sc.nextLine();
	}
	
	public void returnResult(int code) {
		if(code == 0)
			System.out.println("해당 DVD번호가 없습니다.");
		else if(code == 1)
			System.out.println("대여 중인 DVD가 아닙니다");
		else 
			System.out.println("반납이 완료되었습니다.");
	}
	
	//3.연체 DVD 목록
	public void overduedDVDList(ResultSet resultSet) {
		//rental.dvd_no, dvd_name, rental.member_id, member_name, member_phone, return_date
		try {
			System.out.printf("DVD번호\t%-14s\t회원번호\t회원이름\t%-14s\t반납예정일\n","DVD이름","전화번호");
			while(resultSet.next()) {
				System.out.printf("%s\t%-14s\t%s\t%s\t%s\t%s\n",
						  resultSet.getString(1),
						  resultSet.getString(2),
						  resultSet.getString(3),
						  resultSet.getString(4),
						  resultSet.getString(5),
						  resultSet.getString(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}