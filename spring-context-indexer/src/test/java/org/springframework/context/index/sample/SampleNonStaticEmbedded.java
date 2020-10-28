package org.springframework.context.index.sample;

import org.springframework.stereotype.Component;

/**
 * Candidate with a inner class that isn't static (and should therefore not be added).
 *

 */
public class SampleNonStaticEmbedded {

	@Component
	public class InvalidCandidate {

	}

}
