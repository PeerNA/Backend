package cos.peerna.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
	@Id @GeneratedValue
	@Column(name = "like_id")
	private Long likeId;

//	매핑 정리 필요

//	@OneToOne(mappedBy = "like")
//	private User user;
//
//	@OneToOne(mappedBy = "like")
//	private Reply reply;
}
