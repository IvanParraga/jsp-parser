package name.iparraga.parser;

import name.iparraga.model.Bundle;

public class SetBundle extends Bundle {
	private final String bundlePath;

	public SetBundle(String bundlePath) {
		this.bundlePath = bundlePath;
	}

	@Override
	public void toCode(StringBuilder code) {
		code.append(bundlePath);
		code.append("\n");
	}
}
