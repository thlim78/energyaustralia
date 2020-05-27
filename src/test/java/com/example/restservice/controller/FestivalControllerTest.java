package com.example.restservice.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.restservice.exception.ThrottledException;
import com.example.restservice.model.Band;
import com.example.restservice.model.Festival;
import com.example.restservice.model.Record;
import com.example.restservice.service.FestivalService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(FestivalController.class)
public class FestivalControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FestivalService festivalService;

	private final String getRecordsUrl = "/api/v1/records";

	@Test
	public void getRecordShouldReturnMusicFestiveData() throws Exception {
		List<Record> records = new ArrayList<>();
		Record record1 = new Record();
		record1.setRecordLabel("1");
		Band bandX = new Band();
		bandX.setName("X");
		bandX.getFestivals().add(new Festival("Omega Festival"));
		Band bandY = new Band();
		bandX.setName("Y");
		bandY.getFestivals().add(new Festival(null));
		record1.getBands().add(bandX);
		record1.getBands().add(bandY);
		records.add(record1);

		Record record2 = new Record();
		record2.setRecordLabel("2");
		Band bandA = new Band();
		bandA.setName("A");
		bandA.getFestivals().add(new Festival("Alpha123 Festival"));
		bandA.getFestivals().add(new Festival("Alpha345 Festival"));
		bandA.getFestivals().add(new Festival("Beta Festival"));
		record2.getBands().add(bandA);
		records.add(record2);

		when(festivalService.getRecords()).thenReturn(records);

		mockMvc.perform(get(getRecordsUrl)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(records.size()))
				.andExpect(jsonPath("$[0].recordLabel").value(record1.getRecordLabel()))
				.andExpect(jsonPath("$[0].bands", hasSize(record1.getBands().size())))
				.andExpect(jsonPath("$[1].recordLabel").value(record2.getRecordLabel()))
				.andExpect(jsonPath("$[1].bands", hasSize(record2.getBands().size())))
				.andExpect(jsonPath("$[1].bands[0].festivals", hasSize(bandA.getFestivals().size())));

		verify(festivalService).getRecords();
	}

	@Test
	public void getRecordShouldResponseToThrottledException() throws Exception {
		when(festivalService.getRecords()).thenThrow(new ThrottledException("Max throttle limit reached"));

		mockMvc.perform(get(getRecordsUrl)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isTooManyRequests())
				.andExpect(jsonPath("$.message").value("Max throttle limit reached"));
	}
}
