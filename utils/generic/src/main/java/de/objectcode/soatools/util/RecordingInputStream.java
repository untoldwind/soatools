package de.objectcode.soatools.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RecordingInputStream extends InputStream {
	GZipContent content;
	InputStream input;
	OutputStream output;

	RecordingInputStream(InputStream inputStream) {
		input = inputStream;
		content = new GZipContent();
		output = content.getOutputStream();
	}

	@Override
	public int read() throws IOException {
		int readed = input.read();

		if (readed >= 0)
			output.write(readed);

		return readed;
	}

	@Override
	public int available() throws IOException {
		return input.available();
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int readed = input.read(b, off, len);

		if (readed > 0) {
			output.write(b, off, readed);
		}

		return readed;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int readed = input.read(b);

		if (readed > 0) {
			output.write(b, 0, readed);
		}

		return readed;
	}

	@Override
	public void close() throws IOException {
		input.close();
		output.close();
	}

	public GZipContent getContent() {
		return content;
	}

}
