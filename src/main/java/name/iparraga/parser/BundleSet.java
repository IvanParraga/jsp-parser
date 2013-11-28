package name.iparraga.parser;

import name.iparraga.model.Bundle;

public class BundleSet extends Bundle {
	private final String bundlePath;

	public BundleSet(String bundlePath) {
		this.bundlePath = bundlePath;
	}

	@Override
	public void toCode(StringBuilder code) {
		code.append("\t\tResourceBundle bundle = ResourceBundle.getBundle(\"");
		code.append(bundlePath);
		code.append("\");\n");
	}
}
