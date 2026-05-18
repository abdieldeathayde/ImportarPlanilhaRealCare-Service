package com.service.care.service.demo.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.service.care.service.demo.dto.ProdutoDTO;

@Service
public class ExcelService {

    public List<ProdutoDTO> processarExcel(MultipartFile file) throws Exception {
        List<ProdutoDTO> produtos = new ArrayList<>();

        // Abre o arquivo Excel de forma segura (suporta .xls e .xlsx)
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {
            
            // Pega a primeira aba da planilha
            Sheet sheet = workbook.getSheetAt(0);

            // Varre as linhas da planilha (começa na 1 para pular o cabeçalho)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                
                // Pula linhas completamente vazias
                if (row == null) {
                    continue;
                }

                // Lê os dados de cada coluna usando o método que não quebra com números
                String codigo = obterValorComoString(row.getCell(0));
                String descricao = obterValorComoString(row.getCell(1));
                String quantidadeStr = obterValorComoString(row.getCell(2));
                String valorStr = obterValorComoString(row.getCell(3));

                // Se a linha estiver vazia ou for a linha de "TOTAL", ignora e vai para a próxima
                if (codigo.isEmpty() && descricao.isEmpty()) {
                    continue;
                }
                if (codigo.equalsIgnoreCase("TOTAL") || descricao.toUpperCase().contains("TOTAL")) {
                    continue; 
                }

                // Cria o DTO e faz as conversões de segurança
                ProdutoDTO produto = new ProdutoDTO();
                produto.setCodigo(codigo);
                produto.setDescricao(descricao);
                
                // Trata a quantidade (converte texto para Integer)
                produto.setQuantidade(converterParaInteiro(quantidadeStr));
                
                // Trata o valor (converte texto limpo para Double)
                produto.setValor(converterParaDouble(valorStr));

                produtos.add(produto);
            }
        }

        return java.util.Collections.unmodifiableList(produtos);
    }

    /**
     * CORREÇÃO COMPLETA: Lê qualquer tipo de célula (Texto, Número, Fórmula) 
     * e retorna estritamente como uma String limpa, evitando o erro do Apache POI.
     */
    private String obterValorComoString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING -> {
                return cell.getStringCellValue().trim();
            }
                
            case NUMERIC -> {
                double valorNumero = cell.getNumericCellValue();
                // Se o número terminar em .0 (ex: 61252.0), remove o ponto para manter o código/ID original
                if (valorNumero == (long) valorNumero) {
                    return String.valueOf((long) valorNumero);
                }
                return String.valueOf(valorNumero);
            }
                
            case FORMULA -> {
                try {
                    return cell.getStringCellValue().trim();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            }
                
            case BOOLEAN -> {
                return String.valueOf(cell.getBooleanCellValue());
            }
                
            default -> {
                return "";
            }
        }
    }

    // Método auxiliar para converter a quantidade com segurança
    private Integer converterParaInteiro(String texto) {
        try {
            if (texto == null || texto.trim().isEmpty()) return 0;
            // Remove possíveis casas decimais que o excel joga (ex: "1.0" vira "1")
            if (texto.contains(".")) {
                texto = texto.split("\\.")[0];
            }
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            return 0; // Valor padrão se der erro de conversão
        }
    }

    // Método auxiliar para converter o valor monetário com segurança
    private Double converterParaDouble(String texto) {
        try {
            if (texto == null || texto.trim().isEmpty()) return 0.0;
            
            // Limpa o texto tirando "R$", espaços e ajustando a pontuação padrão do Brasil para a do Java
            String limpo = texto.replaceAll("[R$\\s]", "") // Remove R$ e espaços
                                 .replace(".", "")         // Remove ponto de milhar (ex: 1.890,00 -> 1890,00)
                                 .replace(",", ".");       // Substitui vírgula decimal por ponto (ex: 1890,00 -> 1890.00)
            
            return Double.valueOf(limpo);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}