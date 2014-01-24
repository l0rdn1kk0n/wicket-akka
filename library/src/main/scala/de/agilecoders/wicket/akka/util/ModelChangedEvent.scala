package de.agilecoders.wicket.akka.util


/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
case class ModelChangedEvent[T](sender: Class[T], value: T)
