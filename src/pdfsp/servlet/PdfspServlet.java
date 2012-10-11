package pdfsp.servlet;

import java.io.IOException;
import java.io.OutputStream;

import pdfsp.util.PDFCreator;
import pdfsp.util.mock.HTML;

/**
 * Encodes the simple Html from the HTML mock and renders the pdf on the browser
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
@SuppressWarnings("serial")
public class PdfspServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response)
	    throws IOException {

	byte[] pdf;
	try {
	    PDFCreator pdfCreator = new PDFCreator(HTML.simpleHtml, true);
	    pdf = pdfCreator.convert().getBytes();
	    // setting some response headers
	    response.setHeader("Expires", "0");
	    response.setHeader("Cache-Control",
		    "must-revalidate, post-check=0, pre-check=0");
	    response.setHeader("Pragma", "public");
	    // setting the content type
	    response.setContentType("application/pdf");
	    // the content length
	    response.setContentLength(pdf.length);
	    // write ByteArrayOutputStream to the ServletOutputStream
	    OutputStream os = response.getOutputStream();
	    os.write(pdf);
	    os.flush();
	    os.close();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
