package cos.peerna.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Follow extends BaseTimeEntity {
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn
    private User follower;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private User followee;

    public Follow(User follower, User followee) {
        this.follower = follower;
        this.followee = followee;
        follower.getFollowings().add(this);
        followee.getFollowers().add(this);
    }

    @Override
    public void delete() {
        follower.getFollowings().remove(this);
        followee.getFollowers().remove(this);
    }
}
