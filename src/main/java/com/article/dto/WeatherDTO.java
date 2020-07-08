package com.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 夜雨寒笙 on 2020/7/8 11:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {

    private String date;
    private String weather;
    private String temperatue;
    private String wind;
}
