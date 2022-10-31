package exploreWithMe.mapper;

import exploreWithMe.dto.user.NewUserRequest;
import exploreWithMe.dto.user.UserDto;
import exploreWithMe.dto.user.UserShortDto;
import exploreWithMe.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()
        );
    }

    public static User toUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

    public static UserShortDto toUserShortDto(User user){
        return new UserShortDto(user.getId(), user.getName());
    }

    public static List<UserDto> toUsersDto(Page<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(toList());
    }
}
