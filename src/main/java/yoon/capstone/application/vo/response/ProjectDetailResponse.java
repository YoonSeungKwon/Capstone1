package yoon.capstone.application.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class ProjectDetailResponse {

    private String title;

    private String content;

    private String img;

    private String link;

    private int goal;

    private int curr;

    private LocalDateTime enddate;

}