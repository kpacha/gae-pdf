package pdfsp.util.pdf;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import pdfsp.connector.MemcacheConnector;
import pdfsp.model.exception.MemcacheConnectorException;

/**
 * Helper class implementing the ImageProvider class. It is needed to resolve
 * the paths to images
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class ImageFactory implements ImageProvider {

    protected MemcacheConnector mc = null;

    @Override
    public Image getImage(String src, Map<String, String> h,
	    ChainedProperties cprops, DocListener doc) {
	if (!src.matches("^http[s]?://[-a-zA-Z0-9_.:]+[-a-zA-Z0-9_:@&?=+,.!/~*'%$]*$")) {
	    // FIXME should set up a default domain!!!
	    src = "http://localhost/" + src;
	}
	// try to get the resource form the memcache
	try {
	    byte[] rawImage = this.getMemcacheConnector().getByteArray(src);
	    if (rawImage.length == 0) {
		throw new NullPointerException();
	    }
	    System.out.print("Se ha recuperado " + src + " de memcache\n");
	    return Image.getInstance(rawImage);
	} catch (BadElementException e) {
	} catch (MalformedURLException e) {
	} catch (IOException e) {
	} catch (NullPointerException e) {
	}
	// TODO should we look for it at the database?
	try {
	    // so get from the original source
	    Image image = Image.getInstance(src);
	    // save the resource in memcache!
	    this.getMemcacheConnector().put(src, image.getRawData());
	    System.out.print("Se ha guardado " + src + " en memcache\n");
	    return image;
	} catch (DocumentException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Init the memcache connector
     * 
     * @return the connector
     */
    protected MemcacheConnector getMemcacheConnector() {
	try {
	    if (this.mc != null) {
		return this.mc;
	    }
	} catch (NullPointerException e) {
	}
	try {
	    this.mc = new MemcacheConnector();
	} catch (MemcacheConnectorException e) {
	} catch (CacheException e) {
	}
	return this.mc;
    }
}