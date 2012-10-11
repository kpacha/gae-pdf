package pdfsp.util.mock;


/**
 * CSS mocks for testing
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class CSSStyle {

    public static StyleSheet getSampleStyleSheet() {
	StyleSheet styles = new StyleSheet();
	styles.loadTagStyle("body", "margin", "10");
	styles.loadTagStyle("body", "background-color", "#CCC2B8");
	styles.loadTagStyle("body", "font-family", "arial, serif");

	styles.loadTagStyle("#maintitletop", "margin", "auto");
	styles.loadTagStyle("#maintitletop", "width", "62.8em");
	styles.loadTagStyle("#maintitletop", "height", "8.57em");
	styles.loadTagStyle("#maintitletop", "background",
		"url(http://www.htmlquick.com/img/titletop.jpg)");

	styles.loadTagStyle("#mainflags", "float", "right");
	styles.loadTagStyle("#mainflags", "margin", "6em 2em 0 2em");
	styles.loadTagStyle("#mainflags", "width", "2.5em");

	styles.loadTagStyle("#mainpageback", "width", "62.8em");
	styles.loadTagStyle("#mainpageback", "overflow", "auto");
	styles.loadTagStyle("#mainpageback", "margin", "auto");
	styles.loadTagStyle("#mainpageback", "background",
		"url(http://www.htmlquick.com/img/pageback.jpg)");

	styles.loadTagStyle("#maintitlebottom", "margin", "0 1.25em");
	styles.loadTagStyle("#maintitlebottom", "width", "60.3em");
	styles.loadTagStyle("#maintitlebottom", "overflow", "auto");
	styles.loadTagStyle("#maintitlebottom", "background",
		"url(http://www.htmlquick.com/img/titlebottom.jpg)");
	styles.loadTagStyle("#maintitlebottom", "background-repeat",
		"no-repeat");
	styles.loadTagStyle("#maintitlebottom", "background-position",
		"top-left");

	styles.loadTagStyle("#maintopnav", "width", "32em");
	styles.loadTagStyle("#maintopnav", "padding", "2.5em 0 0 1em");
	styles.loadTagStyle("#maintopnav", "font-size", "0.9em");
	styles.loadTagStyle("#maintopnav", "font-weight", "bold");

	styles.loadTagStyle("#mainmenuleft", "float", "left");
	styles.loadTagStyle("#mainmenuleft", "width", "10.57em");
	styles.loadTagStyle("#mainmenuleft", "height", "38.28em");
	styles.loadTagStyle("#mainmenuleft", "margin-top", "0.6em");
	styles.loadTagStyle("#mainmenuleft", "background",
		"url(http://www.htmlquick.com/img/menuleft.jpg)");

	return styles;
    }
}
