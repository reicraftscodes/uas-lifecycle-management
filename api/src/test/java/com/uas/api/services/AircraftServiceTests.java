package com.uas.api.services;

import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.UserAircraftDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.AircraftUser;
import com.uas.api.models.entities.AircraftUserKey;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.AircraftUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AircraftServiceTests {

    @Mock
    private AircraftUserRepository aircraftUserRepository;

    @Autowired
    @InjectMocks
    private AircraftServiceImpl aircraftService;

    @Test
    public void givenGetAllAircraftForUser_ThenReturn2AircraftDTOs() {
        User user = new User();
        user.setId(2l);
        Aircraft aircraftOne = new Aircraft(
                "G-001",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftTwo = new Aircraft(
                "G-002",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_B,
                300);
        List<AircraftUser> aircraftUsers = new ArrayList<>();
        aircraftUsers.add(new AircraftUser(new AircraftUserKey(), user, aircraftOne, 120L));
        aircraftUsers.add(new AircraftUser(new AircraftUserKey(), user, aircraftTwo, 60L));

        when(aircraftUserRepository.findAllByUser_Id(anyLong())).thenReturn(aircraftUsers);

        List<UserAircraftDTO> userAircraftDTOs = aircraftService.getAircraftForUser(2);

        assertEquals("Should return 2 user aircraft dtos", 2, userAircraftDTOs.size());
        assertEquals("Should return tail number G-001", "G-001", userAircraftDTOs.get(0).getTailNumber());
        assertEquals("Should return tail number G-002", "G-002", userAircraftDTOs.get(1).getTailNumber());
    }
}
