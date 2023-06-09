package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.group.application.GroupJoinFacade;
import com.jxx.xuni.group.application.GroupManagingService;
import com.jxx.xuni.group.dto.request.GroupTaskForm;
import com.jxx.xuni.group.dto.response.GroupApiSimpleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupManagingController {

    private final GroupManagingService groupManagingService;
    private final GroupJoinFacade groupJoinFacade;

    @PostMapping("/groups/{group-id}/join")
    public ResponseEntity<GroupApiSimpleResult> join(@PathVariable("group-id") Long groupId,
                                                     @AuthenticatedMember MemberDetails memberDetails) {

        groupJoinFacade.join(memberDetails, groupId);
        return ResponseEntity.ok(GroupApiSimpleResult.join());
    }

    @PatchMapping("/groups/{group-id}/leave")
    public ResponseEntity<GroupApiSimpleResult> leave(@PathVariable("group-id") Long groupId,
                                                     @AuthenticatedMember MemberDetails memberDetails) {

        groupManagingService.leave(memberDetails, groupId);
        return ResponseEntity.ok(GroupApiSimpleResult.leave());
    }

    @PatchMapping("/groups/{group-id}/closing-recruitment")
    public ResponseEntity<GroupApiSimpleResult> close(@PathVariable ("group-id") Long groupId,
                                                      @AuthenticatedMember MemberDetails memberDetails) {

        groupManagingService.closeRecruitment(memberDetails, groupId);
        return ResponseEntity.ok(GroupApiSimpleResult.closeRecruitment());
    }

    @PostMapping("/groups/{group-id}/start")
    public ResponseEntity<GroupApiSimpleResult> start(@PathVariable ("group-id") Long groupId,
                                                      @AuthenticatedMember MemberDetails memberDetails,
                                                      @RequestBody List<GroupTaskForm> studyCheckForms) {

        groupManagingService.start(groupId, memberDetails, studyCheckForms);
        return ResponseEntity.ok(GroupApiSimpleResult.start());
    }

    @PatchMapping("/groups/{group-id}/chapters/{chapter-id}")
    public ResponseEntity<GroupApiSimpleResult> check(@PathVariable ("group-id") Long groupId,
                                                      @PathVariable ("chapter-id") Long chapterId,
                                                      @AuthenticatedMember MemberDetails memberDetails) {

        groupManagingService.doTask(memberDetails, groupId, chapterId);
        return ResponseEntity.ok(GroupApiSimpleResult.check());
    }
}
