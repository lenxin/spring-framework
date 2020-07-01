package org.springframework.http.codec.cbor;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import reactor.core.publisher.Flux;

import org.springframework.core.ResolvableType;
import org.springframework.core.codec.AbstractDecoderTestCase;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.Pojo;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.MimeType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.core.ResolvableType.forClass;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Unit tests for {@link Jackson2CborDecoder}.
 *
 * @author Sebastien Deleuze
 */
public class Jackson2CborDecoderTests extends AbstractDecoderTestCase<Jackson2CborDecoder> {

	private final static MimeType CBOR_MIME_TYPE = new MimeType("application", "cbor");

	private Pojo pojo1 = new Pojo("f1", "b1");

	private Pojo pojo2 = new Pojo("f2", "b2");

	private ObjectMapper mapper = Jackson2ObjectMapperBuilder.cbor().build();

	public Jackson2CborDecoderTests() {
		super(new Jackson2CborDecoder());
	}

	@Override
	@Test
	public void canDecode() {
		assertTrue(decoder.canDecode(forClass(Pojo.class), CBOR_MIME_TYPE));
		assertTrue(decoder.canDecode(forClass(Pojo.class), null));

		assertFalse(decoder.canDecode(forClass(String.class), null));
		assertFalse(decoder.canDecode(forClass(Pojo.class), APPLICATION_JSON));
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void decode() {
		Flux<DataBuffer> input = Flux.just(this.pojo1, this.pojo2)
				.map(this::writeObject)
				.flatMap(this::dataBuffer);

		testDecodeAll(input, Pojo.class, step -> step
				.expectNext(pojo1)
				.expectNext(pojo2)
				.verifyComplete());

	}

	private byte[] writeObject(Object o) {
		try {
			return this.mapper.writer().writeValueAsBytes(o);
		}
		catch (JsonProcessingException e) {
			throw new AssertionError(e);
		}

	}

	@Override
	public void decodeToMono() {
		List<Pojo> expected = Arrays.asList(pojo1, pojo2);

		Flux<DataBuffer> input = Flux.just(expected)
				.map(this::writeObject)
				.flatMap(this::dataBuffer);

		ResolvableType elementType = ResolvableType.forClassWithGenerics(List.class, Pojo.class);
		testDecodeToMono(input, elementType, step -> step
				.expectNext(expected)
				.expectComplete()
				.verify(), null, null);
	}
}
