package cos.peerna.service;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import cos.peerna.domain.Category;
import cos.peerna.domain.Problem;
import cos.peerna.repository.ProblemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProblemServiceTest {

	@Autowired ProblemService problemService;
	@Autowired ProblemRepository problemRepository;

	@Test
	@DisplayName("중복 문제 검증")
	void make() {
		ProblemRegisterRequestDto problemRegisterRequestDto = new ProblemRegisterRequestDto();
		problemRegisterRequestDto.setCategory(Category.JAVA);
		problemRegisterRequestDto.setQuestion("hello");
		problemRegisterRequestDto.setAnswer("안녕");
		Problem problem = Problem.createProblem(problemRegisterRequestDto);
		problemRepository.save(problem);
	}
}