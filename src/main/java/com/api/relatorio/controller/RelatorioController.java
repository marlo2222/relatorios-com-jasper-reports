package com.api.relatorio.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private DataSource dataSource;
    @PostMapping
    public void imprimir(@RequestParam Map<String, Object> parametros, HttpServletResponse response) throws JRException, SQLException, IOException {

        parametros = parametros == null ? parametros = new HashMap<>() : parametros;

        //arquivo  .jasper localizado em resources
        InputStream jasperStrream = this.getClass().getResourceAsStream("/relatorios/disciplinas.jasper");

        //cria o objeto jasperreports
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStrream);

        //Envia para o jasperPrint o relatorio e os parametros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,dataSource.getConnection());

        //resposta do tipo pdf
        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition", "inline; filename=livros.pdf");

        //exportação para o httpServletResponse
        final OutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }
}
