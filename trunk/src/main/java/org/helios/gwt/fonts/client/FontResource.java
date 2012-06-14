package org.helios.gwt.fonts.client;

import org.helios.gwt.fonts.rebind.FontResourceGenerator;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.ResourceGeneratorType;

/**
 * Resource representing a custom web-font. It use is similar to
 * a CSS: it must be injected in the page. It provides getFontName
 * method to use this name in sibling CSS's that want to apply this
 * font. Use @link FontName annotation to manually change this name. Use
 * &#64;Source to indicate the font files to include in the module
 * (must be one TTF and one EOT [for IE]).
 * <p>
 * Sample:
 * <pre>
 * class MyClientBundle extends ClientBundle {
 *    @Source({"font1.ttf", "font1.eot"})
 *    @FontName("otra")
 *    FontResource theFont();
 *
 *    @Source("styles.css")
 *    MyCss css();
 * }
 * </pre>
 * 
 * And you can use the font hardcoded:
 * 
 * <pre>
 * font-family: otra;
 * </pre>
 * 
 * ...or using the convenience method:
 * 
 *  <pre>
 *  font-family: value('theFont.getFontName');
 *  </pre>
 */
@ResourceGeneratorType(FontResourceGenerator.class)
public interface FontResource extends ResourcePrototype
{
	String getFontName();
	String getText();
	boolean ensureInjected();
}
