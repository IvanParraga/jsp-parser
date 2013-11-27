package name.iparraga.model;

/**
 * @author iparraga
 *
 */
public class Code extends ClassToken {
	protected String code;


	public Code(String code) {
		super();
		this.code = code;
	}

	@Override
	public void toCode(StringBuilder code) {
		code.append(processCode());
		code.append("\n");
	}

	private String processCode() {
		return code.replaceAll("return;", "return stringOut.toString();");
	}

	@Override
	public String toString() {
		return "Code [code=\"" + code + "\"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Code other = (Code) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
