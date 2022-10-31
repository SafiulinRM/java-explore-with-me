package exploreWithMe.filter;

import exploreWithMe.status.EventState;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EventAdminFilter {
    private List<Long> users;
    private List<String> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
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

    public List<EventState> getStatesEnum() {
        return states.stream().map(EventState::valueOf).collect(Collectors.toList());
    }
}
