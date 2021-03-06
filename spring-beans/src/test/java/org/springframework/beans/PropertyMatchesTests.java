package org.springframework.beans;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Tests for {@link PropertyMatches}.
 *
 * @author Stephane Nicoll
 */
public class PropertyMatchesTests {

	@Test
	public void simpleBeanPropertyTypo() {
		PropertyMatches matches = PropertyMatches.forProperty("naem", SampleBeanProperties.class);
		assertThat(matches.getPossibleMatches(), hasItemInArray("name"));
	}

	@Test
	public void complexBeanPropertyTypo() {
		PropertyMatches matches = PropertyMatches.forProperty("desriptn", SampleBeanProperties.class);
		assertThat(matches.getPossibleMatches(), emptyArray());
	}

	@Test
	public void unknownBeanProperty() {
		PropertyMatches matches = PropertyMatches.forProperty("unknown", SampleBeanProperties.class);
		assertThat(matches.getPossibleMatches(), emptyArray());
	}

	@Test
	public void severalMatchesBeanProperty() {
		PropertyMatches matches = PropertyMatches.forProperty("counter", SampleBeanProperties.class);
		assertThat(matches.getPossibleMatches(), hasItemInArray("counter1"));
		assertThat(matches.getPossibleMatches(), hasItemInArray("counter2"));
		assertThat(matches.getPossibleMatches(), hasItemInArray("counter3"));
	}

	@Test
	public void simpleBeanPropertyErrorMessage() {
		PropertyMatches matches = PropertyMatches.forProperty("naem", SampleBeanProperties.class);
		String msg = matches.buildErrorMessage();
		assertThat(msg, containsString("naem"));
		assertThat(msg, containsString("name"));
		assertThat(msg, containsString("setter"));
		assertThat(msg, not(containsString("field")));
	}

	@Test
	public void complexBeanPropertyErrorMessage() {
		PropertyMatches matches = PropertyMatches.forProperty("counter", SampleBeanProperties.class);
		String msg = matches.buildErrorMessage();
		assertThat(msg, containsString("counter"));
		assertThat(msg, containsString("counter1"));
		assertThat(msg, containsString("counter2"));
		assertThat(msg, containsString("counter3"));
	}

	@Test
	public void simpleFieldPropertyTypo() {
		PropertyMatches matches = PropertyMatches.forField("naem", SampleFieldProperties.class);
		assertThat(matches.getPossibleMatches(), hasItemInArray("name"));
	}

	@Test
	public void complexFieldPropertyTypo() {
		PropertyMatches matches = PropertyMatches.forField("desriptn", SampleFieldProperties.class);
		assertThat(matches.getPossibleMatches(), emptyArray());
	}

	@Test
	public void unknownFieldProperty() {
		PropertyMatches matches = PropertyMatches.forField("unknown", SampleFieldProperties.class);
		assertThat(matches.getPossibleMatches(), emptyArray());
	}

	@Test
	public void severalMatchesFieldProperty() {
		PropertyMatches matches = PropertyMatches.forField("counter", SampleFieldProperties.class);
		assertThat(matches.getPossibleMatches(), hasItemInArray("counter1"));
		assertThat(matches.getPossibleMatches(), hasItemInArray("counter2"));
		assertThat(matches.getPossibleMatches(), hasItemInArray("counter3"));
	}

	@Test
	public void simpleFieldPropertyErrorMessage() {
		PropertyMatches matches = PropertyMatches.forField("naem", SampleFieldProperties.class);
		String msg = matches.buildErrorMessage();
		assertThat(msg, containsString("naem"));
		assertThat(msg, containsString("name"));
		assertThat(msg, containsString("field"));
		assertThat(msg, not(containsString("setter")));
	}

	@Test
	public void complexFieldPropertyErrorMessage() {
		PropertyMatches matches = PropertyMatches.forField("counter", SampleFieldProperties.class);
		String msg = matches.buildErrorMessage();
		assertThat(msg, containsString("counter"));
		assertThat(msg, containsString("counter1"));
		assertThat(msg, containsString("counter2"));
		assertThat(msg, containsString("counter3"));
	}


	private static class SampleBeanProperties {

		private String name;

		private String description;

		private int counter1;

		private int counter2;

		private int counter3;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getCounter1() {
			return counter1;
		}

		public void setCounter1(int counter1) {
			this.counter1 = counter1;
		}

		public int getCounter2() {
			return counter2;
		}

		public void setCounter2(int counter2) {
			this.counter2 = counter2;
		}

		public int getCounter3() {
			return counter3;
		}

		public void setCounter3(int counter3) {
			this.counter3 = counter3;
		}
	}


	private static class SampleFieldProperties {

		private String name;

		private String description;

		private int counter1;

		private int counter2;

		private int counter3;

	}

}
