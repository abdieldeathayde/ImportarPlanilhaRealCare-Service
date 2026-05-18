package com.service.care.service.demo.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.service.care.service.demo.dto.ProdutoDTO;
import com.service.care.service.demo.service.ExcelService;

@RestController
@RequestMapping("/api/planilha")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/importar")
    public ResponseEntity<?> importarExcel(@RequestParam("arquivo") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo vazio.");
        }

        try {
            List<ProdutoDTO> produtos = excelService.processarExcel(file);
            
            // Opcional: Aqui você faria o loop para salvar no banco de dados:
            // produtoRepository.saveAll(produtos);

            return ResponseEntity.ok(produtos); // Retorna a lista processada em JSON
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar o Excel: " + e.getMessage());
        }
    }
}