package cos.peerna.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class KeywordPK implements Serializable {
	private Problem problem;
	private String name;
}
