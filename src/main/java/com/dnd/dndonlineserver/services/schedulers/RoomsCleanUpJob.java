package com.dnd.dndonlineserver.services.schedulers;

import com.dnd.dndonlineserver.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoomsCleanUpJob {
    private final RoomService roomService;

    @Scheduled(cron = "0 * * ? * *")
    public void cleanUpEmptyRooms() {
        roomService.cleanUpEmptyRooms();
    }
}
