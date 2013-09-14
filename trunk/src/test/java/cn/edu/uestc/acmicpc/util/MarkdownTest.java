package cn.edu.uestc.acmicpc.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.pegdown.PegDownProcessor;

import cn.edu.uestc.acmicpc.util.StringUtil;

/**
 * Test cases for markdown.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class MarkdownTest {

  @Test
  @Ignore
  public void testAmpsAndangleEncoding() {
    // FIXME broken test
    PegDownProcessor pegDownProcessor = new PegDownProcessor();
    String resource =
        "AT&T has an ampersand in their name.\n" + "\n" + "AT&amp;T is another way to write it.\n"
            + "\n" + "This & that.\n" + "\n" + "4 < 5.\n" + "\n" + "6 > 5.\n" + "\n"
            + "Here's a [link] [1] with an ampersand in the URL.\n" + "\n"
            + "Here's a link with an amersand in the link text: [AT&T] [2].\n" + "\n"
            + "Here's an inline [link](/script?foo=1&bar=2).\n" + "\n"
            + "Here's an inline [link](</script?foo=1&bar=2>).\n" + "\n" + "\n"
            + "[1]: http://example.com/?foo=1&bar=2\n" + "[2]: http://att.com/  \"AT&T\"";
    String expected =
        "<p>AT&amp;T has an ampersand in their name.</p>\n"
            + "\n"
            + "<p>AT&amp;T is another way to write it.</p>\n"
            + "\n"
            + "<p>This &amp; that.</p>\n"
            + "\n"
            + "<p>4 &lt; 5.</p>\n"
            + "\n"
            + "<p>6 > 5.</p>\n"
            + "\n"
            + "<p>Here's a <a href=\"http://example.com/?foo=1&amp;bar=2\">link</a> with an ampersand in the URL.</p>\n"
            + "\n"
            + "<p>Here's a link with an amersand in the link text: <a href=\"http://att.com/\" title=\"AT&amp;T\">AT&amp;T</a>.</p>\n"
            + "\n" + "<p>Here's an inline <a href=\"/script?foo=1&amp;bar=2\">link</a>.</p>\n"
            + "\n" + "<p>Here's an inline <a href=\"/script?foo=1&amp;bar=2\">link</a>.</p>\n";
    String actual = pegDownProcessor.markdownToHtml(resource);
    Assert.assertEquals(0, StringUtil.compareSkipSpaces(expected, actual));
  }
}