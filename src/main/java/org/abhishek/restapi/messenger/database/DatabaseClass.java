package org.abhishek.restapi.messenger.database;

import java.sql.*;
import java.util.*;
import java.util.Date;

import org.abhishek.restapi.messenger.model.*;

public class DatabaseClass {
	private String jdbcURL = "jdbc:mysql://localhost:3306/Messenger";
	private String username = "abhishek";
	private String password = "abc@123";
	private Connection con;

	public DatabaseClass() {
	}

//	public DatabaseClass(String jdbcURL, String username, String password) {
//		super();
//		this.jdbcURL = jdbcURL;
//		this.username = username;
//		this.password = password;
//	}

//	public static Map<Long, Profile> getProfile() {
//	return profiles;
//}

	protected void connect() throws SQLException {
		if (con == null || con.isClosed()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (Exception e) {
				throw new SQLException(e);
			}
			con = DriverManager.getConnection(jdbcURL, username, password);
		}

	}

	protected void disconnect() throws SQLException {
		if (con != null || !con.isClosed()) {
			con.close();
		}
	}

//	CRUD Operations for Messages

	public List<Message> getMessages() {
		List<Message> messages = new ArrayList<Message>();
		String sql = "select * from messages";
		try {
			connect();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long id = rs.getLong("message_id");
				String author = rs.getString("author");
				String message = rs.getString("message");
				Date createdDate = rs.getDate("createddate");
				Message msg = new Message(id, message, author);
				msg.setCreatedDate(createdDate);
				messages.add(msg);
			}
			rs.close();
			stmt.close();
			disconnect();

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return messages;
	}

	public Message getMessageById(long id) {
		String sql = "SELECT * FROM messages WHERE message_id = ?";
		Message msg = null;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				long msgId = rs.getLong("message_id");
				String author = rs.getString("author");
				String message = rs.getString("message");
				Date createdDate = rs.getDate("createddate");
				msg = new Message(msgId, message, author);
				msg.setCreatedDate(createdDate);
			}
			rs.close();
			stmt.close();
			disconnect();

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return msg;

	}

	public Message addMessage(Message msg) {
		String sql = "insert into messages (author,message,createddate) values(?,?,now())";
		boolean rowInserted = false;
		Message message = null;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, msg.getAuthor());
			stmt.setString(2, msg.getMessage());
			rowInserted = stmt.executeUpdate() > 0;

			if (rowInserted) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					long id = rs.getLong(1);
					message = getMessageById(id);
				}
			}
			stmt.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return message;
	}

	public boolean deleteMessage(long messageId) {
		String sql = "DELETE FROM messages where message_id = ?";
		boolean rowDeleted = false;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, messageId);
			rowDeleted = stmt.executeUpdate() > 0;
			stmt.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return rowDeleted;
	}

	public boolean updateMessage(Message message) {
		String sql = "UPDATE messages SET author = ?, message = ? ,createddate = now()";
		sql += "where message_id = ?";
		boolean rowUpdated = false;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, message.getAuthor());
			stmt.setString(2, message.getMessage());
			stmt.setLong(3, message.getId());
			rowUpdated = stmt.executeUpdate() > 0;
			stmt.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return rowUpdated;

	}

//	CRUD Operations for Profiles 

	public List<Profile> getAllProfiles() {
		List<Profile> profiles = new ArrayList<>();
		String sql = "SELECT * FROM profiles";
		try {
			connect();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				String profileName = rs.getString("profilename");
				Profile profile = new Profile(profileName, firstName, lastName);
				profiles.add(profile);
			}
			stmt.close();
			con.close();

		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return profiles;
	}

	public Profile getProfileByName(String profileNamme) {
		String sql = "SELECT * FROM profiles WHERE profilename = ?";
		Profile profile = null;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, profileNamme);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				String profileName = rs.getString("profilename");
				profile = new Profile(profileName, firstName, lastName);
			}
			stmt.close();
			con.close();

		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return profile;
	}

	private Profile getProfileById(Long id) {
		String sql = "SELECT * FROM profiles WHERE profile_id = ?";
		Profile profile = null;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				String profileName = rs.getString("profilename");
				profile = new Profile(profileName, firstName, lastName);
			}
			stmt.close();
			con.close();

		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return profile;
	}

	public Profile addProfile(Profile profile) {
		long id;
		String sql = "INSERT INTO profiles(firstname,lastname,profilename) VALUES (?,?,?)";
		Profile newProfile = null;
		boolean profileInserted = false;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, profile.getFirstName());
			stmt.setString(2, profile.getLastName());
			stmt.setString(3, profile.getProfileName());
			profileInserted = stmt.executeUpdate() > 0;
			if (profileInserted) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					id = rs.getLong(1);
					newProfile = getProfileById(id);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return newProfile;
	}

	public boolean updateProfile(Profile profile) {
		String sql = "UPDATE profiles SET firstname = ?, lastName = ? WHERE profilename = ?";
		boolean profileUpdated = false;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, profile.getFirstName());
			stmt.setString(2, profile.getLastName());
			stmt.setString(3, profile.getProfileName());
			profileUpdated = stmt.executeUpdate() > 0;
			stmt.close();
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
		return profileUpdated;
	}

	public boolean deleteProfile(String profileName) {
		String sql = "DELETE FROM profiles WHERE profilename = ?";
		boolean profileDeleted = false;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, profileName);
			profileDeleted = stmt.executeUpdate() > 0;
			stmt.close();
			con.close();

		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return profileDeleted;
	}

//	CRUD Operations for Comments 

	public List<Comment> getAllComments(long messageId) {
		List<Comment> comments = new ArrayList<>();
		String sql = "SELECT * FROM comments WHERE message_id = ?";
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, messageId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				long id = rs.getLong("comment_id");
				long msg_id = rs.getLong("message_id");
				String msg = rs.getString("comment");
				String author = rs.getString("author");
				Date date = rs.getDate("createddate");

				Comment comment = new Comment(id, msg, author);
				comment.setMessage_id(msg_id);
				comment.setCreatedDate(date);
				comments.add(comment);
			}
			stmt.close();
			con.close();

		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return comments;
	}

	public Comment getCommentById(long messageId, long commentId) {
		String sql = "SELECT * FROM comments WHERE comment_id = ? AND message_id = ?";
		Comment comment = null;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, commentId);
			stmt.setLong(2, messageId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				long id = rs.getLong("comment_id");
				long msg_id = rs.getLong("message_id");
				String msg = rs.getString("comment");
				String author = rs.getString("author");
				Date date = rs.getDate("createdDate");
				comment = new Comment(id, msg, author);
				comment.setMessage_id(msg_id);
				comment.setCreatedDate(date);
			}
			stmt.close();
			con.close();

		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return comment;
	}

	public Comment addComment(long messageId, Comment comment) {
		long id;
		String sql = "INSERT INTO comments(comment,author,message_id) VALUES (?,?,?)";
		Comment newComment = null;
		boolean commentInserted = false;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, comment.getComment());
			stmt.setString(2, comment.getAuthor());
			stmt.setLong(3, messageId);
			commentInserted = stmt.executeUpdate() > 0;
			if (commentInserted) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					id = rs.getLong(1);
					newComment = getCommentById(messageId, id);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return newComment;
	}

	public boolean updateComment(long messageId, long commentId, Comment comment) {
		String sql = "UPDATE comments SET comment = ?, author = ? WHERE comment_id = ? AND message_id = ?";
		boolean commentUpdated = false;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, comment.getComment());
			stmt.setString(2, comment.getAuthor());
			stmt.setLong(3, commentId);
			stmt.setLong(4, messageId);
			commentUpdated = stmt.executeUpdate() > 0;
			stmt.close();
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
		return commentUpdated;
	}

	public boolean deleteComment(long messageId, long commentId) {
		String sql = "DELETE FROM comments WHERE comment_id = ? AND message_id = ?";
		boolean commentDeleted = false;
		try {
			connect();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, commentId);
			stmt.setLong(2, messageId);
			commentDeleted = stmt.executeUpdate() > 0;
			stmt.close();
			con.close();

		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		return commentDeleted;
	}

}
