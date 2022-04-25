package ca.uhn.fhir.jpa.searchparam.nickname;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;

@Service
public class NicknameSvc {
	private final NicknameMap myNicknameMap = new NicknameMap();
	
	@Resource(name = "/nickname/names.csv")
	private File myNicknameCsvFile;

	@PostConstruct
	public void init() {
		
	}

	public int size() {
		return myNicknameMap.size();
	}
}
