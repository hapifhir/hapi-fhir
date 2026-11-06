package ca.uhn.fhir.test.utilities.client;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class UnboundedRequestEntity implements HttpEntity {
	private long myBytesRead = 0;

	public UnboundedRequestEntity() {
		super();
	}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public boolean isChunked() {
		return false;
	}

	@Override
	public long getContentLength() {
		return -1;
	}

	@Override
	public Header getContentType() {
		return new BasicHeader("Content-Type", "application/json");
	}

	@Override
	public Header getContentEncoding() {
		return new BasicHeader("Content-Encoding", "gzip");
	}

	@Override
	public InputStream getContent() throws IOException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public long getBytesRead() {
		return myBytesRead;
	}

	@Override
	public void writeTo(OutputStream outStream) throws IOException {
		GZIPOutputStream gos = new GZIPOutputStream(outStream);

		byte[] bytes = new byte[10000];
		for (long l = 0; l < ((Integer.MAX_VALUE) / bytes.length) + 100; l++) {
			gos.write(bytes);
			gos.flush();
			myBytesRead += bytes.length;
		}

		gos.close();
	}

	@Override
	public boolean isStreaming() {
		return false;
	}

	@Override
	public void consumeContent() throws IOException {
		throw new UnsupportedOperationException();
	}
}
