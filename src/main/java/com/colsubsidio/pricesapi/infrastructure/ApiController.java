package com.colsubsidio.pricesapi.infrastructure;

import com.colsubsidio.pricesapi.domain.Resultado;
import com.colsubsidio.pricesapi.tmp.application.DirectorioActivoService;
import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.tmp.domain.*;

import com.colsubsidio.pricesapi.domain.Error;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "APP_EMPRESAS_DIRECTORIO_ACTIVO_API")
@Controller
@CrossOrigin(origins = "*")
public class ApiController {
    @Autowired
    DirectorioActivoService directorioActivoService;

    EnvironmentService environmentService;
    private final LogsManager logsManager;
    ApiController(EnvironmentService environmentService, LogsManager logsManager) {
        this.environmentService = environmentService;
        this.logsManager = logsManager;
    }
    @GetMapping("/version")
    public String version() {
        logsManager.info("Version: ", environmentService.getVersion());
        return environmentService.getVersion();
    }
/*
    @PostMapping(value = "/empresa", produces = "application/json")
    public EmpresaResDto ConsultarEmpresa(@RequestBody EmpresaDto empresaDto) {
        logsManager.info("Se esta consultando la empresa", empresaDto);
        return directorioActivoService.consultarEmpresa(empresaDto);
    }
    @PostMapping(value = "usuarios/empresas", produces = "application/json")
    public EmpresaResDto CrearEmpresa(@Valid @RequestBody ApigeeCrearEmpresaReqDto empresaAggregateDto) {
        logsManager.info("Se esta creando la empresa: ", empresaAggregateDto);
        return directorioActivoService.crearEmpresa(empresaAggregateDto);
    }

    @PostMapping(value = "/usuarios/empresas/personas", produces = "application/json")
    public UsuarioEmpresaResDto ConsultarUsuarioEmpresa(@RequestBody UsuarioEmpresaDto usuarioEmpresaDto) {
        logsManager.info("Se esta consultando el usuario por empresa: ", usuarioEmpresaDto);
        return directorioActivoService.consultarUsuarioEmpresa(usuarioEmpresaDto);
    }
    @PostMapping(value = "/usuarios/personas/empresas", produces = "application/json")
    public UsuarioEmpresaNuevoResDto crearUsuarioEmpresa(@Valid @RequestBody UsuarioEmpresaAggregateDto usuarioEmpresaDto) {
        logsManager.info("Se esta consultando el usuario por empresa: ", usuarioEmpresaDto);
        return directorioActivoService.crearUsuarioEmpresa(usuarioEmpresaDto);
    }*/
    @PostMapping(value = "/usuarios", produces = "application/json")
    @ApiOperation(value = "Registrar usuarios empresas en LDAP")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Acción exitosa"),
            @ApiResponse(code = 500, message = "Error interno de servidor", response = CuentaUsuarioEmpresaResDto.class,
                    examples = @Example(
                            value = {
                                    @ExampleProperty(mediaType = "application/json",
                                            value = ("{\n" +
                                                    "  \"resultado\": [\n" +
                                                    "    {\n" +
                                                    "      \"codigo\": \"500\",\n" +
                                                    "      \"exitoso\": false,\n" +
                                                    "      \"error\": {\n" +
                                                    "        \"codigo\": \"COL08\",\n" +
                                                    "        \"descripcion\": \"Error interno no controlado o desconocido\",\n" +
                                                    "        \"detalles\": [\n" +
                                                    "          \"exception\"\n" +
                                                    "        ]\n" +
                                                    "      },\n" +
                                                    "      \"transaccion\": null\n" +
                                                    "    }\n" +
                                                    "  ]\n" +
                                                    "}")
                                    )
                            }
                    )),
            @ApiResponse(code = 401, message = "No autorizado", response = CuentaUsuarioEmpresaResDto.class, examples = @Example(value = {@ExampleProperty(mediaType = "application/json",
                        value = ("{\n" +
                        "  \"resultado\": [\n" +
                        "    {\n" +
                        "      \"codigo\": \"401\",\n" +
                        "      \"exitoso\": false\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"))})),
            @ApiResponse(code = 400, message = "Parámetros de entrada incorrectos",
                    response = CuentaUsuarioEmpresaResDto.class,
                    examples = @Example(
                            value = {
                                    @ExampleProperty(mediaType = "application/json",
                                            value = ("{\n" +
                                                    "  \"resultado\": [\n" +
                                                    "    {\n" +
                                                    "      \"codigo\": \"400\",\n" +
                                                    "      \"exitoso\": false,\n" +
                                                    "      \"error\": {\n" +
                                                    "        \"codigo\": \"COL09\",\n" +
                                                    "        \"descripcion\": \"Formato de campo invalido o no diligenciado\",\n" +
                                                    "        \"detalles\": [\n" +
                                                    "          \"empresa.documento: Documento es requerido\"\n" +
                                                    "        ]\n" +
                                                    "      },\n" +
                                                    "      \"transaccion\": null\n" +
                                                    "    }\n" +
                                                    "  ]\n" +
                                                    "}")
                                    )
                            }
                    ))
    })
    public CuentaUsuarioEmpresaResDto registrarUsuariosEnLDAP(@Valid @RequestBody CuentaUsuarioEmpresaAggregateDto cuentaUsuarioEmpresaAggregateDto, BindingResult bindingResult) throws Exception {
        CuentaUsuarioEmpresaResDto cuentaUsuarioEmpresaResDto = CuentaUsuarioEmpresaResDto.builder().build();
        Resultado resultado = Resultado.builder().build();
        Error error = Error.builder().build();

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                error.getDetalles().add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }

            error.setCodigo(CodigoTransaccionEnum.COL09.getCodigo());
            error.setDescripcion(CodigoTransaccionEnum.COL09.getDescripcion());
            resultado.setCodigo("400");
            resultado.setError(error);
            cuentaUsuarioEmpresaResDto.getResultado().add(resultado);
            return cuentaUsuarioEmpresaResDto;
        }

        logsManager.info("registrarUsuarios: ", cuentaUsuarioEmpresaAggregateDto);
        var resp = directorioActivoService.registrarUsuariosEnLDAP(cuentaUsuarioEmpresaAggregateDto);
        return resp;
    }
}
