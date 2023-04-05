package cos.peerna.service;

import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import cos.peerna.domain.Problem;
import cos.peerna.domain.Reply;
import cos.peerna.domain.User;
import cos.peerna.repository.ProblemRepository;
import cos.peerna.repository.ReplyRepository;
import cos.peerna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyRepository replyRepository;
	private final UserRepository userRepository;
	private final ProblemRepository problemRepository;

	public void make(ReplyRegisterRequestDto dto) {
		User user = userRepository.findByEmail(dto.getUserEmail()).orElseThrow(() -> new UsernameNotFoundException("No User Data"));
		Problem problem = problemRepository.findById(dto.getProblemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
		Reply reply = Reply.createReply(user, problem, dto.getAnswer());
		replyRepository.save(reply);
	}
}
