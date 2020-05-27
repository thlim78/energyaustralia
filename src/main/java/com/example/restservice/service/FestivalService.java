package com.example.restservice.service;

import com.example.restservice.model.Band;
import com.example.restservice.model.Festival;
import com.example.restservice.model.Record;
import com.example.restservice.model.provider.MusicFestival;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class FestivalService {
    @Value("${api.url.address}")
    private String apiURLAddress;

    private final RestTemplate restTemplate;
    private final String getFestiveDataURL = "/festivals";
    private final Comparator<Festival> compareByFestivalName = (Festival o1, Festival o2) -> o1.getName().compareTo( o2.getName() );
    private final Comparator<Band> compareByBandName = (Band o1, Band o2) -> o1.getName().compareTo( o2.getName() );
    private final Comparator<Record> compareByRecordLabel = (Record o1, Record o2) -> o1.getRecordLabel().compareTo( o2.getRecordLabel() );

    public FestivalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Record> getRecords() {
        String url = apiURLAddress + getFestiveDataURL;
        ResponseEntity<MusicFestival[]> response = restTemplate.getForEntity(url, MusicFestival[].class);

        MusicFestival[] musicFestivalArrays = response.getBody();
        List<MusicFestival> musicFestivals = Arrays.asList(musicFestivalArrays);

        List<Record> records = new ArrayList<>();
        musicFestivals.stream().forEach(musicFestival -> {
            musicFestival.getBands().stream().forEach(it -> {
                if (records.stream().noneMatch(i -> i.getRecordLabel().equalsIgnoreCase(it.getRecordLabel()))) {
                    Record record = new Record();
                    record.setRecordLabel(it.getRecordLabel());
                    Band band = new Band();
                    band.setName(it.getName());
                    band.getFestivals().add(new Festival(musicFestival.getName()));
                    Collections.sort(band.getFestivals(), compareByFestivalName);
                    record.getBands().add(band);
                    Collections.sort(record.getBands(), compareByBandName);
                    records.add(record);
                } else {
                    Record record = records.stream().filter(i-> i.getRecordLabel().equalsIgnoreCase(it.getRecordLabel())).findFirst().get();
                    if (record.getBands().stream().noneMatch(i -> i.getName().equalsIgnoreCase(it.getName()))) {
                        Band band = new Band();
                        band.setName(it.getName());
                        band.getFestivals().add(new Festival(musicFestival.getName()));
                        Collections.sort(band.getFestivals(), compareByFestivalName);
                        record.getBands().add(band);
                    } else {
                        Band band = record.getBands().stream().filter(i-> i.getName().equalsIgnoreCase(it.getName())).findFirst().get();
                        band.getFestivals().add(new Festival(musicFestival.getName()));
                        Collections.sort(band.getFestivals(), compareByFestivalName);
                    }
                    Collections.sort(record.getBands(), compareByBandName);
                }
            });
        });
        Collections.sort(records, compareByRecordLabel);
        return records;
    }

    /*
    public Map<String, Record> getRecordsInMap() {
        String url = apiURLAddress + getFestiveDataURL;
        ResponseEntity<MusicFestival[]> response = restTemplate.getForEntity(url, MusicFestival[].class);

        MusicFestival[] musicFestivalArrays = response.getBody();
        List<MusicFestival> musicFestivals = Arrays.asList(musicFestivalArrays);

        Map<String, Record> records = new TreeMap<>();
        musicFestivals.stream().forEach(musicFestival -> {
            musicFestival.getBands().stream().forEach(it -> {
                if (!records.containsKey(it.getRecordLabel())) {
                    Record record = new Record();
                    Band band = new Band();
                    band.getFestivals().add(new Festival(musicFestival.getName()));
                    Collections.sort(band.getFestivals(), compareByName);
                    record.getBands().put(it.getName(), band);
                    records.put(it.getRecordLabel(), record);
                } else {
                    Record record = records.get(it.getRecordLabel());
                    if (!record.getBands().containsKey(it.getName())) {
                        Band band = new Band();
                        band.getFestivals().add(new Festival(musicFestival.getName()));
                        Collections.sort(band.getFestivals(), compareByName);
                        record.getBands().put(it.getName(), band);
                    } else {
                        Band band = record.getBands().get(it.getName());
                        band.getFestivals().add(new Festival(musicFestival.getName()));
                        Collections.sort(band.getFestivals(), compareByName);
                    }
                }
            });
        });
        return records;
    }*/
}
