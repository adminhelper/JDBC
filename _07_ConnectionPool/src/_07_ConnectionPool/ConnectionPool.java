package _07_ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
/*
������ �̸� Connection��ü���� �����س���
�����(Client App)�� ��û�ϸ�
���۷����� �����ϰ�
�� ��������� �ٽ� �ݳ��޴�
Connection��ü�� �����ϴ� Ŭ����
*/
public class ConnectionPool {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	ArrayList<Connection> free;	// �̻���� Connection ����
	ArrayList<Connection> used; // ������� Connection ����
	String url; 				// Oracle ���� ���� �ּ�
	String user;				// id
	String password;			// password
	int initialCons = 0;		// �ʱ� Ŀ�ؼ� ��
	int maxCons = 0;			// �ִ� Ŀ�ؼ� ��
	int numCons = 0;			// ���� Ŀ�ؼ� ��
	
	/*
	Singleton ����
	�����ͺ��̽� ����ó�� �Ͽ������� �����Ǿ�� �� ������
	���������� ���� ��ü�� ���鵵�� ������� �ʰ� �� 1���� ��ü��
	���鵵�� �����ϰ� �� 1���� ��ü�θ� ����� ���� ����޴� 
	���α׷��� ����̴�.
	1) static���� Ŭ���� ������ �����Ѵ�
	2) �����ڸ� private���� �����Ѵ�
       (�ܺο��� new �̷��� ��ü�� ���� �� ����)
    3) �Ϲ������� Ŭ���� ��ü �����Ͽ� �����ϴ� �޼����
       getInstance()�� �����Ѵ�
    4) �ܺ� Ŭ�������� �� Ŭ���� ��ü�� ��û�� ���� �ݵ��
	   getInstance()�޼��常�� �̿��ؼ� ��ü�� ���� �� �ִ�
	5) �ƹ��� ���� �����忡�� ��û�� �ص� ��ȯ�ϴ� ��ü��
           �����ϰ� ������ ��ü�̴�.
    6) ��ü ��� �� �ݳ��� releaseInstance()�� �����Ѵ�
	*/
	
	// ConnectionPool ��ü�� �� 1�� ����� �޼���
	static ConnectionPool cp = null;
	public synchronized static ConnectionPool getInstance(
							String url, String user,
							String password, int initialCons,
							int maxCons) {
		try {
			// ������ ��ü�� ���ٸ� ��ü�� �����϶�
			if(cp==null) {
//				���ÿ� ���� Ŭ���̾�Ʈ��/���� ��������� 
//				ConnectionPool��ü ��û�� �� �� �����Ƿ�
				// static �޼��忡�� ����ȭ�ϴ� ���
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
	
//	�����ڸ� private �ָ� �ܺο��� new ConnectionPool()��
//	ȣ���� ���� ����. �ֳ��ϸ� new ConnectionPool() �� ��
//	������ �޼��尡 ȣ��Ǿ� �ϴµ� private�̴ϱ� �ƿ� �ܺο�����
//	��ü ������ ���ϰ� ���� �� �ִ�.
//	�� ���� ������ ConnectPool Ŭ���� ������ ��ü�� �����ؾ� �Ѵ�
	private ConnectionPool(String url, String user,
						String password, int initialCons,
						int maxCons) throws SQLException {
		this.url = url;
		this.user = user;
		this.password = password;
		this.initialCons = initialCons;	// ���� Connection���� ����
		this.maxCons = maxCons;			// �ִ� Connection���� ����
		
		if(initialCons < 0)	// -1�� �ָ� ����Ʈ ������ �ض�
			this.initialCons = 5;
		if(maxCons < 0)	// -1�� �ָ� ����Ʈ ������ �ض�
			this.maxCons = 10;
		
//		�ʱ� Connection ������ ������ ArrayList�� ������ �� �ֵ���
//		�ʱ� Connection ������ŭ ArrayList�� ���ΰ����� �����Ѵ�.
		free = new ArrayList<Connection>(this.initialCons);
		used = new ArrayList<Connection>(this.initialCons);
		
		// �ʱ� Connection������ŭ Connection��ü�� ��������
		while(this.numCons < this.initialCons)
			addConnection();	// 1���� ��ü�� �߰��ϴ� �޼���
	}
	
	private void addConnection() throws SQLException {
		free.add(getNewConnection());
	}
	
	// ȣ��� ������ Connection��ü�� �����Ͽ� �����Ѵ�
	// this.numCons : ���� ��ü���� 1�� �����Ѵ�
	private Connection getNewConnection() throws SQLException {
		Connection con = null;
		con = DriverManager.getConnection(
				this.url, this.user, this.password);
		System.out.println("About to connect to " + con);
		this.numCons++;
		return con;
	}
	
	// ���� Client�� �����ϴ� �޼���� ����ȭ ó��	
	// �ܺ� Client���� Oracle ���� ��ü�� �䱸�� �� ����ϴ� �޼���
	public synchronized Connection getConnection() throws SQLException {
		// �������� ���ٸ� �ִ� Connection ������ŭ �߰� �����϶�
		if(free.isEmpty()) {
			while(this.numCons < this.maxCons) {
				addConnection();
			}
		}
//		free���� 1�� ������ used�� �ű��
//		��������� ��ü�� ���۷����� ��ȯ�Ѵ�
		Connection _con = free.get(free.size()-1);
		free.remove(_con);
		used.add(_con);
		return _con;
	}
	
	// ���� Client�� �����ϴ� �޼���� ����ȭ ó��
	// �� ����� Oracle Connection ��ü�� �ݳ��ϴ� �޼���
	public synchronized void releaseConnection(Connection _con) {
		try {
			//user�� ���ԵǾ��ִ���?
			if(used.contains(_con)) {
				//used -> free
				used.remove(_con);
				free.add(_con);
			}else {
				throw new SQLException("ConnectionPool : " + _con
						+ "used�� ���� �ʽ��ϴ�");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ���� ����� ConnectionPool�� �׸� ����Ѵ�
	// ��� Connection�� �����Ѵ�
	public void closeAll() {
		// ������� ��� Connection��ü�� �ݰ� used���� �����Ѵ�
		for(int i=0;i<used.size();i++) {
			Connection _con = used.get(i);
			try {
				used.remove(i--);
				_con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		// �̻������ ��� Connection��ü�� �ݰ� free���� �����Ѵ�
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
	
	// ���� Connection ��ü �ִ� ���� ����
	public int getMaxCons() {
		return this.maxCons;
	}
	
	// ���� �Ҵ�� Connection ��ü ���� ����
	public int getNumCons() {
		return this.numCons;
	}
	
	// Connection ��ü�� �ִ�ġ�� �� �ø��� ���� �� 
	// ���ڸ� ������Ű�� Connection ��ü�� �ʿ��ϸ� �� �þ��
	public void setMaxCons(int num) {
		this.maxCons += num;
	}
}










