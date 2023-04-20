package com.example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * DAO for users table
 */
@ApplicationScoped
public class UsersDAO {
	/**
	 * JNDIで管理されたDataSourceオブジェクトは@Resourceアノテーションで
	 * 取得できます。lookup属性でJNDI名を渡します。
	 */
	@Resource(lookup = "jdbc/__default")
	private DataSource ds;

	@Inject
	private Users users;

	public void getAll() {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT name, role FROM users");) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				users.add(new UserDTO(rs.getString("name"), rs.getString("role"), ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void get(String name) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users where name=?");) {
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			users.add(new UserDTO(rs.getString("name"), rs.getString("role"), rs.getString("password")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create(UserDTO userDTO) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("INSERT INTO users VALUES(?, ?, ?)")) {
			pstmt.setString(1, userDTO.getName());
			pstmt.setString(2, userDTO.getRole());
			pstmt.setString(3, userDTO.getPassword());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteAll() {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("DELETE from users");) {
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(String name) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("DELETE from users where name=?");) {
			pstmt.setString(1, name);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
