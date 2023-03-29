package cos.peerna.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

	@Id
	@GeneratedValue
	@Column(name = "chat_id")
	private Long chatId;

//	매핑 필요
//	private User user;

	private String content;

//	찾아보니까 Date 단점 많아서 LocalTime으로 사용
	private LocalTime time;
}
