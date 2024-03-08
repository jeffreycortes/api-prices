package com.colsubsidio.pricesapi.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class EnvironmentService {
    @Value("${spring.application.version}")
    private String version;

    @Value("${logging.level.api}")
    private String loggingLevelApi;
    @Value("${logging.console}")
    private boolean loggingConsole;

    @Value("${apigee.url}")
    private String urlApigee;
    @Value("${apigee.token.clienteId}")
    private String clienteId;
    @Value("${apigee.token.clienteSecreto}")
    private String clienteSecreto;
    @Value("${apigee.token.maxTimeInMillis}")
    private Long maxTimeInMillis;
    @Value("${apigee.token.url}")
    private String tokenUrl;
    @Value("${apigee.path.logger}")
    private String logger;

    @Value("${apigee.services.consultar-usuarios-empresas}")
    private String consultarUsuariosEmpresas;
    @Value("${apigee.services.consultar-empresa}")
    private String consultarEmpresa;
    @Value("${apigee.services.crear-empresa}")
    private String crearEmpresa;
    @Value("${apigee.services.crear-usuario-empresa}")
    private String crearUsuarioEmpresa;

    @Value("${security.secret-aes}")
    private String secretAes;

    @Value("${security.api-key}")
    private String apiKey;

    @Value("${mail.url}")
    private String emailUrl;
    @Value("${mail.copy}")
    private String emailCopy;
    @Value("${mail.replyHidden}")
    private String emailReplyHidden;

    @Value("${notification.email.url}")
    private String notificationUrl;

    @Value("${notification.email.path}")
    private String notificationPath;

    @Value("${mail.url-colsubsidio.ayuda-afiliacion-grupo-familiar}")
    private String urlAyudaAfiliacionGrupoFamiliar;
    @Value("${mail.url-colsubsidio.colsubsidio-empresas}")
    private String urlColsubsidioEmpresas;
    @Value("${mail.url-colsubsidio.ayuda-subsidio-familiar}")
    private String urlAyudaSubsidioFamiliar;
    @Value("${mail.url-colsubsidio.ayuda-afiliacion-colaboradores}")
    private String urlAyudaAfiliacionColaboradores;
    @Value("${mail.mail-colsubsidio.correo-afiliaciones}")
    private String correoAfiliaciones;
    @Value("${mail.mail-colsubsidio.correo_novedades-afiliacion}")
    private String correoNovedadesAfiliacion;
}

