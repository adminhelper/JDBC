import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DVDManageSystem {
	static DVDViewer menu = new DVDViewer();
	static ResultSet resultSet = null;
	static DVD_DB db = null;
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void start() {
		String sel = null;
		while(true) {
			sel = menu.startMenu();
			switch (sel) {
			case "1":
				DVDManager();
				break;
			case "2":
				memberManager();
				break;
			case "3":
				rentalManager();
				break;
			case "4":
				System.exit(0);
				break;
			}
		}
	}
	
	public static void DVDManager() {
		String sel = null;
//		System.out.println("1.DVD 등록");
//		System.out.println("2.DVD 정보 수정");
//		System.out.println("3.DVD 삭제");
//		System.out.println("4.DVD 목록");
//		System.out.println("5.DVD 검색");
//		System.out.println("6.이전 메뉴");
		while(true) {
			sel = menu.DVDMenu();
			switch (sel) {
			case "1":
				db.insertDVD(menu.insertedDVD_Info());
				break;
			case "2":db.updateDVD(menu.updatedDVD_Info());
				break;
			case "3":
				menu.deleteDVDResult(db.deleteDVD(menu.deletedDVD_no()));
				break;
			case "4":
				menu.DVDList(db.DVDList(menu.choice()));
				break;
			case "5":
				menu.DVDList(db.searchDVD(menu.searchDVD()));
				break;
			case "6":
				return;
			}
		}
	}
	
	public static void memberManager() {
//		System.out.println("1.회원 등록");
//		System.out.println("2.회원 정보 수정");
//		System.out.println("3.회원 삭제");
//		System.out.println("4.회원 목록");
//		System.out.println("5.회원 DVD 대여 목록 검색");
//		System.out.println("6.이전 메뉴");
		String sel = null;
		while(true) {
			sel = menu.memberMenu();
			switch (sel) {
			case "1":
				db.insertMember(menu.insertedMemberInfo());
				break;
			case "2":db.updateMember(menu.updatedMemberInfo());
				break;
			case "3":menu.deleteMemberResult(db.deleteMember(menu.deletedMemberID()));
				break;
			case "4":menu.memberList(db.memberList());
				break;
			case "5":menu.memberRentalList(db.memberRentalList(menu.memberID()));
				break;
			case "6":
				return;
			}
		}
	}
	
	public static void rentalManager() {
//		System.out.println("1.대여");
//		System.out.println("2.반납");
//		System.out.println("3.연체 DVD 목록");
//		System.out.println("4.이전 메뉴");
		String sel = null;
		while(true) {
			sel = menu.rentalMenu();
			switch (sel) {
			case "1":menu.rentalResult(db.rentDVD(menu.rentalInfo()));
				break;
			case "2":menu.returnResult(db.returnDVD(menu.returnInfo()));
				break;
			case "3":menu.overduedDVDList(db.overduedDVDList());
				break;
			case "4":
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","DVDrent", "DVDrent");
			db = new DVD_DB(connection);
			
			start();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
