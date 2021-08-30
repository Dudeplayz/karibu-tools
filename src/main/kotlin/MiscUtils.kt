package com.github.mvysny.kaributools

import com.vaadin.flow.component.HasStyle
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.reflect.Method

internal fun String.containsWhitespace(): Boolean = any { it.isWhitespace() }

private val regexWhitespace = Regex("\\s+")
internal fun String.splitByWhitespaces(): List<String> = split(regexWhitespace).filterNot { it.isBlank() }

/**
 * Splits this string by whitespaces to obtain individual class names, then
 * calls [HasStyle.addClassName] on each class name.
 */
internal fun String.feedClassNamesTo(target: HasStyle) {
    splitByWhitespaces().forEach { target.addClassName(it) }
}

/**
 * Returns the getter method for given property name; fails if there is no such getter.
 */
public fun Class<*>.getGetter(propertyName: String): Method {
    val descriptors: Array<out PropertyDescriptor> = Introspector.getBeanInfo(this).propertyDescriptors
    val descriptor: PropertyDescriptor? = descriptors.firstOrNull { it.name == propertyName }
    requireNotNull(descriptor) { "No such field '$propertyName' in $this; available properties: ${descriptors.joinToString { it.name }}" }
    val getter: Method = requireNotNull(descriptor.readMethod) { "The $this.$propertyName property does not have a getter: $descriptor" }
    return getter
}