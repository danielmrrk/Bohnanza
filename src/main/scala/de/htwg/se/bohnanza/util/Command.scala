package bohnanza.util

import bohnanza.controller.Controller

abstract class Command(controller: Controller) {
  val memento: GameMemento = GameMemento(controller.game, controller.phase)
  def doStep: Unit
  def undoStep: Unit = {
    controller.game = memento.game
    controller.phase = memento.phase
  }
  def redoStep: Unit = doStep
}

class UndoManager:
  var undoStack: List[Command] = Nil
  var redoStack: List[Command] = Nil

  def doStep(command: Command) = {
    undoStack = command :: undoStack
    command.doStep
  }

  def undoStep = {
    undoStack match {
      case Nil =>
      case head :: stack => {
        head.undoStep
        undoStack = stack
        redoStack = head :: redoStack
      }
    }
  }

  def redoStep = {
    redoStack match {
      case Nil =>
      case head :: stack => {
        head.redoStep
        redoStack = stack
        undoStack = head :: undoStack
      }
    }
  }