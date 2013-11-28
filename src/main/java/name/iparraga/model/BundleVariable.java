package name.iparraga.model;

public class BundleVariable extends Bundle {
	private static final String PAGE_SCOPE = "page";
	private static final String REQUEST_SCOPE = "request";
	private static final String SESSION_SCOPE = "session";

	private final String key;
	private final String value;
	private final String scope;

	public BundleVariable(String key, String value, String scope) {
		this.key = key;
		this.value = value;
		this.scope = scope;
	}

	@Override
	public void toCode(StringBuilder code) {
		code.append("\t\t");

		if (REQUEST_SCOPE.equals(scope) || PAGE_SCOPE.equals(scope)) {
			code.append("request.");
		} else if (SESSION_SCOPE.equals(scope)) {
			code.append("session.");
		} else {
			throw new IllegalArgumentException(
					"Scope: \"" + scope + "\" not expected");
		}

		code.append("setAttribute(\"");
		code.append(key);
		code.append("\",\"");
		code.append(value);
		code.append("\");\n");
	}

}
