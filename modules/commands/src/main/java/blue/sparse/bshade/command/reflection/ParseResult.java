package blue.sparse.bshade.command.reflection;

import java.lang.reflect.Parameter;

interface ParseResult {
	class Success implements ParseResult {
		public Object[] args;
	}

	class FailSender implements ParseResult {
	}

	class FailParameter implements ParseResult {
		public Parameter parameter;
	}
}