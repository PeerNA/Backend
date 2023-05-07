package cos.peerna.service;

import com.twitter.penguin.korean.KoreanTokenJava;
import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import com.twitter.penguin.korean.tokenizer.KoreanTokenizer;
import cos.peerna.controller.dto.KeywordRegisterRequestDto;
import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import cos.peerna.repository.KeywordRepository;
import cos.peerna.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import scala.collection.Seq;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService {

    private final ProblemRepository problemRepository;
    private final KeywordRepository keywordRepository;

    public void make(String name, Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        Optional<Keyword> find = keywordRepository.findKeywordByNameAndProblem(name, problem);

        if (!find.isPresent()) {
            Keyword keyword = Keyword.createKeyword(name, problem);
            keywordRepository.save(keyword);
        }
        else {
            Keyword.updateKeyword(find.get());
        }
    }

    public void analyze(String answer, Long problemId) {
        CharSequence normalized = TwitterKoreanProcessorJava.normalize(answer);
        Seq<KoreanTokenizer.KoreanToken> tokens = TwitterKoreanProcessorJava.tokenize(normalized);
        List<KoreanTokenJava> koreanTokens = TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(tokens);

        Map<String, Integer> map = new HashMap<String, Integer>();
        for (KoreanTokenJava token : koreanTokens) {
            if (token.toString().contains("Noun")) {
                if (map.containsKey(token.getText())) {
                    map.put(token.getText(), map.get(token.getText()) + 1);
                } else {
                    map.put(token.getText(), 1);
                }
            }
        }

        ArrayList<Keyword> keywords = new ArrayList<>();
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        for (String key : map.keySet()) {
            Optional<Keyword> findKeyword = keywordRepository.findKeywordByNameAndProblem(key, problem);
            if (!findKeyword.isPresent()) {
                Keyword keyword = Keyword.builder()
                        .name(key)
                        .problem(problem)
                        .count((long) map.get(key))
                        .build();
                keywords.add(keyword);
            } else {
                Keyword.updateKeyword(findKeyword.get());
            }
        }
        keywordRepository.saveAll(keywords);
    }
//
//    public Keyword findOne(KeywordRegisterRequestDto dto) {
//        Problem problem = problemRepository.findById(dto.getProblemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
//        Keyword keyword = keywordRepository.findKeywordByNameAndProblem(dto.getAnswer(), problem).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Keyword Not Found"));
//        return keyword;
//    }

//    private void validateKeyword(Keyword keyword) {
//        keywordRepository.findKeywordByNameAndProblem(keyword.getName(), keyword.getProblem()).ifPresent(Keyword::updateKeyword);
//    }
}

