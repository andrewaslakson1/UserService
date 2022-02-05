package my.project.entity.dtos.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import my.project.entity.User;

@Data
@NoArgsConstructor
public class UserResponse {

    private Long userID;
    private String username;

    public UserResponse(User user) {
        this.userID = user.getUserID();
        this.username = user.getUsername();
    }

}
