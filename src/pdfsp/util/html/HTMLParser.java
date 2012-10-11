package pdfsp.util.html;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pdfsp.model.exception.EmptyBodyException;
import pdfsp.model.exception.EmptyHeadException;
import pdfsp.model.exception.EmptyHtmlSourceException;

/**
 * Html Parser class.
 * 
 * Wrapper of HtmlCleaner lib with some alternative html cleaner methods
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class HTMLParser {

    private String html;

    private Element htmlNode;

    /**
     * Constructor
     * 
     * Cleans the html source and sets the htmlNode
     * 
     * @param html
     * @throws EmptyBodyException
     * @throws EmptyHtmlSourceException
     */
    public HTMLParser(String html) throws EmptyHtmlSourceException,
	    EmptyBodyException {
	// FIXME should set up a default domain!!!
	this(html, "http://localhost/");
    }

    /**
     * Constructor
     * 
     * Cleans the html source and sets the htmlNode
     * 
     * @param html
     * @param domain
     * @throws EmptyBodyException
     * @throws EmptyHtmlSourceException
     */
    public HTMLParser(String html, String domain)
	    throws EmptyHtmlSourceException, EmptyBodyException {
	Document document;
	try {
	    html = this.cleanHref(html, domain);
	} catch (URISyntaxException e1) {
	    // do nothing!
	}
	try {
	    this.html = this.cleanHtml(html);
	} catch (IOException e) {
	    // If there was an exception, try to clean the html in the old
	    // custom way
	    System.out
		    .print("HTMLPasrer: external htmlcleaner fails. Trying with the custom one\n");
	    this.html = this.cleanHtmlUgly(html);
	}
	try {
	    SAXBuilder builder = new SAXBuilder();
	    document = builder.build(this.string2InputStream(this.html));
	    this.htmlNode = document.getRootElement();
	} catch (IOException io) {
	    System.out.println(io.getMessage());
	} catch (JDOMException jdomex) {
	    System.out.println(jdomex.getMessage());
	}
    }

    /**
     * Gets the HEAD node of the HTML
     * 
     * @return the head element
     * @throws EmptyHtmlSourceException
     * @throws EmptyHeadException
     */
    protected Element getHeadNode() throws EmptyHtmlSourceException,
	    EmptyHeadException {
	if (this.htmlNode == null) {
	    throw new EmptyHtmlSourceException("The HTML Node is null");
	}
	if (this.htmlNode.getContentSize() == 0) {
	    throw new EmptyHtmlSourceException();
	}
	// System.out.print("getHeadNode: htmlNode= " + this.htmlNode);
	List node = htmlNode.getChildren("head");
	if (node.size() == 0) {
	    throw new EmptyHeadException();
	}
	Element head = (Element) node.get(0);
	if (head == null) {
	    throw new EmptyHeadException();
	}
	return head;
    }

    /**
     * Gets the BODY node of the HTML
     * 
     * @return the body element
     * @throws EmptyHtmlSourceException
     * @throws EmptyHeadException
     */
    protected Element getBodyNode() throws EmptyHtmlSourceException,
	    EmptyBodyException {
	if (this.htmlNode == null) {
	    throw new EmptyHtmlSourceException("The HTML Node is null");
	}
	if (this.htmlNode.getContentSize() == 0) {
	    throw new EmptyHtmlSourceException();
	}
	List node = htmlNode.getChildren("body");
	if (node.size() == 0) {
	    throw new EmptyBodyException();
	}
	Element body = (Element) node.get(0);
	if (body == null) {
	    throw new EmptyBodyException();
	}
	return body;
    }

    /**
     * Get the inline CSS definitions from the HEAD section
     * 
     * @return the inline css definitions
     * @throws EmptyHtmlSourceException
     * @throws EmptyHeadException
     */
    public List<String> getInlineCSS() throws EmptyHtmlSourceException,
	    EmptyHeadException {
	List<String> cssBlocks = new ArrayList<String>();
	Element headNode = this.getHeadNode();
	List styleNodes = headNode.getChildren("style");
	if (0 < styleNodes.size()) {
	    for (Object style : styleNodes) {
		Element node = (Element) style;
		if (!node.getChildText("type").equalsIgnoreCase("text/css")) {
		    continue;
		}
		cssBlocks.add(node.getChildText("href"));
	    }
	}
	return cssBlocks;
    }

    /**
     * Get th URL of the linked css
     * 
     * @return the list of the linked css resources
     * @throws EmptyHtmlSourceException
     * @throws EmptyHeadException
     */
    public List<String> getCSSUrls() throws EmptyHtmlSourceException,
	    EmptyHeadException {
	List<String> cssUrls = new ArrayList<String>();
	Element headNode = this.getHeadNode();
	List linkNodes = headNode.getChildren("link");
	if (0 < linkNodes.size()) {
	    for (Object link : linkNodes) {
		Element node = (Element) link;
		if (!node.getChildText("rel").equalsIgnoreCase("stylesheet")) {
		    continue;
		}
		if (!node.getChildText("type").equalsIgnoreCase("text/css")) {
		    continue;
		}
		if (node.getChildText("href").equalsIgnoreCase("")) {
		    continue;
		}
		cssUrls.add(node.getChildText("href"));
	    }
	}
	return cssUrls;
    }

    /**
     * Return the cleaned html
     * 
     * @return the html
     */
    public final String getCleanedHtml() {
	return html;
    }

    /**
     * Convert string in InputStream
     * 
     * @param in
     *            the string
     * @return string as InputStream
     * @throws UnsupportedEncodingException
     */
    private InputStream string2InputStream(String in)
	    throws UnsupportedEncodingException {
	InputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
	return is;
    }

    /**
     * Make absolute all the relative urls
     * 
     * @param html
     *            source to clean
     * @param domain
     *            to refer
     * @return the cleaned html source
     */
    private String cleanHref(String html, String domain)
	    throws URISyntaxException {
	// absolute URI used for change all relative links
	URI addressUri = new URI(domain);
	// finds all link atributes (href, src, etc.)
	// Pattern pattern =
	// Pattern.compile("(href|src|action|background)=\"[^\"]*\"",
	// Pattern.CASE_INSENSITIVE);
	Pattern pattern = Pattern.compile("(src)=\"[^\"]*\"",
		Pattern.CASE_INSENSITIVE);
	Matcher m = pattern.matcher(html);
	// determines if the link is allready absolute
	Pattern absoluteLinkPattern = Pattern.compile("[a-z]+://.+");
	// buffer for result saving
	StringBuffer buffer = new StringBuffer();
	// position from where should next iteration take content to append to
	// buffer
	int lastEnd = 0;
	while (m.find()) {
	    // position of link in quotes
	    int startPos = html.indexOf('"', m.start()) + 1;
	    int endPos = m.end() - 1;
	    String link = html.substring(startPos, endPos);
	    Matcher absoluteMatcher = absoluteLinkPattern.matcher(link);
	    // is the link relative?
	    if (!absoluteMatcher.find()) {
		// create relative URL
		URI tmpUri = addressUri.resolve(link);
		// append the string between links
		buffer.append(html.substring(lastEnd, startPos - 1));
		// append new link
		buffer.append(tmpUri.toString());
		lastEnd = endPos + 1;
	    }
	}
	// append the end of file
	buffer.append(html.substring(lastEnd));
	return buffer.toString();
    }

    /**
     * Cleans the HTML source with HtmlCleaner lib
     * 
     * @param html
     * @return the cleaned html
     * @throws IOException
     */
    private String cleanHtml(String html) throws IOException {
	// set cleaner properties
	CleanerProperties props = this.getCleanner();

	// clean and compact the html source
	TagNode tagNode = new HtmlCleaner(props).clean(html);
	StringWriter stringWriter = new StringWriter();
	new CompactHtmlSerializer(props).write(tagNode, stringWriter, "utf-8");
	return stringWriter.getBuffer().toString();
    }

    /**
     * Create and init a cleanner
     * 
     * @return the initialized CleannerProperties
     */
    private CleanerProperties getCleanner() {
	// set cleaner properties
	CleanerProperties props = new CleanerProperties();

	props.setAdvancedXmlEscape(true);
	props.setTransResCharsToNCR(true);
	props.setTranslateSpecialEntities(true);
	props.setTransSpecialEntitiesToNCR(true);

	props.setOmitXmlDeclaration(true);
	props.setOmitComments(true);

	props.setIgnoreQuestAndExclam(true);
	props.setUseEmptyElementTags(true);

	return props;
    }

    /**
     * Cleans the HTML source
     * 
     * @param html
     * @return the cleaned html
     */
    private String cleanHtmlUgly(String html) {
	int index;
	html = this.cleanScript(html);
	while (html.indexOf("&nbsp") != -1) {
	    index = html.indexOf("&nbsp");
	    html = html.substring(0, index) + html.substring(index + 5);
	}
	html = HtmlManipulator.replaceHtmlEntities(html);
	return this.cleanDocType(html);
    }

    /**
     * Removes the inline javascript blocks from the html source
     * 
     * @param html
     * @return the cleaned html
     */
    private String cleanScript(String html) {
	int open;
	int close;
	while ((open = html.indexOf("<script")) != -1
		&& (close = html.indexOf("</script>")) != -1) {
	    html = html.substring(0, open) + html.substring(close + 9);
	}
	return html;
    }

    /**
     * Removes the Doctype declaration
     * 
     * @param html
     * @return the cleaned html
     */
    private String cleanDocType(String html) {
	String needle = "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"";
	if (html.indexOf(needle) == -1) {
	    return html;
	}
	return html.substring(0, html.indexOf(needle))
		+ html.substring(0, html.indexOf(needle) + needle.length() + 1);
    }

    /**
     * Make the href absolute
     * 
     * @param html
     * @param domain
     *            to insert in the relative url
     * @return the cleaned html
     * @throws EmptyBodyException
     * @throws EmptyHtmlSourceException
     */
    // private String cleanHref(String html, String domain)
    // throws EmptyHtmlSourceException, EmptyBodyException {
    // Element bodyNode = this.getBodyNode();
    // List imgNodes = bodyNode.getChildren("img");
    // if (0 < imgNodes.size()) {
    // String relativeSrc = null;
    // String absoluteSrc = null;
    // Element node = null;
    // for (Object link : imgNodes) {
    // node = (Element) link;
    // relativeSrc = node.getChildText("src");
    // if (relativeSrc.equalsIgnoreCase("")) {
    // continue;
    // }
    // if (relativeSrc.indexOf("http") != 0) {
    // absoluteSrc = domain + '/' + relativeSrc;
    // }
    // html.replace(relativeSrc, absoluteSrc);
    // }
    // }
    // return "";
    // }
}
