package com.github.pasp.tool.xml;

import java.io.InputStream;

public interface IXmlParser {

	<T> T parse(InputStream in, Class<T> cl);

}
