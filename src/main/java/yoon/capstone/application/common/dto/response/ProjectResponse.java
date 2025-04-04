package yoon.capstone.application.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectResponse {

    private long idx;

    private String writer;

    private String title;

    private String img;

    private int goal;

    private int curr;

    private int count;

    private String category;

    private String created;

    private String enddate;

    private String profile;

}


