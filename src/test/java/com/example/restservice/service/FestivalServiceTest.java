package com.example.restservice.service;

import com.example.restservice.config.TestRestTemplateConfig;
import com.example.restservice.exception.ThrottledException;
import com.example.restservice.model.Band;
import com.example.restservice.model.Festival;
import com.example.restservice.model.Record;
import com.example.restservice.model.provider.BandBuilder;
import com.example.restservice.model.provider.MusicFestival;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RunWith(SpringRunner.class)
@RestClientTest(FestivalService.class)
@Import(TestRestTemplateConfig.class)
public class FestivalServiceTest {

    @Autowired
    private FestivalService festivalService;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.url.address}")
    private String apiURLAddress;

    private final String getFestiveDataURL = "/festivals";

    @Test
    public void getRecordShouldReturnMusicFestiveDataInAlphabeticalOrder()
            throws Exception {
        //Given - create test data and expectation
        MusicFestival omegaFestival = new MusicFestival();
        omegaFestival.setName("Omega Festival");
        omegaFestival.getBands().add(new BandBuilder("X", "1").build());

        MusicFestival festival = new MusicFestival();
        festival.setName(null);
        festival.getBands().add(new BandBuilder("Y", "1").build());

        MusicFestival alpha123Festival = new MusicFestival();
        alpha123Festival.setName("Alpha123 Festival");
        alpha123Festival.getBands().add(new BandBuilder("A", "2").build());

        MusicFestival betaFestival = new MusicFestival();
        betaFestival.setName("Beta Festival");
        betaFestival.getBands().add(new BandBuilder("A", "2").build());

        MusicFestival alpha345Festival = new MusicFestival();
        alpha345Festival.setName("Alpha345 Festival");
        alpha345Festival.getBands().add(new BandBuilder("A", "2").build());

        List<MusicFestival> musicFestivals = new ArrayList<>();
        musicFestivals.add(omegaFestival);
        musicFestivals.add(festival);
        musicFestivals.add(alpha123Festival);
        musicFestivals.add(betaFestival);
        musicFestivals.add(alpha345Festival);

        String musicRecordsInJsonString = objectMapper.writeValueAsString(musicFestivals);

        this.server.expect(requestTo(apiURLAddress + getFestiveDataURL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(musicRecordsInJsonString, MediaType.APPLICATION_JSON));

        //When
        List<Record> actualRecords = this.festivalService.getRecords();

        //Then
        assertEquals(2, actualRecords.size());
        verifyRecordLabelInAlphabetOrder(actualRecords);
        verifyBandNameInAlphabetOrder(actualRecords.get(0).getBands());

        assertEquals(1, actualRecords.get(0).getBands().get(0).getFestivals().size());
        assertEquals("Omega Festival", actualRecords.get(0).getBands().get(0).getFestivals().get(0).getName());
        assertEquals(1, actualRecords.get(0).getBands().get(1).getFestivals().size());
        assertEquals(null, actualRecords.get(0).getBands().get(1).getFestivals().get(0).getName());

        assertEquals(1, actualRecords.get(1).getBands().size());
        assertEquals(3, actualRecords.get(1).getBands().get(0).getFestivals().size());
        verifyFestivalNameInAlphabetOrder(actualRecords.get(1).getBands().get(0).getFestivals());
    }

    @Test
    public void getRecordShouldResponseToThrottledException() {
        //Given
        this.server.expect(requestTo(apiURLAddress + getFestiveDataURL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS).body("Max throttle limit reached"));

        //When
        try {
            List<Record> actualRecords = this.festivalService.getRecords();
        } catch (ThrottledException e) {
            assertEquals("Max throttle limit reached", e.getMessage());
        }

        //Then
        this.server.verify();
    }

    private void verifyRecordLabelInAlphabetOrder(List<Record> records) {
        List<String> expectedRecordLableInAlphabetOrder = Arrays.asList("1", "2");
        assertEquals(expectedRecordLableInAlphabetOrder, records.stream().map(record -> record.getRecordLabel()).collect(Collectors.toList()));
    }

    private void verifyBandNameInAlphabetOrder(List<Band> bands) {
        List<String> expectedBandNameInAlphabetOrder = Arrays.asList("X", "Y");
        assertEquals(expectedBandNameInAlphabetOrder, bands.stream().map(band -> band.getName()).collect(Collectors.toList()));
    }

    private void verifyFestivalNameInAlphabetOrder(List<Festival> festivals) {
        List<String> expectedFestivalNameInAlphabetOrder = Arrays.asList("Alpha123 Festival", "Alpha345 Festival", "Beta Festival");
        assertEquals(expectedFestivalNameInAlphabetOrder, festivals.stream().map(it -> it.getName()).collect(Collectors.toList()));
    }
}
