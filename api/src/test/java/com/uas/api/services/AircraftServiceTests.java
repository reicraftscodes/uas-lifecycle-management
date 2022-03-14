package com.uas.api.services;

import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.PlatformStatusAndroidFullDTO;
import com.uas.api.models.dtos.PlatformStatusDTO;
import com.uas.api.models.dtos.UserAircraftDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.AircraftUser;
import com.uas.api.models.entities.AircraftUserKey;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.AircraftRepository;
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

    @Mock
    private AircraftRepository aircraftRepository;

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

    @Test
    public void givenGetPlatformStatus_ThenReturn2PlatformStatusDTOs() {
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
        List<Aircraft> aircraft = new ArrayList<>();
        aircraft.add(aircraftOne);
        aircraft.add(aircraftTwo);

        when(aircraftRepository.findAll()).thenReturn(aircraft);

        List<PlatformStatusDTO> platformStatusDTOList = aircraftService.getPlatformStatus();

        assertEquals("Should return 2 platform status dtos", 2, platformStatusDTOList.size());
        assertEquals("Should return tail number G-001", "G-001", platformStatusDTOList.get(0).getTailNumber());
        assertEquals("Should return tail number G-002", "G-002", platformStatusDTOList.get(1).getTailNumber());
    }

    @Test
    public void whenAircraftOfAllStatusExistThenAllShouldBeReturned() {
        Aircraft aircraftOne = new Aircraft(
                "M-004",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftTwo = new Aircraft(
                "M-003",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.DESIGN,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftThree = new Aircraft(
                "M-002",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.REPAIR,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftFour = new Aircraft(
                "M-001",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.PRODUCTION,
                PlatformType.PLATFORM_A,
                250);
        List<Aircraft> repairs = new ArrayList<>();
        repairs.add(aircraftThree);
        List<Aircraft> production = new ArrayList<>();
        production.add(aircraftFour);
        List<Aircraft> design = new ArrayList<>();
        design.add(aircraftTwo);
        List<Aircraft> operational = new ArrayList<>();
        operational.add(aircraftOne);
        when(aircraftRepository.findAircraftsByPlatformStatus(PlatformStatus.REPAIR)).thenReturn(repairs);
        when(aircraftRepository.findAircraftsByPlatformStatus(PlatformStatus.DESIGN)).thenReturn(design);
        when(aircraftRepository.findAircraftsByPlatformStatus(PlatformStatus.OPERATION)).thenReturn(operational);
        when(aircraftRepository.findAircraftsByPlatformStatus(PlatformStatus.PRODUCTION)).thenReturn(production);

        PlatformStatusAndroidFullDTO mockList = aircraftService.getPlatformStatusAndroid();
        assertEquals("Should have length of 1", 1, mockList.getOperational().size());
    }
}
