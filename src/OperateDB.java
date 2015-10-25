import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class OperateDB {
	private Connection connect = null;
	String dbName = "TwittInfo";
	String userName = Credentials.DBname;
	String password = Credentials.DBpassword;
	String hostname = Credentials.DBHostName;
	String port = "3306";
	String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
	
	public boolean isConnected() {
		return connect != null;
	}
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection(jdbcUrl);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			Common.writeLog("Error", e);
		}
	}

	public void addRowToTweetTable(TweetData tweetData) throws Exception {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connect.prepareStatement("INSERT INTO FilteredTweetData "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setLong(1, tweetData.statusId);
			preparedStatement.setLong(2, tweetData.userId);
			preparedStatement.setString(3, tweetData.screenName);
			preparedStatement.setString(4, tweetData.content);
			preparedStatement.setDouble(5, tweetData.longitude);
			preparedStatement.setDouble(6, tweetData.latitude);
			preparedStatement.setTimestamp(7, new java.sql.Timestamp(tweetData.createDate.getTime()));
			preparedStatement.setString(8, tweetData.keyword);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			Common.writeLog("SQL Error", e);
		}
	}
	
	public List<WebData> getDataByMinutes(int minutes, String keyword) {
		if (minutes < 0) {
			return getDataByDate(true, null, keyword);
		}
		Timestamp ts = new Timestamp(new Date().getTime());
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		cal.setTime(ts);
		cal.add(Calendar.MINUTE, -1 * minutes);
		ts.setTime(cal.getTime().getTime());
		return getDataByDate(false, ts, keyword);
	}

	private List<WebData> getDataByDate(boolean isAll, Timestamp startDate, String keyword) {
		if (connect == null) {
			this.connect();
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<WebData> returnData = new ArrayList<WebData>();
		try {
			if (keyword.equals("all")) {
				if (isAll) {
					preparedStatement = connect.prepareStatement("SELECT statusId, latitude, longitude, content, keyword FROM FilteredTweetData");
				} else {
					preparedStatement = connect.prepareStatement("SELECT statusId, latitude, longitude, content, keyword FROM FilteredTweetData WHERE createDate > ?");
					preparedStatement.setTimestamp(1, startDate);
				}
				resultSet = preparedStatement.executeQuery();
			} else {
				if (isAll) {
					preparedStatement = connect.prepareStatement("SELECT statusId, latitude, longitude, content, keyword FROM FilteredTweetData WHERE keyword = ?");
					preparedStatement.setString(1, keyword);
				} else {
					preparedStatement = connect.prepareStatement("SELECT statusId, latitude, longitude, content, keyword FROM FilteredTweetData WHERE createDate > ? AND keyword = ?");
					preparedStatement.setTimestamp(1, startDate);
					preparedStatement.setString(2, keyword);
				}
				resultSet = preparedStatement.executeQuery();
			}
			while (resultSet.next()) {
				Long statusId = resultSet.getLong(1);
				Double latitude = resultSet.getDouble(2);
				Double longitude = resultSet.getDouble(3);
				String content = resultSet.getString(4);
				String key = resultSet.getString(5);
				WebData result = new WebData(statusId, latitude, longitude, content, key);
				returnData.add(result);
			}
		} catch (SQLException e) {
			Common.writeLog("SQL Error", e);
		}
		return returnData;
	}
	
	public void autoDelete() {
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connect.prepareStatement("DELETE FROM FilteredTweetData ORDER BY statusId LIMIT 100");
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			Common.writeLog("SQL Error, Unable to auto delete", e);
		}
	}
	
	public int getEntriesCount() {
		int res = 0;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connect.prepareStatement("SELECT COUNT(1) FROM FilteredTweetData");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			res = resultSet.getInt(1);
		} catch (SQLException e) {
			Common.writeLog("SQL Error", e);
		}
		return res;
	}
	
	public void close() {
		try {
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {
			Common.writeLog("Error in closing the database", e);
		}
	}
}
