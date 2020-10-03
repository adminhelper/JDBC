import java.util.Scanner;

class Menu {
	final static int JOIN = 1;
	final static int LOGIN = 2;
	final static int IDPW = 3;
	final static int CHARGE=4;
	final static int BYE=5;
}
class Menu2 {
	final static int ALLSEARCH = 1;
	final static int SEARCH = 2;
	final static int SEAT = 3;
	final static int ORDER = 4;
	final static int INVENTORY =5;
	final static int ALBA = 6;
	final static int STOREINFO = 7;
	final static int EXIT = 8;
	
	
}

class Menu3 {
	final static int MYINFO = 1;
	final static int MYINFOUP = 2;
	final static int SEATCHANGE = 3;
	final static int ORDERFOOD = 4;
	final static int CCHARGE=5;
	final static int BYEBYE = 6;
}


public class View {
	public void logo1() {
	System.out.println("===================================================================== ");
	System.out.println();
	System.out.println("  ████            ████         ██████████████          █████████████ ");
	System.out.println(" ██░░██████████████░░██        ██░░░░░░░░░░██         ██░░░░░░░░░░██ ");
	System.out.println(" ██░░░░░░░░░░░░░░░░░░██        ██░░██████░░██        ██░░███████████ ");
	System.out.println(" ██░░██████░░██████░░██        ██░░██  ██░░██        ██░░██          ");
	System.out.println(" ██░░██  ██░░██  ██░░██        ██░░██████░░██       ██░░██           ");
	System.out.println(" ██░░██  ██░░██  ██░░██        ██░░░░░░░░░░██       ██░░██           ");
	System.out.println(" ██░░██  ██████  ██░░██        ██░░██████████       ██░░██           ");
	System.out.println(" ██░░██    ██    ██░░██        ██░░██               ██░░██           ");
	System.out.println(" ██░░██          ██░░██        ██░░██                ██░░██          ");
	System.out.println(" ██░░██          ██░░██ ██████ ██░░██         ██████ ██░░███████████ ");
	System.out.println(" ██░░██          ██░░██ ██░░██ ██░░██         ██░░██  ██░░░░░░░░░░██ ");
	System.out.println(" ██████          ██████ ██████ ██████         ██████   █████████████ ");
	System.out.println();
	System.out.println("                                                          ★ m.p.c 피시방에 어서오세요 ★                                                      ");
	System.out.println("=====================================================================");
	}
	public void logo2(){
	System.out.println("===================================================================== ");
	System.out.println();
	System.out.println("  ████            ████         ██████████████          █████████████ ");
	System.out.println(" ██░░██████████████░░██        ██░░░░░░░░░░██         ██░░░░░░░░░░██ ");
	System.out.println(" ██░░░░░░░░░░░░░░░░░░██        ██░░██████░░██        ██░░███████████ ");
	System.out.println(" ██░░██████░░██████░░██        ██░░██  ██░░██        ██░░██          ");
	System.out.println(" ██░░██  ██░░██  ██░░██        ██░░██████░░██       ██░░██           ");
	System.out.println(" ██░░██  ██░░██  ██░░██        ██░░░░░░░░░░██       ██░░██           ");
	System.out.println(" ██░░██  ██████  ██░░██        ██░░██████████       ██░░██           ");
	System.out.println(" ██░░██    ██    ██░░██        ██░░██               ██░░██           ");
	System.out.println(" ██░░██          ██░░██        ██░░██                ██░░██          ");
	System.out.println(" ██░░██          ██░░██ ██████ ██░░██         ██████ ██░░███████████ ");
	System.out.println(" ██░░██          ██░░██ ██░░██ ██░░██         ██░░██  ██░░░░░░░░░░██ ");
	System.out.println(" ██████          ██████ ██████ ██████         ██████   █████████████ ");
	System.out.println();
	System.out.println("                                                               ★ 관리자 계정 로그인 ★                                                      ");
	System.out.println("===================================================================== ");
	}
	public void logo3() {
		System.out.println("===================================================================== ");
		System.out.println();
		System.out.println("  ████            ████         ██████████████          █████████████ ");
		System.out.println(" ██░░██████████████░░██        ██░░░░░░░░░░██         ██░░░░░░░░░░██ ");
		System.out.println(" ██░░░░░░░░░░░░░░░░░░██        ██░░██████░░██        ██░░███████████ ");
		System.out.println(" ██░░██████░░██████░░██        ██░░██  ██░░██        ██░░██          ");
		System.out.println(" ██░░██  ██░░██  ██░░██        ██░░██████░░██       ██░░██           ");
		System.out.println(" ██░░██  ██░░██  ██░░██        ██░░░░░░░░░░██       ██░░██           ");
		System.out.println(" ██░░██  ██████  ██░░██        ██░░██████████       ██░░██           ");
		System.out.println(" ██░░██    ██    ██░░██        ██░░██               ██░░██           ");
		System.out.println(" ██░░██          ██░░██        ██░░██                ██░░██          ");
		System.out.println(" ██░░██          ██░░██ ██████ ██░░██         ██████ ██░░███████████ ");
		System.out.println(" ██░░██          ██░░██ ██░░██ ██░░██         ██░░██  ██░░░░░░░░░░██ ");
		System.out.println(" ██████          ██████ ██████ ██████         ██████   █████████████ ");
		System.out.println();
		System.out.println("                                                          ★ 회원 계정 로그인 ★                                                      ");
		System.out.println("===================================================================== ");
		
	}
	static Scanner sc = new Scanner(System.in);
	public void menu1() {
		System.out.println("1.회원가입		2.로그인		3.PW찾기 		4.시간 충전 	5.나가기");
	}
	public void menu2() {
		System.out.println("1.회원 전체 조회		2.회원 검색 조회 		3.자리 현황 	  4.주문 정보 조회");
		System.out.println("5.재고 확인 	  	6.직원 정보 조회		7.사업장 정보 조회 	  8.나가기");
		
	}
	public void menu3() {
		System.out.println("1.내 정보 확인			2.내 정보 수정			3.자리 이동");
		System.out.println("4. 먹거리 주문			5.시간 충전 			6.나가기");
	}

	public boolean mainMenu(int choice) {
		Join jo=new Join();
		boolean bool;
		switch (choice) {
		case Menu.JOIN:
			System.out.println("회원가입 ");
			bool= jo.join();
			return bool;
		case Menu.LOGIN:
			System.out.println("로그인");
			bool= jo.Login();
			return bool;
		case Menu.IDPW:
			System.out.println("pw 찾기");
			bool=jo.Idpw();
			return bool;
		case Menu.CHARGE:
			System.out.println("시간 충전");
			bool=jo.charge();
			return bool;
		case Menu.BYE:
			System.out.println("안녕히 가세요");
			return false;
		default:
			System.out.println("메뉴에있는 숫자를 입력해주세요");
			break;
		}

		return true;
	}

	public void loop1() {
		boolean bool = true;
		int choice;

		while (bool) {
			logo1();
			menu1();
			try {
				System.out.print("입력 >>");
				choice = sc.nextInt();
				bool = mainMenu(choice);//public boolean customer(int choice)
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("숫자를 입력해주세요");
				continue;
			}
		}

	}
	
	public boolean AdminView(int choice) {
		Admin ad=new Admin();
		boolean bool;
		switch (choice) {
		case Menu2.ALLSEARCH:
			System.out.println("전체 회원 조회 ");
			bool= ad.searchAll();
			return bool;
		case Menu2.SEARCH:
			System.out.println("회원 검색 조회");
			bool= ad.searchMember();
			return bool;
		case Menu2.SEAT:
			System.out.println("자리 정보");
			bool=ad.seatInfo();
			return bool;
		case Menu2.ORDER:
			System.out.println("주문 정보 조회");
			bool=ad.orderInfo();
			return bool;
		case Menu2.INVENTORY:
			System.out.println("재고 확인");
			bool=ad.invenInfo();
			return bool;
		case Menu2.ALBA:
			System.out.println("직원 정보 조회");
			bool=ad.employeeInfo();
			return bool;
		case Menu2.STOREINFO:
			System.out.println("사업장 정보 조회");
			bool=ad.storeInfo();
			return bool;
		case Menu2.EXIT:
			System.out.println("나가기");
			return false;
		
		default:
			System.out.println("메뉴에있는 숫자를 입력해주세요");
			break;
		}

		return true;
	}
	
	public void loop2() {
		boolean bool = true;
		int choice;
		
		while (bool) {
			logo2();
			menu2();
			try {
				System.out.print("입력 >>");
				choice = sc.nextInt();
				bool = AdminView(choice);//public boolean customer(int choice)
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("숫자를 입력해주세요");
				continue;
			}
		}	
	}
	public boolean customerView(int choice,String Id,int[] seat,int a) {
		Customer cm = new Customer();
		boolean bool;
		switch (choice) {
		case Menu3.MYINFO:
			System.out.println("내 정보 보기");
			bool=cm.myInfo(Id);
			return bool;
			
		case Menu3.MYINFOUP:
			System.out.println("내 정보 수정");
			bool=cm.myInfoUp(Id);
			return bool;
		
			
		case Menu3.SEATCHANGE:
			System.out.println("자리 변경");
			bool=cm.seatMove(Id,seat);
			return bool;
			
		case Menu3.ORDERFOOD:
			System.out.println("");
			bool=cm.orderFood(Id,seat,a);
			return bool;
		case Menu3.CCHARGE:
			System.out.println("");
			bool=cm.ccharge(Id);
			return bool;
		case Menu3.BYEBYE:
			System.out.println("");
			bool=cm.logOut(Id,seat[0]);
			return bool;
			
		default:
			System.out.println("메뉴에 있는 숫자를 입력해주세요");
			break;
		}
		return true;
	}
	public void loop3(String Id,int seat) {
		boolean bool = true;
		int choice;
		int a=0;
		int[] nseat=new int[1];
		nseat[0]=seat;
		while (bool) {
			logo3();
			menu3();
			try {
				System.out.print("입력 >>");
				choice = sc.nextInt();
				bool = customerView(choice,Id,nseat,++a);//public boolean customer(int choice)
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("숫자를 입력해주세요");
				continue;
			}
		}	
	}
	
	

}
