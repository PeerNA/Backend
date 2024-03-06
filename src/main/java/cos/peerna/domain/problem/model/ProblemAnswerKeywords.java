package cos.peerna.domain.problem.model;

import cos.peerna.domain.keyword.model.Keyword;
import java.util.List;

public interface ProblemAnswerKeywords {
    String getAnswer();
    List<Keyword> getKeywords();
}
