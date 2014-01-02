package com.devisfactures.service;

import java.io.FileOutputStream;
import java.util.ArrayList;

import com.devisfactures.model.Article;
import com.devisfactures.model.Client;
import com.devisfactures.model.Devis;
import com.devisfactures.model.Facture;
import com.devisfactures.model.Infos;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePDF {
	
	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD);
	private static Font smallItalic = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLDITALIC);
	
	public static void createDevis(Infos src, Client clt, 
			Devis devis, ArrayList<Article> articles, 
			ArrayList<Integer> qttes){
		Document document = new Document(PageSize.A4);
		String fileName = "Devis_" + devis.getIdDevis() + ".pdf";
		try {
			PdfWriter.getInstance(document, new FileOutputStream("/sdcard/" + 
					fileName));
			document.open();
			document.addTitle(fileName);
			document.addCreator("DevisFactures");
			
			if(!src.getLogo().equals("")){
				Image logo = Image.getInstance(src.getLogo().toString());
				document.add(logo);
			}
				
			Paragraph p = new Paragraph();
			addEmptyLine(p, 2);
			p = new Paragraph("Devis n°" + devis.getIdDevis() + 
					", le " + DBService.formatDate(devis.getDate()), 
					smallItalic);
			p.setAlignment(Element.ALIGN_RIGHT);
			document.add(p);
			addEmptyLine(p, 3);

			p = new Paragraph(src.getRaisonSociale() + "\n" + src.getAdresse() 
					+ "\nTel : " + src.getTel() + "\nFax : " +
					src.getFax() + "\n" + src.getEmail() + "\n" + 
					src.getNotes(), smallBold);
			document.add(p);
			addEmptyLine(p, 3);
			
			p = new Paragraph("Au nom du client : \n" + clt.getRaisonSociale() 
					+ "\n" + clt.getAdresse(), smallBold);
			p.setAlignment(Element.ALIGN_RIGHT);
			addEmptyLine(p, 3);
			
			PdfPTable table; 
			PdfPCell c;

			table = new PdfPTable(5);
			c = new PdfPCell(new Phrase("Id. Article"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("Désignation"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("Qtté"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("PrixU HT"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("PrixU TTC"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			table.setHeaderRows(1);
			for(int i=0; i<articles.size(); i++){
				table.addCell(articles.get(i).getIdArticle().toString());
				table.addCell(articles.get(i).getLibelle().toString());
				table.addCell(String.valueOf(qttes.get(i)));
				table.addCell(String.valueOf(articles.get(i).getPrix()) + " " + 
						src.getDevise());
				table.addCell(String.valueOf(DBService.prixTTC(
						articles.get(i).getPrix())) + " " + src.getDevise());
			}
			p.add(table);
			
			addEmptyLine(p, 1);
			
			table = new PdfPTable(2);
			c = new PdfPCell(new Phrase("Prix total HT"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("Prix total TTC"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			table.setHeaderRows(1);
			c = new PdfPCell(new Phrase(String.valueOf(devis.getPrixHT()) + " " 
					+ src.getDevise()));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase(String.valueOf(DBService.prixTTC(
					devis.getPrixHT()) + " " + src.getDevise())));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			p.add(table);
			
			document.add(p);
			
			document.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		}
	}
	
	public static void createFacture(Infos src, Client clt, 
			Facture facture, ArrayList<Article> articles, 
			ArrayList<Integer> qttes){
		Document document = new Document(PageSize.A4);
		String fileName = "Facture_" + facture.getIdFacture() + ".pdf";
		try {
			PdfWriter.getInstance(document, new FileOutputStream("/sdcard/" + 
					fileName));
			document.open();
			document.addTitle(fileName);
			document.addCreator("DevisFactures");
			
	        Image logo = Image.getInstance(src.getLogo().toString());
	        document.add(logo);
				
			Paragraph p = new Paragraph();
			addEmptyLine(p, 2);
			p = new Paragraph("Facture n°" + facture.getIdFacture() + 
					", le " + DBService.formatDate(facture.getDate()), 
					smallItalic);
			p.setAlignment(Element.ALIGN_RIGHT);
			document.add(p);
			addEmptyLine(p, 3);

			p = new Paragraph(src.getRaisonSociale() + "\n" + src.getAdresse() 
					+ "\nTel : " + src.getTel() + "\nFax : " +
					src.getFax() + "\n" + src.getEmail() + "\n" + 
					src.getNotes(), smallBold);
			document.add(p);
			addEmptyLine(p, 3);
			
			p = new Paragraph("Au nom du client : \n" + clt.getRaisonSociale() 
					+ "\n" + clt.getAdresse(), smallBold);
			p.setAlignment(Element.ALIGN_RIGHT);
			addEmptyLine(p, 3);
			
			PdfPTable table; 
			PdfPCell c;

			table = new PdfPTable(5);
			c = new PdfPCell(new Phrase("Id. Article"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("Désignation"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("Qtté"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("PrixU HT"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("PrixU TTC"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			table.setHeaderRows(1);
			for(int i=0; i<articles.size(); i++){
				table.addCell(articles.get(i).getIdArticle().toString());
				table.addCell(articles.get(i).getLibelle().toString());
				table.addCell(String.valueOf(qttes.get(i)));
				table.addCell(String.valueOf(articles.get(i).getPrix()) + " " + 
						src.getDevise());
				table.addCell(String.valueOf(DBService.prixTTC(
						articles.get(i).getPrix())) + " " + src.getDevise());
			}
			p.add(table);
			
			addEmptyLine(p, 1);
			
			table = new PdfPTable(2);
			c = new PdfPCell(new Phrase("Prix total HT"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase("Prix total TTC"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			table.setHeaderRows(1);
			c = new PdfPCell(new Phrase(String.valueOf(facture.getPrixHT()) + 
					" " + src.getDevise()));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			c = new PdfPCell(new Phrase(String.valueOf(DBService.prixTTC(
					facture.getPrixHT()) + " " + src.getDevise())));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			p.add(table);
			
			document.add(p);
			
			document.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		}
	}


	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}
