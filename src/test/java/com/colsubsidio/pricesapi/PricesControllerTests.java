package com.colsubsidio.pricesapi;

import com.colsubsidio.pricesapi.application.PriceManagerService;
import com.colsubsidio.pricesapi.common.DateUtils;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.domain.PriceEntity;
import com.colsubsidio.pricesapi.domain.PriceRequestDto;
import com.colsubsidio.pricesapi.domain.PriceResponseDto;
import com.colsubsidio.pricesapi.domain.PricesRepository;
import com.colsubsidio.pricesapi.infrastructure.PricesController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.colsubsidio.pricesapi.common.EnvironmentService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(PricesController.class)
@AutoConfigureMockMvc
public class PricesControllerTests {
    private final long cadenaId = 1;
    private final long productoId = 35455;
    private final String ApiKey = "3c4504c4-72b2-11ee-b962-0242ac120002";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvironmentService environmentService;

    @MockBean
    private LogsManager logsManager;

    @MockBean
    private PriceManagerService priceManagerService;

    @MockBean
    private PricesRepository pricesRepository;

    @Test
    public void testGetPriceFinal() throws Exception {
        String dateApply = "2022-06-14 10:00:00";

        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        Date fechaInicio = DateUtils.formatDate("2020-06-15 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");

        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setBrandId(cadenaId);
        priceEntity.setProductId(productoId);
        priceEntity.setPriority((short) 1);
        priceEntity.setStartDate(fechaInicio);
        priceEntity.setEndDate(fechaFin);

        var lista = new ArrayList<PriceEntity>();
        lista.add(priceEntity);

        PriceRequestDto expectedRequest = PriceRequestDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .fechaAplicacion(fechaAplicacion)
                .build();

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(2)
                .fechasAplicacion(DateUtils.toISO(fechaAplicacion))
                .precioFinal(BigDecimal.valueOf(25.45))
                .build();

        when(environmentService.getApiKey()).thenReturn(ApiKey);

        when(
                pricesRepository
                        .findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(cadenaId, productoId, fechaAplicacion, fechaAplicacion))
                .thenReturn(lista);

        when(priceManagerService.findPriceFinal(expectedRequest)).thenReturn(expectedResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/prices/{cadenaId}/{productoId}/query", cadenaId, productoId)
                        .header("ApiKey", ApiKey)
                        .param("dateApply", dateApply)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }

    @Test
    public void testGetPriceFInal2() throws Exception {
        String dateApply = "2022-06-14 21:00:00";

        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        Date fechaInicio = DateUtils.formatDate("2020-06-14 15:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");

        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setBrandId(cadenaId);
        priceEntity.setProductId(productoId);
        priceEntity.setPriority((short) 1);
        priceEntity.setStartDate(fechaInicio);
        priceEntity.setEndDate(fechaFin);

        var lista = new ArrayList<PriceEntity>();
        lista.add(priceEntity);

        PriceRequestDto expectedRequest = PriceRequestDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .fechaAplicacion(fechaAplicacion)
                .build();

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(2)
                .fechasAplicacion(DateUtils.toISO(fechaAplicacion))
                .precioFinal(BigDecimal.valueOf(25.45))
                .build();

        when(environmentService.getApiKey()).thenReturn(ApiKey);

        when(
                pricesRepository
                        .findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(cadenaId, productoId, fechaAplicacion, fechaAplicacion))
                .thenReturn(lista);

        when(priceManagerService.findPriceFinal(expectedRequest)).thenReturn(expectedResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/prices/{cadenaId}/{productoId}/query", cadenaId, productoId)
                        .header("ApiKey", ApiKey)
                        .param("dateApply", dateApply)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }

    @Test
    public void testGetPriceFInal3() throws Exception {
        String dateApply = "2022-06-14 21:00:00";

        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        Date fechaInicio = DateUtils.formatDate("2020-06-15 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");

        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setBrandId(cadenaId);
        priceEntity.setProductId(productoId);
        priceEntity.setPriority((short) 1);
        priceEntity.setStartDate(fechaInicio);
        priceEntity.setEndDate(fechaFin);

        var lista = new ArrayList<PriceEntity>();
        lista.add(priceEntity);

        PriceRequestDto expectedRequest = PriceRequestDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .fechaAplicacion(fechaAplicacion)
                .build();

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(1)
                .fechasAplicacion(DateUtils.toISO(fechaAplicacion))
                .precioFinal(BigDecimal.valueOf(35.50))
                .build();

        when(environmentService.getApiKey()).thenReturn(ApiKey);

        when(
                pricesRepository
                        .findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(cadenaId, productoId, fechaAplicacion, fechaAplicacion))
                .thenReturn(lista);

        when(priceManagerService.findPriceFinal(expectedRequest)).thenReturn(expectedResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/prices/{cadenaId}/{productoId}/query", cadenaId, productoId)
                        .header("ApiKey", ApiKey)
                        .param("dateApply", dateApply)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }

    @Test
    public void testGetPriceFinal4() throws Exception {
        String dateApply = "2022-06-15 10:00:00";
        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        Date fechaInicio = DateUtils.formatDate("2020-06-15 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");
        var lista = new ArrayList<PriceEntity>();

        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setBrandId(cadenaId);
        priceEntity.setProductId(productoId);
        priceEntity.setPriority((short) 1);
        priceEntity.setStartDate(fechaInicio);
        priceEntity.setEndDate(fechaFin);

        lista.add(priceEntity);


        PriceRequestDto expectedRequest = PriceRequestDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .fechaAplicacion(fechaAplicacion)
                .build();


        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(1)
                .fechasAplicacion(DateUtils.toISO(fechaAplicacion))
                .precioFinal(BigDecimal.valueOf(30.50))
                .build();

        when(environmentService.getApiKey()).thenReturn(ApiKey);

        when(
                pricesRepository
                    .findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(cadenaId, productoId, fechaAplicacion, fechaAplicacion))
                    .thenReturn(lista);

        when(priceManagerService.findPriceFinal(expectedRequest)).thenReturn(expectedResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/prices/{cadenaId}/{productoId}/query", cadenaId, productoId)
                        .header("ApiKey", ApiKey)
                        .param("dateApply", dateApply)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }
    @Test
    public void testGetPriceFinal5() throws Exception {
        String dateApply = "2022-06-16 21:00:00";

        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        Date fechaInicio = DateUtils.formatDate("2020-06-15 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");

        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setBrandId(cadenaId);
        priceEntity.setProductId(productoId);
        priceEntity.setPriority((short) 1);
        priceEntity.setStartDate(fechaInicio);
        priceEntity.setEndDate(fechaFin);

        var lista = new ArrayList<PriceEntity>();
        lista.add(priceEntity);

        PriceRequestDto expectedRequest = PriceRequestDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .fechaAplicacion(fechaAplicacion)
                .build();

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(4)
                .fechasAplicacion(DateUtils.toISO(fechaAplicacion))
                .precioFinal(BigDecimal.valueOf(38.95))
                .build();

        when(environmentService.getApiKey()).thenReturn(ApiKey);

        when(
                pricesRepository
                        .findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(cadenaId, productoId, fechaAplicacion, fechaAplicacion))
                .thenReturn(lista);

        when(priceManagerService.findPriceFinal(expectedRequest)).thenReturn(expectedResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/prices/{cadenaId}/{productoId}/query", cadenaId, productoId)
                        .header("ApiKey", ApiKey)
                        .param("dateApply", dateApply)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }
}
