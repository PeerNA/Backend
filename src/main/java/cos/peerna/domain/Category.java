package cos.peerna.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;

public enum Category {
    JAVA, JAVASCRIPT, PYTHON, SPRING, JPA, REACT, NODEJS, DJANGO, RDB, NOSQL, OS, SECURITY, NETWORK, DATA_STRUCTURE, ALGORITHM, COMPILER, TROUBLE, DESIGN, TEST, INFRA, CONTAINER, DEVOPS, COMMUNICATION, WORK, TECH, CODING_TEST;

    @JsonCreator
    public static Category from(String s) {
        return Category.valueOf(s.toUpperCase());
    }

}
