package cos.peerna.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class Interest {
    @Enumerated(EnumType.STRING)
    private Category priority1;
    @Enumerated(EnumType.STRING)
    private Category priority2;
    @Enumerated(EnumType.STRING)
    private Category priority3;

    protected Interest() {

    }

    public Interest(Category priority1, Category priority2, Category priority3) {
        this.priority1 = priority1;
        this.priority2 = priority2;
        this.priority3 = priority3;
    }
}
