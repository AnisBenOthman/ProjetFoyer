package tn.esprit.tpfoyer.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
    void testRetriveBloc_success(){
        long blocId = 1;
        Bloc mockBloc = Bloc.builder().idBloc(1).nomBloc("test blanc").build(); // Assuming a Bloc class exists
        // Mock the repository behavior
        when(blocRepository.existsById(blocId)).thenReturn(true);
        when(blocRepository.findById(blocId)).thenReturn(Optional.ofNullable(mockBloc));
        // Act: Call the method under test
        Bloc result = blocService.retrieveBloc(blocId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(blocId,result.getIdBloc());
        Assertions.assertEquals("test blanc",result.getNomBloc());
        // Assert: Verify the repository's existsById method was called
        verify(blocRepository, times(1)).existsById(blocId);
        verify(blocRepository,times(1)).findById(blocId);


    }
    @Test
    void testRetriveBloc_failed(){
        long blocId = 99;
        when(blocRepository.existsById(blocId)).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() -> blocService.retrieveBloc(blocId));
        Assertions.assertEquals("Bloc not found with ID: 99",exception.getMessage());
        verify(blocRepository, never()).findById(blocId);

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


    @Test
    void testDeleteBloc_Success() {
        // Arrange: Mock the repository behavior
        long blocId = 1;
        when(blocRepository.existsById(blocId)).thenReturn(true);

        // Act: Call the method under test
        blocService.removeBloc(blocId);

        // Assert: Verify the repository's deleteById method was called
        verify(blocRepository, times(1)).deleteById(blocId);
    }

    @Test
    void testDeleteBloc_BlocNotFound() {
        // Arrange: Mock the repository behavior
        long blocId = 99;
        when(blocRepository.existsById(blocId)).thenReturn(false);

        // Act & Assert: Check that an exception is thrown
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> blocService.removeBloc(blocId)
        );

        assertEquals("Bloc not found with ID: 99", exception.getMessage());

        // Verify that deleteById is never called
        verify(blocRepository, never()).deleteById(blocId);
    }
    @Test
    void testTrouverBlocsSansFoyer(){
List<Bloc> mockBlocs = List.of(Bloc.builder().idBloc(1).foyer(null).build(),
        Bloc.builder().idBloc(2).foyer(null).build());
when(blocRepository.findAllByFoyerIsNull()).thenReturn(mockBlocs);
List<Bloc> result = blocService.trouverBlocsSansFoyer();
Assertions.assertNotNull(result);
Assertions.assertEquals(1,result.get(0).getIdBloc());
verify(blocRepository,times(1)).findAllByFoyerIsNull();
    }
    @Test
    void testTrouverBlocsParNomEtCap_success(){
       List<Bloc> mockBlocs = List.of(Bloc.builder().nomBloc("bloc A").capaciteBloc(200).build(),
               Bloc.builder().nomBloc("bloc A").capaciteBloc(200).build());
        when((blocRepository.findAllByNomBlocAndCapaciteBloc("bloc A",200))).thenReturn(mockBlocs);
        List<Bloc> result = blocService.trouverBlocsParNomEtCap("bloc A", 200);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(bloc -> bloc.getCapaciteBloc() == 200 && bloc.getNomBloc().equals("bloc A")));
        // Verify repository interaction
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("bloc A", 200);
    }
    @Test
    void testTrouverBlocsParNomEtCap_NoResults() {
        // Arrange: Mock repository behavior
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc Z", 200)).thenReturn(List.of());

        // Act: Call the service method
        List<Bloc> result = blocService.trouverBlocsParNomEtCap("Bloc Z", 200);

        // Assert: Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Should return an empty list

        // Verify repository interaction
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("Bloc Z", 200);
    }

}


