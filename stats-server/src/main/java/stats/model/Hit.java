package stats.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1024, nullable = false)
    private String app;
    @Column(length = 1024, nullable = false)
    private String uri;
    @Column(length = 1024, nullable = false)
    private String ip;
    @Column(name = " times_tamp", nullable = false)
    private LocalDateTime timestamp;
}
