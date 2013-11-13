package name.iparraga.model;

public class Comment extends ClassToken {
	private final String comment;

	public Comment(String comment) {
		super();
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public void toCode(StringBuilder code) {
		code.append(comment);
		code.append("\n");
	}
}
