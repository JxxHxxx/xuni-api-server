package com.jxx.xuni.statistics.application;

import com.jxx.xuni.common.event.connector.ReviewCreatedConnector;
import com.jxx.xuni.common.event.trigger.GroupAccessedEvent;
import com.jxx.xuni.common.event.trigger.StatisticsAccessedEvent;
import com.jxx.xuni.statistics.domain.MemberStatistics;
import com.jxx.xuni.statistics.domain.MemberStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberStatisticsNotifier {

    private final MemberStatisticsRepository memberStatisticsRepository;
    private final ReviewCreatedConnector reviewCreatedConnector;

    @EventListener(StatisticsAccessedEvent.class)
    public void handle(StatisticsAccessedEvent event) {
        int progress = reviewCreatedConnector.receive(GroupAccessedEvent.by(event));
        Optional<MemberStatistics> optionalMemberStatistics = memberStatisticsRepository.readBy(event.memberId(), event.studyProductId());

        if (optionalMemberStatistics.isEmpty()) {
            MemberStatistics statistics = new MemberStatistics(event.memberId(), event.studyProductId(), progress);
            memberStatisticsRepository.save(statistics);
        }

        if (optionalMemberStatistics.isPresent()) {
            MemberStatistics memberStatistics = optionalMemberStatistics.get();
            memberStatistics.updateProgress(progress);
        }
    }
}