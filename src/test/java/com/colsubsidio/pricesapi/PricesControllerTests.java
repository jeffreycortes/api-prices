package com.colsubsidio.pricesapi;

import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.domain.PriceResponseDto;
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

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(PricesController.class)
@AutoConfigureMockMvc
public class PricesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvironmentService environmentService;

    @MockBean
    private LogsManager logsManager;

    @Test
    public void testGetPriceFinal() throws Exception {
        String cadenaId = "cadena1";
        String productoId = "producto1";
        String dateApply = "2022-03-10";
        String ApiKey = "3c4504c4-72b2-11ee-b962-0242ac120002";

        // Crear un ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        when(environmentService.getApiKey()).thenReturn(ApiKey);

        PriceResponseDto expectedResponse = PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa("35.50")
                .fechasAplicacion(dateApply)
                .build();

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/prices/{cadenaId}/{productoId}", cadenaId, productoId)
                        .header("ApiKey", ApiKey)
                        .param("dateApply", dateApply)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }
}
