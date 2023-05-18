package cos.peerna.service;


import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import cos.peerna.controller.dto.NotificationResponseDto;
import cos.peerna.domain.Notification;
import cos.peerna.domain.User;
import cos.peerna.repository.NotificationRepository;
import cos.peerna.repository.UserRepository;
import cos.peerna.security.dto.SessionUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	public NotificationResponseDto getNotifications(SessionUser sessionUser) {
		User user = userRepository.findById(sessionUser.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
		List<Notification> notificationList = notificationRepository.findByUser(user);

		return NotificationResponseDto.builder()
				.notificationList(notificationList)
				.build();
	}

	public void acceptNotification(SessionUser sessionUser, Long notificationId) {
		User user = userRepository.findById(sessionUser.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
		Notification notification = notificationRepository.findNotificationById(notificationId);

		Notification.acceptNotification(notification);

		String question = notification.getReply().getProblem().getQuestion();
		String answer = notification.getReply().getAnswer();

		String url = "https://api.github.com/repos/";

		if (Notification.isPRNotification(notification)) {
			forkRepository(sessionUser.getToken(), url + "ksundong/backend-interview-question/forks");
			createBranch(sessionUser.getToken(), url + sessionUser.getLogin() +
							"/backend-interview-question/git/refs",
							sessionUser.getLogin(), "backend-interview-question");
			getContentAndPush(sessionUser.getToken(), url + sessionUser.getLogin() +
							"/backend-interview-question/tree/peerna", question, answer);
			createPullReq(sessionUser.getToken(), url + sessionUser.getLogin() +
							"/backend-interview-question/tree/peerna/pulls", question);
		}
	}

	private void forkRepository(String token, String url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.parseMediaType("application/vnd.github.v3+json")));
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.POST, entity, String.class);
		if (response.getStatusCode() == HttpStatus.ACCEPTED) {
			log.info("Fork Success");
		} else {
			log.info("Fork Failed");
		}
	}

	private void createBranch(String token, String url, String login, String repo) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.parseMediaType("application/vnd.github.v3+json")));
		headers.set("Authorization", "Bearer " + token);

		String branchName = "peerna";
		String commitSha = "commit-sha-to-base-branch";
		String ref = "refs/heads/" + branchName;
		String apiUrlFormatted = MessageFormat.format(url, login, repo);

		Map<String, Object> refObject = new HashMap<>();
		refObject.put("ref", ref);
		refObject.put("sha", commitSha);

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(refObject, headers);
		ResponseEntity<String> response = restTemplate.exchange(apiUrlFormatted, HttpMethod.POST, requestEntity, String.class);
		if (response.getStatusCode() == HttpStatus.ACCEPTED) {
			log.info("Branch Created");
		} else {
			log.info("Branch Creation Failed");
		}
	}

	private void getContentAndPush(String token, String url, String question, String answer) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.parseMediaType("application/vnd.github.v3+json")));
		headers.set("Authorization", "Bearer " + token);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				url, HttpMethod.GET, entity, String.class);
		Gson gson = new Gson();
		String body = response.getBody();
		JsonObject responseJsonObject = gson.fromJson(body, JsonObject.class);

		// sha 추출
		String sha = responseJsonObject.get("sha").getAsString();
		log.info("sha: " + sha);

		// content 추출 및 Base64 디코딩
		String contentBase64 = responseJsonObject.get("content").getAsString();
		String content = new String(Base64.decodeBase64(contentBase64));
//		log.info("content: " + content);

		String[] split = content.split("<contents>|</contents>");
		String result = "";
		for (String s : split) {
			if (s.contains(question)) {
				result += "<details>\n" +
						"  <summary>" + question + "</summary>\n" +
						"  </br>\n" +
						"  <p>" + answer + "</p>\n"
						+ "</details>\n";
			} else {
				result += "<details>" + s + "</details>\n";
			}
		}

		updateContent(token, url, result, sha);
	}

	private void updateContent(String token, String url, String content, String sha) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.parseMediaType("application/vnd.github.v3+json")));
		headers.set("Authorization", "Bearer " + token);

		Map<String, Object> body = new HashMap<>();
		body.put("message", "update");
		body.put("content", Base64.encodeBase64(content.getBytes()));
		body.put("sha", sha);

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
		if (response.getStatusCode() == HttpStatus.ACCEPTED) {
			log.info("Content Updated");
		} else {
			log.info("Content Update Failed");
		}
	}

	private void createPullReq(String token, String url, String title) {
		String baseBranch = "main";
		String headBranch = "peerna";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.parseMediaType("application/vnd.github.v3+json")));

		Map<String, String> body = new HashMap<>();
		body.put("title", title);
		body.put("body", "PeerNA 서비스의 자동 PR입니다.");
		body.put("head", headBranch);
		body.put("base", baseBranch);

		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			log.info("PR Created");
		} else {
			log.info("PR Creation Failed");
		}
	}
}
