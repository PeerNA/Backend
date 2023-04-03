package cos.peerna.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likey {

	@Id @GeneratedValue
	@Column(name = "like_id")
	private Long id;

	@NotNull @ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull @ManyToOne
	@JoinColumn(name = "reply_id")
	private Reply reply;

//	매핑 정리 필요

//	@OneToOne(mappedBy = "like")
//	private User user;
//
//	@OneToOne(mappedBy = "like")
//	private Reply reply;
}
