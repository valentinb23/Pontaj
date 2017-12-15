package net.metrosystems.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;

import net.metrosystems.domain.Pontaj;

public class GetPontajData {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Pontaj> read() {

		String sql = "SELECT * FROM pontaj order by zi";

		// List<Map> rows = jdbcTemplate.queryForList(sql);
		List<Pontaj> pontaje = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Pontaj.class));

		return pontaje;

	}
}
