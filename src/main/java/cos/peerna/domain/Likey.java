package cos.peerna.domain;

import cos.peerna.controller.dto.UserRegisterRequestDto;
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

	@NotNull @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_id")
	private Reply reply;

	public static Likey createLikey(User user, Reply reply) {
		Likey likey = new Likey();
		likey.reply = reply;
		likey.user = user;

		return likey;
	}
}
