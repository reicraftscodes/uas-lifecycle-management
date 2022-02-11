package com.uas.api.services;

import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.models.entities.Location;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.PartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartServiceTests {

    @Mock
    private PartRepository partRepository;

    @Mock
    private LocationRepository locationRepository;

    @Autowired
    @InjectMocks
    private PartServiceImpl partService;

    @Test
    public void givenGetLowStockPartsReturn22Parts() {
        List<Location> locations = new ArrayList<>();
        Location locationCardiff = new Location();
        locationCardiff.setLocationName("Cardiff");
        Location locationBristol = new Location();
        locationBristol.setLocationName("Bristol");
        locations.add(locationCardiff);
        locations.add(locationBristol);

        when(locationRepository.findAll()).thenReturn(locations);
        when(partRepository.countAllByLocation_LocationNameAndPartType_PartName(any(), any())).thenReturn(30);

        List<PartStockLevelDTO> partStockLevelDTOs = partService.getPartsAtLowStock();

        assertEquals("Should return 22 parts", 22, partStockLevelDTOs.size());
    }

    @Test
    public void givenGetLowStockPartsReturn0Parts() {
        List<Location> locations = new ArrayList<>();
        Location locationCardiff = new Location();
        locationCardiff.setLocationName("Cardiff");
        Location locationBristol = new Location();
        locationBristol.setLocationName("Bristol");
        locations.add(locationCardiff);
        locations.add(locationBristol);

        when(locationRepository.findAll()).thenReturn(locations);
        when(partRepository.countAllByLocation_LocationNameAndPartType_PartName(any(), any())).thenReturn(41);

        List<PartStockLevelDTO> partStockLevelDTOs = partService.getPartsAtLowStock();

        assertEquals("Should return 0 parts", 0, partStockLevelDTOs.size());
    }
}
