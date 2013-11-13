package name.iparraga.model;

import java.util.LinkedList;
import java.util.List;

public class MainClass {
	List<ClassToken> comments = new LinkedList<>();

	private final String package_;
	private final String className;


	public MainClass(String package_, String className) {
		super();
		this.package_ = package_;
		this.className = className;
	}

	public String toCode() {
		return
				"package " + package_ + ";\n\n" +
				"public class " + className + " {\n" +
				"}";
	}
}
