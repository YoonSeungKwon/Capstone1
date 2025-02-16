package yoon.capstone.application.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MemberDetailResponse {

    private String email;

    private String username;

    private String phone;

    private String profile;

    private boolean oauth;

    private LocalDateTime regdate;

    private LocalDateTime lastVisit;


}
