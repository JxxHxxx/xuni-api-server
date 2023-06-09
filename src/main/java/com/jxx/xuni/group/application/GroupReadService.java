package com.jxx.xuni.group.application;

import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.domain.Task;
import com.jxx.xuni.group.dto.response.*;
import com.jxx.xuni.group.query.GroupReadRepository;
import com.jxx.xuni.group.query.dynamic.GroupSearchCondition;
import com.jxx.xuni.common.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class GroupReadService {

    private final GroupReadRepository groupReadRepository;

    public List<GroupReadAllResponse> readAll() {
        List<Group> groups = groupReadRepository.readAllWithFetch();

        return groups.stream().map(group -> new GroupReadAllResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getStudy(),
                group.getTime(),
                group.getPeriod())).toList();
    }

    @Transactional
    public GroupReadOneResponse readOne(Long groupId, Long userId) {
        Group group = groupReadRepository.readOneWithFetch(groupId).get();
        group.updateGroupMemberLastVisitedTime(userId);

        List<GroupMemberDto> groupMembers = group.getGroupMembers().stream()
                .map(gm -> new GroupMemberDto(gm.getGroupMemberId(), gm.getGroupMemberName(), gm.getIsLeft(), gm.getLastVisitedTime()))
                .toList();

        return new GroupReadOneResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getStudy(),
                group.getTime(),
                group.getPeriod(),
                groupMembers);
    }

    public List<GroupReadAllResponse> readByCategory(Category category) {
        List<Group> groups = groupReadRepository.readByCategoryWithFetch(category);

        return groups.stream().map(group -> new GroupReadAllResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getStudy(),
                group.getTime(),
                group.getPeriod())).toList();
    }

    public Page<GroupAllQueryResponse> searchGroup(GroupSearchCondition condition, Pageable pageable) {
        condition.nullHandle();
        return groupReadRepository.searchGroup(condition, pageable);
    }

    public List<GroupAllQueryResponse> readOwn(Long groupMemberId) {
        return groupReadRepository.readOwnWithFetch(groupMemberId);
    }

    public List<GroupStudyCheckResponse> readGroupTask(Long groupId, Long userId) {
        Group group = groupReadRepository.readTaskWithFetch(groupId, userId)
                .orElseThrow(() -> new IllegalArgumentException(BAD_REQUEST));
        List<Task> tasks = group.receiveTasksOf(userId);

        return tasks.stream().map(s -> new GroupStudyCheckResponse(
                s.getChapterId(),
                s.getTitle(),
                s.isDone())).toList();
    }
}
