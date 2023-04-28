package cos.peerna.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;


@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeywordPK implements Serializable {
	@NotNull
	private Problem problem;
	@NotNull
	private String name;

}
