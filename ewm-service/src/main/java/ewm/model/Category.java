package ewm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Класс хранения категорий.
 *
 * @author safiulinrm
 * @see ewm.dto.category.CategoryDto
 * @see ewm.dto.category.NewCategoryDto
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    /**
     * id категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * имя категории.
     */
    @Column(nullable = false, length = 255, unique = true)
    private String name;
}
