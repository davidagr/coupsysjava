package il.co.David.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;






public class ConnectionPool
{

	private static ConnectionPool instance = null;
	public static List<Connection> connections = new ArrayList<>();
	private final int CONNECTION_MAX = 10;
	public static Object someObject = new Object();

	private ConnectionPool() {
		for (int i = 0; i < CONNECTION_MAX; i++) {
			connections.add(addConnection());
		}
	}

	public static ConnectionPool getInstance() {
		synchronized (someObject) {
			if (instance == null) {

				instance = new ConnectionPool();
			}
		}

		return instance;
	}

	public Connection addConnection() {

		String url = "jdbc:sqlserver://localhost:52967;databaseName=CouponSystem;integratedSecurity=true";
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	public synchronized Connection getConnection() throws InterruptedException,CouponSystemException {
		while (!checkIfConnectionPoolIsEmpty()) {
			this.wait();
		}
		int lastElement = connections.size() - 1;
		Connection conn = connections.get(lastElement);
		connections.remove(lastElement);
		return conn;
	}

	private synchronized boolean checkIfConnectionPoolIsEmpty() {
		if (connections.size() == 0) {
			return false;
		}
		return true;
	}

	public synchronized void returnConnection(Connection connection) {
		if (connections.size() < CONNECTION_MAX) {
			connections.add(connection);
		}
	}

	public static void closeAllConnections() throws CouponSystemException {

		for (int i = 0; i < connections.size(); i++) {
			try {
				if (connections.get(i) != null) {
					connections.get(i).close();
				}
			} catch (Exception e) {
				throw new CouponSystemException( "Failed to shut down");
			}

		}

	}

}
