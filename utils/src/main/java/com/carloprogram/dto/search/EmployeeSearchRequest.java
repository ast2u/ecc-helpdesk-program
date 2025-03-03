package com.carloprogram.dto.search;

import com.carloprogram.model.enums.EmploymentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EmployeeSearchRequest {
    private Integer page = 0;
    private Integer size = 4;
    private String name;
    private LocalDate birthDate;
    private String address;
    private EmploymentStatus status;
    private boolean deleted;
    private List<Long> roles;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdEnd;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedEnd;

}
