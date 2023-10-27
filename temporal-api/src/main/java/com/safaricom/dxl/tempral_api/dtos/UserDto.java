package com.safaricom.dxl.tempral_api.dtos;


import com.safaricom.dxl.tempral_api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String username;
    private String email;
    private String password;
    private String status;

    public User toEntity() {
        return User.builder().username(this.getUsername())
                .email(this.getEmail()).password(this.getPassword()).build();
    }
}
