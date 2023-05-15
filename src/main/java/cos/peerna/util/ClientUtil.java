package cos.peerna.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cos.peerna.security.dto.SessionUser;
import lombok.Value;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class ClientUtil {

	private final String url = "https://github.com/PeerNA/tech-interview-for-peerna/pulls";

	public void openPullRequest(SessionUser sessionUser, String title, String body) throws IOException {
		GitHub gitHub = new GitHubBuilder().withOAuthToken(sessionUser.getToken()).build();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
		request.add("owner", name);
		request.add("repo", "tech-interview-for-peerna");
		request.add("title", title);
		request.add("body", body);
		request.add("head", name);
		request.add("base", "main");

		HttpEntity<?> httpEntity = new HttpEntity<>(request, httpHeaders);

		HttpEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
	}
}
