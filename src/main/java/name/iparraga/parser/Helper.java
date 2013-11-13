package name.iparraga.parser;

import name.iparraga.model.Comment;
import name.iparraga.model.MainClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Helper {
	private static final Logger logger = LoggerFactory.getLogger(Helper.class);
	private final MainClass class_;

	public Helper() {
		class_ = new MainClass("foo","foo");
	}

	public void addComment(String comment) {
		logger.debug("adding comment " + comment);
		class_.add(new Comment(comment));
	}

	public MainClass getMainClass() {
		return class_;
	}
}
