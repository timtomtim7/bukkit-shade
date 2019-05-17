package blue.sparse.bshade.command.parameters.annotations;

import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface HasPermission {
	String value();
	PermissionDefault defaultTo() default PermissionDefault.OP;
}
