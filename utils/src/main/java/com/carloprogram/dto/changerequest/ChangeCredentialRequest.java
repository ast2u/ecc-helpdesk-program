package com.carloprogram.dto.changerequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCredentialRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
