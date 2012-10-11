package pdfsp.util.css;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper to work with the CSS and parse css declarations from String to
 * StyleSheet
 * 
 * @author kpacha666@gmail.com
 * @version 0.1
 */
public class Css2StyleSheet {

    private String css;

    public Css2StyleSheet(String css) {
	this.css = css;
	this.cleanCss();
    }

    /**
     * Removes the comments from the css string
     */
    private void cleanCss() {
	int open = 0;
	int close = 0;
	while ((open = css.indexOf("/*")) != -1
		&& (close = css.indexOf("*/")) != -1) {
	    css = css.substring(0, open) + css.substring(close + 2);
	}
    }

    /**
     * Adds the CSS declarations to the received StyleSheet
     * 
     * @param styles
     * @return the received styles param updated with the CSS declarations at
     *         this.css
     */
    public StyleSheet convert(StyleSheet styles) {
	// Each rule gets parsed and then removed from _css_ until all rules
	// have been
	// parsed.
	while (css.length() > 0) {
	    // Save the index of the first left bracket and first right bracket.
	    int lbracket = css.indexOf('{');
	    int rbracket = css.indexOf('}');

	    // ## Part 1: The declarations

	    // Split the declaration block of the first rule into an array and
	    // remove
	    // whitespace from each declaration.
	    Map<String, String> declarations = this.toMap(css.substring(
		    lbracket + 1, rbracket).split(";"));

	    // ## Part 2: The selectors
	    //
	    // Each selector in the selectors block will be associated with the
	    // declarations defined above. For example, `h1, p#bar {color:
	    // red}`<br/>
	    // result in the object<br/>
	    // {"h1": {color: red}, "p#bar": {color: red}}

	    // Split the selectors block of the first rule into an array and
	    // remove
	    // whitespace, e.g. `"h1, p#bar, span.foo"` get parsed to
	    // `["h1", "p#bar", "span.foo"]`.
	    String[] selectors = css.substring(0, lbracket).split(",");

	    // Iterate through each selector from _selectors_.
	    for (String selector : selectors) {
		styles.applyStyle(selector.trim(), declarations);
	    }

	    // Continue to next instance
	    css = css.substring(rbracket + 1).trim();
	}
	// return the json data
	return styles;
    }

    /**
     * Helper method that transform an array to a map, by splitting each
     * declaration (_font: Arial_) into key (_font_) and value(_Arial_).
     * 
     * Transform the declarations to a map. For example, the declarations<br/>
     * `font: 'Times New Roman' 1em; color: #ff0000; margin-top: 1em;`<br/>
     * result in the object<br/>
     * `{"font": "'Times New Roman' 1em", "color": "#ff0000", "margin-top":
     * "1em"}`.
     * 
     * @param declarations
     * @return the map with the css declarations
     */
    protected Map<String, String> toMap(String[] declarations) {
	Map<String, String> hm = new HashMap<String, String>();
	for (String declaration : declarations) {
	    int index = declaration.indexOf(":");
	    String property = declaration.substring(0, index).trim();
	    String value = declaration.substring(index + 1).trim();
	    hm.put(value, property);
	}
	return hm;
    }
}
