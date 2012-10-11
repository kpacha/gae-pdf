package pdfsp.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;

import pdfsp.model.exception.EmptyBodyException;
import pdfsp.model.exception.EmptyHeadException;
import pdfsp.model.exception.EmptyHtmlSourceException;
import pdfsp.util.css.Css2StyleSheet;
import pdfsp.util.html.HTMLParser;
import pdfsp.util.pdf.FontFactoryImplLocal;
import pdfsp.util.pdf.ImageFactory;

/**
 * ITextPdf Helper
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class PDFCreator {

    // The HTML code to parse.
    protected String html;

    // The StyleSheet.
    protected StyleSheet styles = null;

    // Extra properties.
    protected HashMap<String, Object> providers = null;

    // The HTML parser
    protected HTMLParser htmlParser = null;

    protected boolean shouldClean;

    /**
     * Default constructor
     * 
     * @param html
     * @throws EmptyBodyException
     * @throws EmptyHtmlSourceException
     */
    public PDFCreator(Text html) throws EmptyHtmlSourceException,
	    EmptyBodyException {
	this(html, false);
    }

    /**
     * Default constructor
     * 
     * @param source
     * @param shouldClean
     * @throws EmptyBodyException
     * @throws EmptyHtmlSourceException
     */
    public PDFCreator(Text source, boolean shouldClean)
	    throws EmptyHtmlSourceException, EmptyBodyException {
	this.shouldClean = shouldClean;
	this.setHtml(source.getValue());
	String html = this.getHtml();
	this.extractCSS(html);
	this.prepareProviders();
	if (this.shouldClean) {
	    this.setHtml(this.getHTMLParser(html).getCleanedHtml());
	}
    }

    /**
     * Starts the received html conversion and cleans the source on demand
     * 
     * @param html
     * @return the pdf as a blob
     * @throws IOException
     * @throws DocumentException
     */
    public Blob convert() throws IOException, DocumentException {
	return new Blob(this.encodeBody(this.getHtml()));
    }

    /**
     * Declare the providers
     */
    private void prepareProviders() {
	providers = new HashMap<String, Object>();
	providers.put(HTMLWorker.FONT_PROVIDER, new FontFactoryImplLocal());
	providers.put(HTMLWorker.IMG_PROVIDER, new ImageFactory());
    }

    /**
     * Extracts the HTML Body and returns the parsed result as a byte array
     * 
     * @param source
     * @return the byte[] of the body tag
     * @throws IOException
     * @throws DocumentException
     */
    private byte[] encodeBody(String source) throws IOException,
	    DocumentException {
	Document document = new Document();
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PdfWriter writer = PdfWriter.getInstance(document, baos);
	// TODO should the document be signed?

	// TODO should the document be encrypted?
	// writer.setEncryption(USER, OWNER, PdfWriter.ALLOW_PRINTING,
	// PdfWriter.STANDARD_ENCRYPTION_128);
	// writer.createXmpMetadata();

	// TODO should the document be compressed?
	// writer.setFullCompression();
	document.open();
	List<Element> objects = HTMLWorker.parseToList(
		new StringReader(source), styles, providers);
	for (Element element : objects) {
	    document.add(element);
	}
	document.close();
	// Document.compress = true;
	byte[] result = baos.toByteArray();
	baos.close();
	return result;
    }

    /**
     * Extracts linked and inline CSS definitions
     * 
     * @param source
     */
    private void extractCSS(String source) {
	styles = null;
	try {
	    this.extractExternalCSS(source);
	    this.extractInlineCSS(source);
	    // System.out.print("extractCSS: extraction success!\n");
	} catch (EmptyHtmlSourceException e1) {
	    System.out.print(e1.getMessage());
	} catch (EmptyHeadException e) {
	    System.out.print(e.getMessage());
	} catch (EmptyBodyException e) {
	    System.out.print(e.getMessage());
	}
    }

    /**
     * @return the html
     */
    public final String getHtml() {
	return html;
    }

    /**
     * @param source
     *            the html to set
     */
    public final void setHtml(String source) {
	this.html = source;
    }

    /**
     * Sets the styles for the HTML to PDF conversion
     * 
     * @param styles
     *            a StyleSheet object
     */
    public void setStyles(StyleSheet styles) {
	this.styles = styles;
    }

    /**
     * Set some extra properties.
     * 
     * @param providers
     *            the properties map
     */
    public void setProviders(HashMap<String, Object> providers) {
	this.providers = providers;
    }

    /**
     * Return the the html parser
     * 
     * @param html
     * @return the HTMLParser
     * @throws EmptyHtmlSourceException
     * @throws EmptyBodyException
     */
    private HTMLParser getHTMLParser(String html)
	    throws EmptyHtmlSourceException, EmptyBodyException {
	if (this.htmlParser == null) {
	    this.htmlParser = new HTMLParser(html);
	}
	return this.htmlParser;
    }

    /**
     * Add the inline CSS declarations to the class prop styles
     * 
     * @param html
     * @throws EmptyHtmlSourceException
     * @throws EmptyHeadException
     * @throws EmptyBodyException
     */
    private void extractInlineCSS(String html) throws EmptyHtmlSourceException,
	    EmptyHeadException, EmptyBodyException {
	List<String> cssBlocks = this.getHTMLParser(html).getInlineCSS();
	for (String cssBlock : cssBlocks) {
	    Css2StyleSheet converter = new Css2StyleSheet(cssBlock);
	    styles = converter.convert(styles);
	}
	this.setStyles(styles);
    }

    /**
     * Extract the CSS links, download them and parse the defined rules
     * 
     * @param html
     * @throws EmptyHtmlSourceException
     * @throws EmptyHeadException
     * @throws EmptyBodyException
     */
    public void extractExternalCSS(String html)
	    throws EmptyHtmlSourceException, EmptyHeadException,
	    EmptyBodyException {
	StyleSheet styles = new StyleSheet();
	String styleTxt;
	List<String> cssUrls = this.getHTMLParser(html).getCSSUrls();
	for (String cssUrl : cssUrls) {
	    styleTxt = "";
	    try {
		URL url = new URL(cssUrl);
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(url.openStream()));
		String line;

		while ((line = reader.readLine()) != null) {
		    styleTxt += line;
		}
		reader.close();

		Css2StyleSheet converter = new Css2StyleSheet(styleTxt);
		styles = converter.convert(styles);

	    } catch (MalformedURLException e) {
		// ...
	    } catch (IOException e) {
		// ...
	    } catch (NullPointerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (ClassCastException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	this.setStyles(styles);
	// this.setStyles(CSSStyle.getSampleStyleSheet());
    }
}
