package com.colsubsidio.pricesapi;

import com.colsubsidio.pricesapi.application.PriceManagerService;
import com.colsubsidio.pricesapi.common.DateUtils;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.domain.*;
import com.colsubsidio.pricesapi.infrastructure.PricesController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.colsubsidio.pricesapi.common.EnvironmentService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    private void execute(String dateApply, Date fechaAplicacion, List<PriceEntity> lista, Resultado resultado) throws Exception {
        var expectedRequest = PriceRequestDto.instance(cadenaId, productoId, fechaAplicacion);

        when(environmentService.getApiKey()).thenReturn(ApiKey);

        when(
                pricesRepository
                .findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(cadenaId, productoId, fechaAplicacion, fechaAplicacion))
            .thenReturn(lista);

        when(priceManagerService.getPriceFinal(expectedRequest)).thenReturn(resultado);

        String expectedJsonResponse = objectMapper.writeValueAsString(resultado);

        mockMvc.perform(MockMvcRequestBuilders.get("/prices/{cadenaId}/{productoId}", cadenaId, productoId)
                        .header("ApiKey", ApiKey)
                        .param("dateApply", dateApply)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }

    @Test
    public void testGetPriceFinal() throws Exception {
        int tarifa = 2;
        short priority = 1;
        var dateApply = "2022-06-14 10:00:00";
        var lista = new ArrayList<PriceEntity>();
        var precioFinal = BigDecimal.valueOf(25.45);
        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        String fechaAplicacionEnIso = DateUtils.toISO(fechaAplicacion);
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaInicio = DateUtils.formatDate("2020-06-15 00:00:00", "yyyy-MM-dd HH:mm:ss");

        var priceEntity = PriceEntity.getInstance(cadenaId, productoId, priority, fechaInicio, fechaFin);

        lista.add(priceEntity);

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(tarifa)
                .fechasAplicacion(fechaAplicacionEnIso)
                .precioFinal(precioFinal)
                .build();

        var resultado =  Resultado.instance(HttpStatus.OK.value(),true,  expectedResponse);

        execute(dateApply, fechaAplicacion, lista, resultado);
    }

    @Test
    public void testGetPriceFInal2() throws Exception {
        int tarifa = 2;
        short priority = 1;
        var dateApply = "2022-06-14 21:00:00";
        var lista = new ArrayList<PriceEntity>();
        var precioFinal = BigDecimal.valueOf(25.45);
        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        String fechaAplicacionEnIso = DateUtils.toISO(fechaAplicacion);
        Date fechaInicio = DateUtils.formatDate("2020-06-14 15:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");

        var priceEntity = PriceEntity.getInstance(cadenaId, productoId, priority, fechaInicio, fechaFin);

        lista.add(priceEntity);

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(tarifa)
                .fechasAplicacion(fechaAplicacionEnIso)
                .precioFinal(precioFinal)
                .build();

        var resultado =  Resultado.instance(HttpStatus.OK.value(),true,  expectedResponse);

        execute(dateApply, fechaAplicacion, lista, resultado);
    }

    @Test
    public void testGetPriceFInal3() throws Exception {
        int tarifa = 1;
        short priority = 1;
        var dateApply = "2022-06-14 21:00:00";
        var lista = new ArrayList<PriceEntity>();
        var precioFinal = BigDecimal.valueOf(35.50);
        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        String fechaAplicacionEnIso = DateUtils.toISO(fechaAplicacion);
        Date fechaInicio = DateUtils.formatDate("2020-06-15 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");

        var priceEntity = PriceEntity.getInstance(cadenaId, productoId, priority, fechaInicio, fechaFin);

        lista.add(priceEntity);

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(tarifa)
                .fechasAplicacion(fechaAplicacionEnIso)
                .precioFinal(precioFinal)
                .build();

        var resultado =  Resultado.instance(HttpStatus.OK.value(),true,  expectedResponse);

        execute(dateApply, fechaAplicacion, lista, resultado);
    }

    @Test
    public void testGetPriceFinal4() throws Exception {
        int tarifa = 1;
        short priority = 1;
        var dateApply = "2022-06-15 10:00:00";
        var lista = new ArrayList<PriceEntity>();
        var precioFinal = BigDecimal.valueOf(30.50);
        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        String fechaAplicacionEnIso = DateUtils.toISO(fechaAplicacion);
        Date fechaInicio = DateUtils.formatDate("2020-06-15 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");

        var priceEntity = PriceEntity.getInstance(cadenaId, productoId, priority, fechaInicio, fechaFin);

        lista.add(priceEntity);

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(tarifa)
                .fechasAplicacion(fechaAplicacionEnIso)
                .precioFinal(precioFinal)
                .build();

        var resultado =  Resultado.instance(HttpStatus.OK.value(),true,  expectedResponse);

        execute(dateApply, fechaAplicacion, lista, resultado);
    }
    @Test
    public void testGetPriceFinal5() throws Exception {
        int tarifa = 4;
        short priority = 1;
        var dateApply = "2022-06-16 21:00:00";
        var lista = new ArrayList<PriceEntity>();
        var precioFinal = BigDecimal.valueOf(38.95);
        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        String fechaAplicacionEnIso = DateUtils.toISO(fechaAplicacion);
        Date fechaInicio = DateUtils.formatDate("2020-06-15 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fechaFin = DateUtils.formatDate("2020-06-15 11:00:00", "yyyy-MM-dd HH:mm:ss");

        var priceEntity = PriceEntity.getInstance(cadenaId, productoId, priority, fechaInicio, fechaFin);

        lista.add(priceEntity);

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(tarifa)
                .fechasAplicacion(fechaAplicacionEnIso)
                .precioFinal(precioFinal)
                .build();

        var resultado =  Resultado.instance(HttpStatus.OK.value(),true,  expectedResponse);

        execute(dateApply, fechaAplicacion, lista, resultado);
    }
}
