package ca.uhn.fhir.jpa.searchparam.nickname;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Service
public class NicknameSvc {
	private final NicknameMap myNicknameMap = new NicknameMap();

	@Value("classpath:/nickname/names.csv")
	Resource myNicknameCsvResource;
	
	@PostConstruct
	public void init() throws IOException {
		try (InputStream inputStream = myNicknameCsvResource.getInputStream()) {
			try (Reader reader = new InputStreamReader(inputStream)) {
				myNicknameMap.load(reader);
			}
		}
	}

	public int size() {
		return myNicknameMap.size();
	}
}
