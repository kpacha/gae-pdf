package pdfsp.util.pdf;

import com.itextpdf.text.FontFactoryImp;

/**
 * Helper class implementing the FontProvider interface and extending the
 * FontFactoryImp. This is needed to select the correct fonts.
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class FontFactoryImplLocal extends FontFactoryImp {
//     // the registered fonts
//     // protected HashMap<String, Font> fonts = new HashMap<String, Font>();
//
//    @Override
//    public Font getFont(String fontname, String encoding, boolean embedded,
//	    float size, int style, BaseColor color) {
//	try {
//	    if (fonts.containsKey(fontname)) {
//		fonts.put(
//			fontname,
//			new Font(BaseFont.createFont(fontname, encoding,
//				embedded, true), size, style, color));
//	    }
//	    return fonts.get(fontname);
//	} catch (NullPointerException e) {
//	    // do nothing
//	} catch (DocumentException e) {
//	    // do nothing
//	} catch (IOException e) {
//	    // do nothing
//	}
//	// default font is times roman
//	return new Font(FontFamily.TIMES_ROMAN, size, style, color);
//    }
//
//    @Override
//    public boolean isRegistered(String fontname) {
//	return fonts.containsKey(fontname);
    }
}