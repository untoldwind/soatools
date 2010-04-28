package de.objectcode.soatools.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.stream.StreamSource;

public class GZipContent extends StreamSource implements Serializable {
	private static final long serialVersionUID = 1L;

	public byte[] data;

	private void writeObject(ObjectOutputStream out) throws IOException {
		if (data == null)
			out.writeInt(0);
		else {
			out.writeInt(data.length);
			out.write(data);
		}
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		data = new byte[in.readInt()];
		in.readFully(data);
	}

	@Override
	public InputStream getInputStream() {
		try {
			return new GZIPInputStream(new ByteArrayInputStream(data));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public OutputStream getOutputStream() {
		try {
			return new GZIPOutputStream(new ByteArrayOutputStream()) {
				@Override
				public void close() throws IOException {
					super.flush();
					super.close();

					data = ((ByteArrayOutputStream) out).toByteArray();
				}
			};
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
