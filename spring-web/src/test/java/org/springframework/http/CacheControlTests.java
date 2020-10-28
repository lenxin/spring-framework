package org.springframework.http;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**

 */
public class CacheControlTests {

	@Test
	public void emptyCacheControl() throws Exception {
		CacheControl cc = CacheControl.empty();
		assertThat(cc.getHeaderValue(), Matchers.nullValue());
	}

	@Test
	public void maxAge() throws Exception {
		CacheControl cc = CacheControl.maxAge(1, TimeUnit.HOURS);
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600"));
	}

	@Test
	public void maxAge_duration() throws Exception {
		CacheControl cc = CacheControl.maxAge(Duration.ofHours(1));
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600"));
	}

	@Test
	public void maxAgeAndDirectives() throws Exception {
		CacheControl cc = CacheControl.maxAge(3600, TimeUnit.SECONDS).cachePublic().noTransform();
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600, no-transform, public"));
	}

	@Test
	public void maxAgeAndSMaxAge() throws Exception {
		CacheControl cc = CacheControl.maxAge(1, TimeUnit.HOURS).sMaxAge(30, TimeUnit.MINUTES);
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600, s-maxage=1800"));
	}

	@Test
	public void maxAgeAndSMaxAge_duration() throws Exception {
		CacheControl cc = CacheControl.maxAge(Duration.ofHours(1)).sMaxAge(Duration.ofMinutes(30));
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600, s-maxage=1800"));
	}

	@Test
	public void noCachePrivate() throws Exception {
		CacheControl cc = CacheControl.noCache().cachePrivate();
		assertThat(cc.getHeaderValue(), Matchers.equalTo("no-cache, private"));
	}

	@Test
	public void noStore() throws Exception {
		CacheControl cc = CacheControl.noStore();
		assertThat(cc.getHeaderValue(), Matchers.equalTo("no-store"));
	}

	@Test
	public void staleIfError() throws Exception {
		CacheControl cc = CacheControl.maxAge(1, TimeUnit.HOURS).staleIfError(2, TimeUnit.HOURS);
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600, stale-if-error=7200"));
	}

	@Test
	public void staleIfError_duration() throws Exception {
		CacheControl cc = CacheControl.maxAge(Duration.ofHours(1)).staleIfError(2, TimeUnit.HOURS);
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600, stale-if-error=7200"));
	}

	@Test
	public void staleWhileRevalidate() throws Exception {
		CacheControl cc = CacheControl.maxAge(1, TimeUnit.HOURS).staleWhileRevalidate(2, TimeUnit.HOURS);
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600, stale-while-revalidate=7200"));
	}

	@Test
	public void staleWhileRevalidate_duration() throws Exception {
		CacheControl cc = CacheControl.maxAge(Duration.ofHours(1)).staleWhileRevalidate(2, TimeUnit.HOURS);
		assertThat(cc.getHeaderValue(), Matchers.equalTo("max-age=3600, stale-while-revalidate=7200"));
	}

}
