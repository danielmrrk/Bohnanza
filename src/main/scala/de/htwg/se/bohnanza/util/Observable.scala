package bohnanza.util

import bohnanza.model.*

enum ObserverEvent {
  case PhaseChange
  case Plant
  case Harvest
  case Take
  case GameInfo
  case Draw // draw and turn for debugging purposes!!!
  case Turn
}

trait Observer {
  def update(event: ObserverEvent): Unit
  def update(error: HandlerResponse): Unit
}

class Observable {
  var subscribers: Vector[Observer] = Vector()

  def add(s: Observer): Unit = subscribers = subscribers :+ s

  def remove(s: Observer): Unit = subscribers =
    subscribers.filterNot(o => o == s)

  def notifyObservers(event: ObserverEvent): Unit =
    subscribers.foreach(o => o.update(event))

  def notifyObservers(error: HandlerResponse): Unit =
    subscribers.foreach(o => o.update(error))
}
