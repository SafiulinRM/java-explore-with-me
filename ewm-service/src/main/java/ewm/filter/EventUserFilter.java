package ewm.filter;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class EventUserFilter {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private String sort;
    private Integer from;
    private Integer size;

    private static final String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public void setRangeStart(String rangeStart) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN);
        this.rangeStart = LocalDateTime.parse(rangeStart, formatter);
    }

    public void setRangeEnd(String rangeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN);
        this.rangeEnd = LocalDateTime.parse(rangeEnd, formatter);
    }
}
