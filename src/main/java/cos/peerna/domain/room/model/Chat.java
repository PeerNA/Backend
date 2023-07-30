package cos.peerna.domain.room.model;

import cos.peerna.domain.history.model.History;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

	@Id @GeneratedValue
	@Column(name = "chat_id")
	private Long id;

	@NotNull
	private Long writerId;

	private String content;

	//	찾아보니까 Date 단점 많아서 LocalTime으로 사용
	private LocalTime time;

	@NotNull @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "history_id")
	private History history;

	@Builder
	public Chat(Long writerId, String content, History history) {
		this.writerId = writerId;
		this.content = content;
		this.history = history;
		this.time = LocalTime.now();
	}
}
