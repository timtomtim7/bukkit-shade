package blue.sparse.bshade.command.parameters.filter;

import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;

public interface AnnotationParameterFilter<A extends Annotation, V> {

	Class<A> getAnnotationClass();

	Class<V> getValueClass();

	boolean filter(CommandSender sender, A annotation, V value);

//	String getDefaultFailMessage();
//
//	default String getFailMessageKey() {
//		return "fail"+getAnnotationClass().getName();
//	}

}
