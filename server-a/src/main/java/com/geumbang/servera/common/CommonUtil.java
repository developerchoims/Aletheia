package com.geumbang.servera.common;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CommonUtil {

    // orderNumber 생성기
    public String createOrderNumber(Object a, LocalDateTime b) {
        DateFormat dateFormat = new SimpleDateFormat("yyMMHHssmm");
        // LocalDateTime을 Date로 변환
        Date date = Date.from(b.atZone(ZoneId.systemDefault()).toInstant());
        String formattedDate = dateFormat.format(date);
        return a.toString() + "-" + formattedDate;
    }

}
