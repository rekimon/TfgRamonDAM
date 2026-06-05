package com.tfg.agrogestion.domain.cosecha.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.tfg.agrogestion.domain.cosecha.entity.Cosecha;
import com.tfg.agrogestion.domain.cosecha.repository.CosechaRepository;
import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfService {

    private final CosechaRepository cosechaRepository;

    private static final Font SUBTITULO_FONT =
            new Font(Font.FontFamily.HELVETICA, 14,
                    Font.BOLD, new BaseColor(46, 125, 50));
    private static final Font LABEL_FONT =
            new Font(Font.FontFamily.HELVETICA, 10,
                    Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font VALOR_FONT =
            new Font(Font.FontFamily.HELVETICA, 10,
                    Font.NORMAL, BaseColor.BLACK);
    private static final Font TOTAL_FONT =
            new Font(Font.FontFamily.HELVETICA, 14,
                    Font.BOLD, new BaseColor(27, 94, 32));

    @Transactional(readOnly = true)
    public byte[] generarPdfCosecha(Long cosechaId) {
        Cosecha cosecha = cosechaRepository.findByIdWithDetails(cosechaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cosecha", cosechaId));

        log.info("Generando PDF cosecha id={} parcela={}",
                cosechaId,
                cosecha.getCultivo().getParcela().getNombre());

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 60, 60);
            PdfWriter.getInstance(document, baos);
            document.open();

            PdfPTable header = new PdfPTable(1);
            header.setWidthPercentage(100);
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(new BaseColor(27, 94, 32));
            headerCell.setPadding(20);
            headerCell.setBorder(Rectangle.NO_BORDER);

            Paragraph titulo = new Paragraph("AgroGestion",
                    new Font(Font.FontFamily.HELVETICA, 24,
                            Font.BOLD, BaseColor.WHITE));
            titulo.setAlignment(Element.ALIGN_CENTER);
            headerCell.addElement(titulo);

            Paragraph subtitulo = new Paragraph("Informe de Cosecha",
                    new Font(Font.FontFamily.HELVETICA, 14,
                            Font.NORMAL, new BaseColor(200, 230, 201)));
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            headerCell.addElement(subtitulo);

            header.addCell(headerCell);
            document.add(header);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph(
                    "Informacion de la Explotacion", SUBTITULO_FONT));
            document.add(new LineSeparator(1, 100,
                    new BaseColor(46, 125, 50), Element.ALIGN_LEFT, -2));
            document.add(Chunk.NEWLINE);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(5);
            infoTable.setSpacingAfter(15);

            agregarFila(infoTable, "Parcela:",
                    cosecha.getCultivo().getParcela().getNombre());
            agregarFila(infoTable, "Municipio:",
                    cosecha.getCultivo().getParcela().getMunicipio()
                    + ", "
                    + cosecha.getCultivo().getParcela().getProvincia());
            agregarFila(infoTable, "Superficie:",
                    cosecha.getCultivo().getParcela().getSuperficieHa() + " ha");
            agregarFila(infoTable, "Tipo de cultivo:",
                    cosecha.getCultivo().getTipoCultivo().getNombre());
            agregarFila(infoTable, "Cultivo:",
                    cosecha.getCultivo().getNombrePersonalizado() != null
                    ? cosecha.getCultivo().getNombrePersonalizado()
                    : cosecha.getCultivo().getTipoCultivo().getNombre());

            document.add(infoTable);

            document.add(new Paragraph("Fechas del Ciclo", SUBTITULO_FONT));
            document.add(new LineSeparator(1, 100,
                    new BaseColor(46, 125, 50), Element.ALIGN_LEFT, -2));
            document.add(Chunk.NEWLINE);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            PdfPTable fechasTable = new PdfPTable(2);
            fechasTable.setWidthPercentage(100);
            fechasTable.setSpacingBefore(5);
            fechasTable.setSpacingAfter(15);

            agregarFila(fechasTable, "Fecha de siembra:",
                    cosecha.getCultivo().getFechaSiembra().format(fmt));
            agregarFila(fechasTable, "Fecha de cosecha:",
                    cosecha.getFechaCosecha().format(fmt));
            if (cosecha.getCultivo().getFechaCosechaEstimada() != null) {
                agregarFila(fechasTable, "Fecha estimada cosecha:",
                        cosecha.getCultivo().getFechaCosechaEstimada().format(fmt));
            }

            document.add(fechasTable);

            document.add(new Paragraph("Datos de Produccion", SUBTITULO_FONT));
            document.add(new LineSeparator(1, 100,
                    new BaseColor(46, 125, 50), Element.ALIGN_LEFT, -2));
            document.add(Chunk.NEWLINE);

            PdfPTable produccionTable = new PdfPTable(2);
            produccionTable.setWidthPercentage(100);
            produccionTable.setSpacingBefore(5);
            produccionTable.setSpacingAfter(15);

            agregarFila(produccionTable, "Kilogramos obtenidos:",
                    cosecha.getKgObtenidos() + " kg");
            agregarFila(produccionTable, "Precio por kg:",
                    cosecha.getPrecioPorKg() + " €/kg");
            agregarFila(produccionTable, "Calidad:", cosecha.getCalidad());
            if (cosecha.getObservaciones() != null
                    && !cosecha.getObservaciones().isEmpty()) {
                agregarFila(produccionTable, "Observaciones:",
                        cosecha.getObservaciones());
            }

            document.add(produccionTable);

            PdfPTable totalTable = new PdfPTable(1);
            totalTable.setWidthPercentage(100);
            PdfPCell totalCell = new PdfPCell();
            totalCell.setBackgroundColor(new BaseColor(232, 245, 233));
            totalCell.setPadding(15);
            totalCell.setBorderColor(new BaseColor(46, 125, 50));

            Paragraph totalLabel = new Paragraph(
                    "Beneficio Economico Total", LABEL_FONT);
            totalLabel.setAlignment(Element.ALIGN_CENTER);
            totalCell.addElement(totalLabel);

            java.math.BigDecimal ingreso = cosecha.getIngresoTotal();
            if (ingreso == null) {
                ingreso = cosecha.getKgObtenidos()
                        .multiply(cosecha.getPrecioPorKg());
            }

            Paragraph totalValor = new Paragraph(
                    String.format("%.2f €", ingreso), TOTAL_FONT);
            totalValor.setAlignment(Element.ALIGN_CENTER);
            totalCell.addElement(totalValor);

            totalTable.addCell(totalCell);
            document.add(totalTable);

            document.add(Chunk.NEWLINE);
            Paragraph pie = new Paragraph(
                    "Documento generado por AgroGestion · "
                    + java.time.LocalDate.now().format(fmt),
                    new Font(Font.FontFamily.HELVETICA, 8,
                            Font.ITALIC, BaseColor.GRAY));
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            document.close();
            log.info("PDF generado correctamente cosecha id={}", cosechaId);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generando PDF cosecha {}: {}",
                    cosechaId, e.getMessage(), e);
            throw new RuntimeException(
                    "Error generando PDF: " + e.getMessage(), e);
        }
    }

    private void agregarFila(PdfPTable table, String label, String valor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
        labelCell.setBorder(Rectangle.BOTTOM);
        labelCell.setBorderColor(new BaseColor(220, 220, 220));
        labelCell.setPadding(8);
        labelCell.setBackgroundColor(new BaseColor(250, 250, 250));

        PdfPCell valorCell = new PdfPCell(
                new Phrase(valor != null ? valor : "-", VALOR_FONT));
        valorCell.setBorder(Rectangle.BOTTOM);
        valorCell.setBorderColor(new BaseColor(220, 220, 220));
        valorCell.setPadding(8);

        table.addCell(labelCell);
        table.addCell(valorCell);
    }
}