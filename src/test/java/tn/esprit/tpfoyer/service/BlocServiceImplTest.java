package tn.esprit.tpfoyer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BlocServiceImplTest {
    @Mock
    private BlocRepository blocRepository; // Mocking the repository
    @InjectMocks
    private BlocServiceImpl blocService;
    @Test
    void testRetrieveAllBlocs() {
        List<Bloc> mockBlocs = List.of(
                Bloc.builder().idBloc(2).nomBloc("blac a").capaciteBloc(30).build(),
                Bloc.builder().idBloc(3).nomBloc("blac b").capaciteBloc(40).build()
        );
        // Mock the repository behavior
        when(blocRepository.findAll()).thenReturn(mockBlocs);
        // Act: Call the service method
        List<Bloc> result = blocService.retrieveAllBlocs();
        // Assert: Verify the results
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("blac a", result.get(0).getNomBloc());
        Assertions.assertEquals(30, result.get(0).getCapaciteBloc());
        Assertions.assertEquals("blac b", result.get(1).getNomBloc());
        Assertions.assertEquals(40, result.get(1).getCapaciteBloc());
        // Verify interactions with the repository
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBlocsSelonCapacite() {
        // Mocking a list of blocs with varying capacities
        List<Bloc> mockBlocs = List.of(
                Bloc.builder().idBloc(1).nomBloc("Bloc A").capaciteBloc(30).build(),
                Bloc.builder().idBloc(2).nomBloc("Bloc B").capaciteBloc(50).build(),
                Bloc.builder().idBloc(3).nomBloc("Bloc C").capaciteBloc(20).build()
        );

        // Mocking the repository's findAll() method to return the mockBlocs
        when(blocRepository.findAll()).thenReturn(mockBlocs);

        // Act: Call the service method with a capacity of 30
        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(30);

        // Assert: Verify the size and contents of the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());  // Should return 2 blocs (Bloc A and Bloc B)
        Assertions.assertEquals("Bloc A", result.get(0).getNomBloc());
        Assertions.assertEquals(30, result.get(0).getCapaciteBloc());
        Assertions.assertEquals("Bloc B", result.get(1).getNomBloc());
        Assertions.assertEquals(50, result.get(1).getCapaciteBloc());

        // Verify interactions with the repository
        verify(blocRepository, times(1)).findAll();
    }




    @Test
    void addBloc() {
        Bloc mockBloc = Bloc.builder().idBloc(2).nomBloc("bloc a").capaciteBloc(30).build();
        // Mock the repository behavior
        when(blocRepository.save(mockBloc)).thenReturn(mockBloc);

        Bloc result = blocService.addBloc(mockBloc);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("bloc a", result.getNomBloc());
    }

    @Test
    void modifyBloc() {
        Bloc mockBloc = Bloc.builder().idBloc(2).nomBloc("bloc a").capaciteBloc(30).build();
        // Mock the repository behavior
        when(blocRepository.save(mockBloc)).thenReturn(mockBloc);

        Bloc result = blocService.modifyBloc(mockBloc);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("bloc a", result.getNomBloc());
    }


}