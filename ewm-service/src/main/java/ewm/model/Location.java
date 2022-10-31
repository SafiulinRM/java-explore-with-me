package ewm.model;

import ewm.dto.LocationShort;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Float lat;
    @Column(nullable = false)
    private Float lon;

    public Location(LocationShort locationShort) {
        this.lat = locationShort.getLat();
        this.lon = locationShort.getLon();
    }
}
