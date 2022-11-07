package ewm.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

/**
 * Класс хранения подборок.
 *
 * @author safiulinrm
 * @see ewm.dto.compilation.CompilationDto
 * @see ewm.dto.compilation.NewCompilationDto
 */
@Entity
@Table(name = "compilations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    /**
     * список событий.
     */
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "events_compilation",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "compilation_id")}
    )
    @ToString.Exclude
    private List<Event> events = new ArrayList<>();
    /**
     * id подборки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * закрепленная/не закрепленная подборка.
     */
    @Column
    private Boolean pinned;
    /**
     * заглавие подборки.
     */
    @Column(length = 128, nullable = false)
    private String title;

    /**
     * Equals по полю {@link #id}.
     *
     * @return Equals по полю {@link #id}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Compilation that = (Compilation) o;
        return id != null && Objects.equals(id, that.id);
    }

    /**
     * HashCode по классу #Compilation.
     *
     * @return HashCode по полю {@link #Compilation}.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
