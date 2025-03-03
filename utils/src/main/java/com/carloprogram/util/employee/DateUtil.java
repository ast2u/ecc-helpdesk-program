package com.carloprogram.util.employee;

import java.time.LocalDate;
import java.time.Period;

public class DateUtil {
    public static int computeAge(LocalDate birthDate) {
        return (birthDate == null) ? 0 : Period.between(birthDate, LocalDate.now()).getYears();
    }
}
