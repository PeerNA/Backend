package cos.peerna.controller.dto;

import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
public class HistoryResponseDto implements Comparator<HistoryResponseDto> {
    private Long     historyId;
    private Long     problemId;
    private String   question;
    private Category category;
    private LocalDate time;

    @Override
    public int compare(HistoryResponseDto dto1, HistoryResponseDto dto2) {
        if (dto1.historyId > dto2.historyId) {
            return 1;
        } else if (dto1.historyId < dto2.historyId) {
            return -1;
        }
        return 0;
    }
}



