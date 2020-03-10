package com.kasafal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;


public abstract class ADaoImpl {
	Connection con = DatabaseConnection.getConnaction();

	public <T> List<T> select(String query, PreparedStatementSetter ps,RowMapper<T> rm) {
		try {
			PreparedStatement statement = con.prepareStatement(query);
			if(ps !=null)
				ps.setValues(statement);
			ResultSet rs = statement.executeQuery();
			List<T> list= new ArrayList<T>();
			while(rs.next()) {
				list.add(rm.mapRow(rs, 0));
			}
			return list;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	public void insert(String query) {
		

	}
	public void update(String query) {
		
	}

}
