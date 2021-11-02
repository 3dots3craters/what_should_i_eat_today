package seoul42.openproject.selectfood.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import seoul42.openproject.selectfood.dto.member.MemberSignUpDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "MEMBER")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "NICK_NAME", nullable = false, length = 100)
    private String nickName;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SelectedFood> selectedFoods = new ArrayList<SelectedFood>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<LikeFood> likeFoods = new ArrayList<LikeFood>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<DislikeFood> dislikeFoods = new ArrayList<DislikeFood>();

    public static Member createMember(MemberSignUpDto memberInfo, List<LikeFood> likeFoods, List<DislikeFood> dislikeFoods) {
        Member member = new Member();
        member.setEmail(memberInfo.getEmail());
        member.setNickName(memberInfo.getNickName());
        member.setPassword(memberInfo.getPassword());
        member.setRoles(Collections.singletonList("ROLE_USER"));
        for (LikeFood likeFood : likeFoods) {
            member.addLikeFood(likeFood);
        }
        for (DislikeFood dislikeFood : dislikeFoods) {
            member.addDislikeFood(dislikeFood);
        }
        return member;
    }

    /* 연관관계 매서드 */
    public void addSelectedFood(SelectedFood selectedFood) {
        selectedFoods.add(selectedFood);
        selectedFood.setMember(this);
    }

    public void addLikeFood(LikeFood likeFood) {
        likeFoods.add(likeFood);
        likeFood.setMember(this);
    }

    public boolean hasLikeFood(Long foodId) {
        LikeFood likeFood = likeFoods.stream()
                .filter(likeFood1 -> likeFood1.getFood().getId() == foodId)
                .findAny()
                .orElse(null);
        if (likeFood == null)
            return false;
        return true;
    }

    public void addDislikeFood(DislikeFood dislikeFood) {
        dislikeFoods.add(dislikeFood);
        dislikeFood.setMember(this);
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isEnabled() {
        return true;
    }
}
