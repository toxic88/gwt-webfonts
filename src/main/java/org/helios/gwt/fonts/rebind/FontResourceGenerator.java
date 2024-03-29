package org.helios.gwt.fonts.rebind;



import java.net.URL;

import org.helios.gwt.fonts.client.FontName;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.ext.AbstractResourceGenerator;
import com.google.gwt.resources.ext.ResourceContext;
import com.google.gwt.resources.ext.ResourceGeneratorUtil;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.StringSourceWriter;

public class FontResourceGenerator extends AbstractResourceGenerator 
{
	@Override
	public String createAssignment(TreeLogger logger, ResourceContext context,
			JMethod method) throws UnableToCompleteException 
	{
		SourceWriter sw = new StringSourceWriter();
		
		sw.print("new ");
		sw.print(method.getReturnType().getQualifiedSourceName());
		sw.println("() {");
		sw.indent();

		writeGetFontName(method, sw);
		writeGetName(method, sw);
		writeEnsureInjected(sw);
		writeGetText(logger, context, method, sw);
		
		sw.outdent();
		sw.println("}");
		return sw.toString();
	}

	private void writeGetFontName(JMethod method, SourceWriter sw) 
	{
		sw.println("public String getFontName() {");
		sw.indent();
		
		sw.print("return \"");
		sw.print(getFontName(method));
		sw.println("\";");
		sw.outdent();
		sw.println("}");
	}

	protected void writeEnsureInjected(SourceWriter sw) {
	    sw.println("private boolean injected;");
	    sw.println("public boolean ensureInjected() {");
	    sw.indent();
	    sw.println("if (!injected) {");
	    sw.indentln("injected = true;");
	    sw.indentln(StyleInjector.class.getName() + ".inject(getText());");
	    sw.indentln("return true;");
	    sw.println("}");
	    sw.println("return false;");
	    sw.outdent();
	    sw.println("}");
	  }	

	  private void writeGetName(JMethod method, SourceWriter sw) {
	    sw.println("public String getName() {");
	    sw.indent();
	    sw.println("return \"" + method.getName() + "\";");
	    sw.outdent();
	    sw.println("}");
	  }
	  
	  private void writeGetText(TreeLogger logger, ResourceContext context, JMethod method, SourceWriter sw) throws UnableToCompleteException {
		  sw.println("public String getText() {");
		  sw.indent();
		  
		  URL[] urls = ResourceGeneratorUtil.findResources(logger, context, method);
		  
		  sw.print("return \"");
		  sw.print("@font-face{");
		  
		  sw.print("font-family:'");
		  sw.print(getFontName(method));
		  sw.print("';");
		  
		  sw.print("font-style:normal;");
		  sw.print("font-weight:400;");
	  
		  try {
				String agent = context.getGeneratorContext().getPropertyOracle().getSelectionProperty(logger, "user.agent").getCurrentValue();
				if (agent.equals("ie6") || agent.equals("ie8"))
					writeSrcIE(logger, context, method, sw, urls);
				else
					writeSrc(logger, context, method, sw, urls);
			} catch (BadPropertyValueException e) {
				throw new UnableToCompleteException();
			}
		  sw.print("}");

		  sw.println("\";");
		  
		  sw.outdent();
		  sw.println("}");
		  
	  }

	private void writeSrc(TreeLogger logger, ResourceContext context,
			JMethod method, SourceWriter sw, URL[] urls) throws UnableToCompleteException 
	{
		for (URL url: urls)
		{
			String lower = url.getPath().toLowerCase();
			if (lower.endsWith(".ttf") || lower.endsWith(".otf"))
			{
				  // generar recurso para el fichero ttf
				  String outputUrlExpression = context.deploy(
				            url, "application/x-font-ttf", false);
				  
				  sw.print("src:url('\" + ");
				  sw.print(outputUrlExpression);
				  sw.print(" + \"') format('truetype');");
			}
		}
	}

	private void writeSrcIE(TreeLogger logger, ResourceContext context,
			JMethod method, SourceWriter sw, URL[] urls) throws UnableToCompleteException 
	{
		for (URL url: urls)
		{
			if (url.getPath().toLowerCase().endsWith(".eot"))
			{
			  String outputUrlExpression = context.deploy(
			            url, "application/vnd.ms-fontobject", false);
			  
			  sw.print("src:url('\" + ");
			  sw.print(outputUrlExpression);
			  sw.print(" + \"');");
			}
		}
	}

	private String getFontName(JMethod method) {
		FontName name = method.getAnnotation(FontName.class);
		return name == null? method.getName(): name.value();
	}
}
