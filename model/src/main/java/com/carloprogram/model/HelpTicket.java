package com.carloprogram.model;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HelpTicket {
    private int ticket_number;
    private String title;
    private String body;
    private String assignee;
    private String status;
    private Date created_date;
    private String created_by;
    private Date updated_date;
    private String updated_by;
    private String remarks;

}
