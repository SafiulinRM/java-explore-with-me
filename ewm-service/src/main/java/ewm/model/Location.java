package ewm.model;

import ewm.dto.LocationShort;
import lombok.*;

import javax.persistence.*;

/**
 * Класс хранения локаций.
 *
 * @author safiulinrm
 * @see ewm.dto.LocationShort
 */
@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    /**
     * id локации.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * широта.
     */
    @Column(nullable = false)
    private Float lat;
    /**
     * долгота.
     */
    @Column(nullable = false)
    private Float lon;

    /**
     * конструктор локации.
     *
     * @param locationShort dto локации
     */
    public Location(LocationShort locationShort) {
        this.lat = locationShort.getLat();
        this.lon = locationShort.getLon();
    }
}
