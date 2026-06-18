package com.example.exam.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserRolesUpdateRequest {

    @NotEmpty
    private List<String> roleCodes;
}

