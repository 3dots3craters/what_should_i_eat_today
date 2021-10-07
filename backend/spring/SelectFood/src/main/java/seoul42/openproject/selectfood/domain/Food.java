package seoul42.openproject.selectfood.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter @Setter
public class Food {

    @Id @GeneratedValue
    private AtomicLong id;
    private String name;
    private String category;
    private String taste;
    private String ingredient;
    private String imgUrl;
}
