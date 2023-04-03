package cos.peerna.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    JAVA, JAVASCRIPT, PYTHON, SPRING, REACT, NODE_JS, DJANGO, RDB, NOSQL, OS, SECURITY, NETWORK, DATA_STRUCTURE;

    @JsonCreator
    public static Category from(String s) {
        return Category.valueOf(s.toUpperCase());
    }

}
