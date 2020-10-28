package org.springframework.context.index.sample;

import org.springframework.stereotype.Component;

/**
 * Test candidate for an embedded {@link Component}.
 *

 */
public class SampleEmbedded {

	@Component
	public static class PublicCandidate {

	}

	public static class Another {

		@Component
		public static class AnotherPublicCandidate {

		}
	}

}
