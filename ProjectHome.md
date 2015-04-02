# Motivation #

GWT-compiled CSS is great but it lacks support for @font-face declaration and font files embedding. This project aims to add that type.

# How to include into your project #

  1. Add dependency to the .jar (manually or via Maven: `org.helios.gwt:gwt-webfonts:0.1`).
  1. Include dependency in your gwt.xml file: `<inherits name="org.helios.gwt.fonts.GwtWebFonts"/>`

# How to declare fonts #

It simply provides a new type of Resource (to include into a client bundle). Use @Source standard label to indicate font files and that's it.

```
public interface MyClientBundle extends ClientBundle
{
   // be sure to include eot fonts for IE8 and previous versions)
   @Source({"font1.ttf", "font1.eot"})
   FontResource myFont();

   @Source("styles.css")
   MyCssResource css();
}

(...)

private static MyClientBundle MY_RESOURCES = GWT.create(MyClientBundle.class);
```

# How to use your font #

First, ensure the declaration is injected, as you do with CssResource's:

```
MY_RESOURCES.myFont().ensureInjected();
```

Then you can use your font, modify the generated font-family, ask for it in the CSS file with a value(...) declaration so you wont need to edit the file if it changes. It also generate the correct declaration depending on the target browser.

`MY_RESOURCES.myFont().getFontName()` returns the font-family used.

You can include it manually or use GWT's css `value` feature to build your css:

```
.someClass {
   font-family: value('myFont.getFontName');
}
```

# Next steps #

  * **Name obfuscation:** It should be nice to have font-family name obfuscation (or prefixing), as in CSS classes, to avoid name collisions.
  * **More browser and format support:** now the generation of @font-face is rudimentary. It should check add other browsers support, add more font file extensions support and check font file format too (now it relies on file extension).

# Current supported fonts and browsers #

  * If browser is IE6/IE8 it uses only `.EOT` files.
  * With other browsers it uses only `.TTF` and `.OTF` files.

# Me #

<a href='http://stackoverflow.com/users/9686/helios'>
<img src='http://stackoverflow.com/users/flair/9686.png' alt='profile for helios at Stack Overflow, Q&A for professional and enthusiast programmers' title='profile for helios at Stack Overflow, Q&A for professional and enthusiast programmers' width='208' height='58' />
</a>