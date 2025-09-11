package de.oetting.bgp.gamingevent.service;

import de.oetting.bgp.gamingevent.entity.GamingEventEntity;
import de.oetting.bgp.gamingevent.entity.GamingEventRepository;
import de.oetting.bgp.gamingevent.entity.Schedule;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Arrays;

@Service
public class RecurringGamingEventService {

    private static final Logger LOG = LoggerFactory.getLogger(RecurringGamingEventService.class);

    @Autowired
    private GamingEventRepository gamingEventRepository;

    @Autowired
    private GamingEventService gamingEventService;

    @Scheduled(cron = "0 * * * * *") // run every hour
    @Transactional
    public void createPastSingularEventsFromRecurringEvents() {
        LOG.info("Checking if singular events in the past have to be created");

        gamingEventRepository.findByScheduleIn(Arrays.asList(Schedule.WEEKLY, Schedule.MONTHLY))
                .forEach(this::createNewSingularEvent);
    }

    private void createNewSingularEvent(GamingEventEntity eventSchedule) {
        // Check if startDate of the recurring event plus schedule time is in the past.
        // If yes, a single event needs to be created, if it does not already exist.
        // During each run, at most one event will be created. So if a recurring event is created
        if (eventSchedule.getSchedule() == Schedule.WEEKLY) {
            var nextEventStart = eventSchedule.getStart().plusWeeks(1);
            if (nextEventStart.isBefore(ZonedDateTime.now())) {
                processNewStartScheduleForRecurringEvent(eventSchedule, nextEventStart);
            }
        } else if (eventSchedule.getSchedule() == Schedule.MONTHLY) {
            var nextEventStart = eventSchedule.getStart().plusMonths(1);
            if (nextEventStart.isBefore(ZonedDateTime.now())) {
                processNewStartScheduleForRecurringEvent(eventSchedule, nextEventStart);
            }
        } else {
            LOG.error("Unknown schedule {} for event with id {}", eventSchedule.getSchedule(), eventSchedule.getId());
        }
    }

    private void processNewStartScheduleForRecurringEvent(GamingEventEntity eventSchedule, ZonedDateTime nextEventStart) {
        eventSchedule.setStart(nextEventStart);
        if (gamingEventRepository.findByGameGroupIdAndScheduleAndStart(eventSchedule.getGameGroup().getId(), Schedule.ONCE, nextEventStart).isEmpty()) {
            LOG.info("Group {}: Creating new event starting at {}", eventSchedule.getGameGroup().getName(), nextEventStart);
            GamingEventEntity clonedEvent = gamingEventService.cloneEvent(eventSchedule);
            clonedEvent.setStart(nextEventStart);
            clonedEvent.setSchedule(Schedule.ONCE);
            clonedEvent.setParent(eventSchedule);
        }
    }
}
