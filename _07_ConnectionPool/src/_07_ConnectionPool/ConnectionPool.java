package _07_ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
/*
사전에 미리 Connection객체들을 생성해놓고
사용자(Client App)이 요청하면
레퍼런스를 전달하고
다 사용했으면 다시 반납받는
Connection객체를 관리하는 클래스
*/
public class ConnectionPool {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	ArrayList<Connection> free;	// 미사용인 Connection 저장
	ArrayList<Connection> used; // 사용중인 Connection 저장
	String url; 				// Oracle 서버 접속 주소
	String user;				// id
	String password;			// password
	int initialCons = 0;		// 초기 커넥션 수
	int maxCons = 0;			// 최대 커넥션 수
	int numCons = 0;			// 현재 커넥션 수
	
	/*
	Singleton 패턴
	데이터베이스 접근처럼 일원적으로 관리되어야 할 업무는
	여러군데서 접근 객체를 만들도록 허락하지 않고 단 1개의 객체만
	만들도록 강요하고 이 1개의 객체로만 사용할 것을 강요받는 
	프로그래밍 방식이다.
	1) static으로 클래스 변수를 선언한다
	2) 생성자를 private으로 선언한다
       (외부에서 new 이렇게 객체를 만들 수 없다)
    3) 일반적으로 클래스 객체 생성하여 리턴하는 메서드는
       getInstance()라 명명한다
    4) 외부 클래스에서 이 클래스 객체를 요청할 때는 반드시
	   getInstance()메서드만을 이용해서 객체를 얻을 수 있다
	5) 아무리 많은 스레드에서 요청을 해도 반환하는 객체는
           유일하고 동일한 객체이다.
    6) 객체 사용 후 반납은 releaseInstance()라 명명한다
	*/
	
	// ConnectionPool 객체를 단 1개 만드는 메서드
	static ConnectionPool cp = null;
	public synchronized static ConnectionPool getInstance(
							String url, String user,
							String password, int initialCons,
							int maxCons) {
		try {
			// 생성된 객체가 없다면 객체를 생성하라
			if(cp==null) {
//				동시에 여러 클라이언트들/여러 스레드들이 
//				ConnectionPool객체 요청을 할 수 있으므로
				// static 메서드에서 동기화하는 방법
				//synchronized(ConnectionPool.class) {
					cp = new ConnectionPool(url, user,
							password, initialCons, maxCons);
				//}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return cp;
	}
	
//	생성자를 private 주면 외부에서 new ConnectionPool()를
//	호출할 수가 없다. 왜냐하면 new ConnectionPool() 할 때
//	생성자 메서드가 호출되야 하는데 private이니까 아예 외부에서는
//	객체 생성을 못하게 막을 수 있다.
//	이 때는 무조건 ConnectPool 클래스 내에서 객체를 생성해야 한다
	private ConnectionPool(String url, String user,
						String password, int initialCons,
						int maxCons) throws SQLException {
		this.url = url;
		this.user = user;
		this.password = password;
		this.initialCons = initialCons;	// 최초 Connection생성 개수
		this.maxCons = maxCons;			// 최대 Connection생성 개수
		
		if(initialCons < 0)	// -1을 주면 디폴트 설정을 해라
			this.initialCons = 5;
		if(maxCons < 0)	// -1을 주면 디폴트 설정을 해라
			this.maxCons = 10;
		
//		초기 Connection 개수를 각각의 ArrayList에 저장할 수 있도록
//		초기 Connection 개수만큼 ArrayList의 내부공간을 설정한다.
		free = new ArrayList<Connection>(this.initialCons);
		used = new ArrayList<Connection>(this.initialCons);
		
		// 초기 Connection개수만큼 Connection객체를 생성하자
		while(this.numCons < this.initialCons)
			addConnection();	// 1개씩 객체를 추가하는 메서드
	}
	
	private void addConnection() throws SQLException {
		free.add(getNewConnection());
	}
	
	// 호출될 때마다 Connection객체를 생성하여 리턴한다
	// this.numCons : 연결 객체수를 1씩 증가한다
	private Connection getNewConnection() throws SQLException {
		Connection con = null;
		con = DriverManager.getConnection(
				this.url, this.user, this.password);
		System.out.println("About to connect to " + con);
		this.numCons++;
		return con;
	}
	
	// 여러 Client가 접속하는 메서드는 동기화 처리	
	// 외부 Client에서 Oracle 연결 객체를 요구할 때 사용하는 메서드
	public synchronized Connection getConnection() throws SQLException {
		// 여유분이 없다면 최대 Connection 개수만큼 추가 생성하라
		if(free.isEmpty()) {
			while(this.numCons < this.maxCons) {
				addConnection();
			}
		}
//		free에서 1개 꺼내서 used로 옮기고
//		사용자한테 객체의 레퍼런스를 반환한다
		Connection _con = free.get(free.size()-1);
		free.remove(_con);
		used.add(_con);
		return _con;
	}
	
	// 여러 Client가 접속하는 메서드는 동기화 처리
	// 다 사용한 Oracle Connection 객체를 반납하는 메서드
	public synchronized void releaseConnection(Connection _con) {
		try {
			//user에 포함되어있는지?
			if(used.contains(_con)) {
				//used -> free
				used.remove(_con);
				free.add(_con);
			}else {
				throw new SQLException("ConnectionPool : " + _con
						+ "used에 있지 않습니다");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 서비스 종료시 ConnectionPool을 그만 사용한다
	// 모든 Connection을 종료한다
	public void closeAll() {
		// 사용중인 모든 Connection객체를 닫고 used에서 제거한다
		for(int i=0;i<used.size();i++) {
			Connection _con = used.get(i);
			try {
				used.remove(i--);
				_con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		// 미사용중인 모든 Connection객체를 닫고 free에서 제거한다
		for(int i=0;i<free.size();i++) {
			Connection _con = free.get(i);
			try {
				free.remove(i--);
				_con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 현재 Connection 객체 최대 연결 개수
	public int getMaxCons() {
		return this.maxCons;
	}
	
	// 현재 할당된 Connection 객체 연결 개수
	public int getNumCons() {
		return this.numCons;
	}
	
	// Connection 객체의 최대치를 더 늘리고 싶을 때 
	// 숫자를 증가시키면 Connection 객체가 필요하면 더 늘어난다
	public void setMaxCons(int num) {
		this.maxCons += num;
	}
}











