package org.springframework.jdbc.core.namedparam;

import java.sql.Types;

import org.junit.Test;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.JdbcUtils;

import static org.junit.Assert.*;

/**



 */
public class MapSqlParameterSourceTests {

	@Test
	public void nullParameterValuesPassedToCtorIsOk() {
		new MapSqlParameterSource(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getValueChokesIfParameterIsNotPresent() {
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.getValue("pechorin was right!");
	}

	@Test
	public void sqlParameterValueRegistersSqlType() {
		MapSqlParameterSource msps = new MapSqlParameterSource("FOO", new SqlParameterValue(Types.NUMERIC, "Foo"));
		assertEquals("Correct SQL Type not registered", 2, msps.getSqlType("FOO"));
		MapSqlParameterSource msps2 = new MapSqlParameterSource();
		msps2.addValues(msps.getValues());
		assertEquals("Correct SQL Type not registered", 2, msps2.getSqlType("FOO"));
	}

	@Test
	public void toStringShowsParameterDetails() {
		MapSqlParameterSource source = new MapSqlParameterSource("FOO", new SqlParameterValue(Types.NUMERIC, "Foo"));
		assertEquals("MapSqlParameterSource {FOO=Foo (type:NUMERIC)}", source.toString());
	}

	@Test
	public void toStringShowsCustomSqlType() {
		MapSqlParameterSource source = new MapSqlParameterSource("FOO", new SqlParameterValue(Integer.MAX_VALUE, "Foo"));
		assertEquals("MapSqlParameterSource {FOO=Foo (type:" + Integer.MAX_VALUE + ")}", source.toString());
	}

	@Test
	public void toStringDoesNotShowTypeUnknown() {
		MapSqlParameterSource source = new MapSqlParameterSource("FOO", new SqlParameterValue(JdbcUtils.TYPE_UNKNOWN, "Foo"));
		assertEquals("MapSqlParameterSource {FOO=Foo}", source.toString());
	}

}
