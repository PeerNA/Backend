package cos.peerna.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

	@Id @GeneratedValue
	@Column(name = "chat_id")
	private Long id;

	@NotNull
	private Long aUserId;

	@NotNull
	private Long bUserId;

	private String content;

	//	찾아보니까 Date 단점 많아서 LocalTime으로 사용
	private LocalTime time;

	@NotNull @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "history_id")
	private History history;

}
