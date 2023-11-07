package com.utavi.ledger.api.service.misc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class SerializationUtils {

	private final Pool<Kryo> kryoPool;

	public SerializationUtils() {
		this.kryoPool = new Pool<>(true, false, 8) {
			protected Kryo create() {
				var kryo = new Kryo();
				kryo.setRegistrationRequired(false);
				return kryo;
			}
		};
	}

	public <T> String serializeAndHex(final T t, final Class<T> clazz) {
		final byte[] bytes = serialize(t, clazz);
		return bytesToHexString(bytes);
	}

	public <T> byte[] serialize(final T t, final Class<T> clazz) {
		final Kryo kryo = getWithDefaultSerializer(clazz);
		final ByteArrayOutputStream b = new ByteArrayOutputStream();
		final Output output = new Output(b);
		kryo.writeObject(output, t);
		final byte[] serializedEnrollment = output.getBuffer();
		output.close();
		this.kryoPool.free(kryo);
		return serializedEnrollment;
	}

	private String bytesToHexString(final byte[] bytes) {
		return Hex.toHexString(bytes);
	}

	public <T> T deserialize(final String hexString, final Class<T> clazz) {
		return deserialize(hexStringToBytes(hexString), clazz);
	}

	public <T> T deserialize(final byte[] serialized, final Class<T> clazz) {
		final Kryo kryo = this.kryoPool.obtain();
		kryo.register(clazz);
		final Input input = new Input(serialized);
		final T t = kryo.readObject(input, clazz);
		input.close();
		this.kryoPool.free(kryo);
		return t;
	}

	private byte[] hexStringToBytes(final String hexString) {
		return Hex.decode(hexString);
	}

	private <T> Kryo getWithDefaultSerializer(final Class<T> clazz) {
		final Kryo kryo = this.kryoPool.obtain();
		kryo.register(clazz);
		return kryo;
	}

}
