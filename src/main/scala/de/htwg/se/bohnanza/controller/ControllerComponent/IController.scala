package de.htwg.se.bohnanza.controller.ControllerComponent

import de.htwg.se.bohnanza.model.GameComponent.IGame
import de.htwg.se.bohnanza.util.*
import de.htwg.se.bohnanza.util.UndoManager
import de.htwg.se.bohnanza.model.PhaseStateComponent.*

import java.util.Observer

trait IController extends Observable {
  var game: IGame
  var phase: IPhaseState

  def createPlayer(playerName: String): Unit

  def undo(): Unit

  def redo(): Unit

  def draw(playerIndex: Int): Unit

  def plant(playerIndex: Int, beanFieldIndex: Int): Unit

  def nextPhase: Unit

  def harvest(playerIndex: Int, beanFieldIndex: Int): Unit

  def turn: Unit

  def take(playerIndex: Int, cardIndex: Int, beanFieldIndex: Int): Unit
}
