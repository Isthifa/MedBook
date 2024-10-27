package com.usermanage.userdatamanage.jwt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Authentication request")
@Data
public class Authreq {
    @Schema(description = "User's username", example = "john.doe")
    private String userName;

    @Schema(description = "User's password", example = "password123")
    private String passWord;
}
