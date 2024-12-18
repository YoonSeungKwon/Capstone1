package yoon.capstone.application.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoon.capstone.application.common.dto.response.FriendsReqResponse;
import yoon.capstone.application.common.dto.response.FriendsResponse;
import yoon.capstone.application.common.dto.response.MemberDetailResponse;
import yoon.capstone.application.common.dto.response.MemberResponse;
import yoon.capstone.application.service.FriendsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
@Tag(name = "친구 관련 API", description = "v1")
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping("/")
    @Operation(summary = "받은 친구 요청 불러오기")
    public ResponseEntity<List<FriendsReqResponse>> getRequests(){

        List<FriendsReqResponse> result = friendsService.getFriendsRequest();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/info/{memberIndex}")                        //친구 정보, 상태 보기
    @Operation(summary = "친구 정보 불러오기")
    public ResponseEntity<MemberDetailResponse> getFriends(@PathVariable long memberIndex){

        MemberDetailResponse result = friendsService.friendsDetail(memberIndex);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }//친구만 정보를 볼 수 있게 수정

    @GetMapping("/lists")                      //친구 목록 불러오기
    @Operation(summary = "친구 목록 불러오기", description = "본인이 친구 목록을 가져온다. 신청중인 친구 제외")
    public ResponseEntity<List<MemberResponse>> getFriendsList(){

        List<MemberResponse> result = friendsService.getFriendsList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/link/{memberIndex}")                       //친구 요청 보내기
    @Operation(summary = "친구 신청하기")
    public ResponseEntity<FriendsResponse> requestFriends(@PathVariable long memberIndex){

        FriendsResponse result = friendsService.requestFriends(memberIndex);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/answer/{friendIdx}/{status}")                     //친구 요청 응답
    @Operation(summary = "친구 요청 응답", description = "status에 따라서 친구요청 수락 혹은 거절 가능")
    public ResponseEntity<?> responseFriends(@PathVariable String status, @PathVariable long friendIdx){

        List<FriendsResponse> result;

        if(status.equals("ok")){
            result = friendsService.acceptFriends(friendIdx);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            friendsService.declineFriends(friendIdx);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

    }

    @DeleteMapping("/unlink/{friendIdx}")             //친구 삭제
    @Operation(summary = "친구 삭제")
    public ResponseEntity<?> deleteFriends(@PathVariable long friendIdx){

        friendsService.deleteFriends(friendIdx);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }



}
