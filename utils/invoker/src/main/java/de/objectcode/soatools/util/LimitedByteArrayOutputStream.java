package de.objectcode.soatools.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LimitedByteArrayOutputStream extends ByteArrayOutputStream {

	private boolean isClosed = false;
	private int maxSize;
	private Object monitor;

	public LimitedByteArrayOutputStream() {
		this(100 * 1024);
	}

	public LimitedByteArrayOutputStream(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public synchronized void write(byte[] b, int off, int len) {
		super.write(b, off, len);
		if (count > maxSize)
			throw new RuntimeException("Limit exeeced");
	}

	@Override
	public synchronized void write(int b) {
		super.write(b);
		if (count > maxSize)
			throw new RuntimeException("Limit exeeced");
	}

	@Override
	public synchronized void writeTo(OutputStream out) throws IOException {
		super.writeTo(out);
		if (count > maxSize)
			throw new RuntimeException("Limit exeeced");
	}

	@Override
	public synchronized void close() throws IOException {
		super.close();
		System.out
				.println(">> Hubba " + Thread.currentThread() + " " + monitor);

		isClosed = true;
		notifyAll();
	}

	public boolean isClosed() {
		return isClosed;
	}

	public int getCount() {
		return count;
	}
}
