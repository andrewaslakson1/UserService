package my.project.entity.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import my.project.entity.User;

@Data
@NoArgsConstructor
public class UpdateUserRequest {

    private Long userID;
    private String username;

    public User get() {
        return new User(userID, username);
    }
}
