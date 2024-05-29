package bohnanza.controller

import bohnanza.model.*
import bohnanza.util.*
import java.util.Observer
import bohnanza.util.UndoManager

class Controller(var game: Game, var phase: PhaseState = PlayCardPhase())
    extends Observable {

  val undoManager = new UndoManager
  val takeInvalidPlantHandler = TakeInvalidPlantHandler(None)
  val invalidPlantHandler = InvalidPlantHandler(Option(takeInvalidPlantHandler))
  val turnOverFieldIndexHandler = TurnOverFieldIndexHandler(
    Option(invalidPlantHandler)
  )
  val beanFieldIndexHandler = BeanFieldIndexHandler(
    Option(turnOverFieldIndexHandler)
  )
  val playerIndexHandler = PlayerIndexHandler(Option(beanFieldIndexHandler))
  val argumentHandler = MethodHandler(Option(playerIndexHandler))

  def undo(): Unit = {
    undoManager.undoStep
    notifyObservers(ObserverEvent.Undo)
  }

  def redo(): Unit = {
    undoManager.redoStep
    notifyObservers(ObserverEvent.Redo)
  }

  def draw(playerIndex: Int): Unit = {
    val response = argumentHandler.checkOrDelegate(
      args = Map(
        HandlerKey.Method.key -> "draw",
        HandlerKey.PlayerFieldIndex.key -> playerIndex
      ),
      phase = phase,
      game = game
    )

    if (response == HandlerResponse.Success) {
      undoManager.doStep(DrawCommand(this, playerIndex))
      notifyObservers(ObserverEvent.Draw)
      return
    }

    notifyObservers(response)
  }

  def plant(playerIndex: Int, beanFieldIndex: Int): Unit = {
    val response = argumentHandler.checkOrDelegate(
      args = Map(
        HandlerKey.Method.key -> "plant",
        HandlerKey.PlayerFieldIndex.key -> playerIndex,
        HandlerKey.BeanFieldIndex.key -> beanFieldIndex
      ),
      phase = phase,
      game = game
    )

    if (response == HandlerResponse.Success) {
      undoManager.doStep(PlantCommand(this, playerIndex, beanFieldIndex))
      notifyObservers(ObserverEvent.Plant)
      return
    }

    notifyObservers(response)
  }

  def nextPhase: Unit = {
    undoManager.doStep(NextPhaseCommand(this))
    notifyObservers(ObserverEvent.PhaseChange)
  }

  def harvest(playerIndex: Int, beanFieldIndex: Int): Unit = {
    val response = argumentHandler.checkOrDelegate(
      args = Map(
        HandlerKey.Method.key -> "harvest",
        HandlerKey.PlayerFieldIndex.key -> playerIndex,
        HandlerKey.BeanFieldIndex.key -> beanFieldIndex
      ),
      phase = phase,
      game = game
    )

    if (response == HandlerResponse.Success) {
      undoManager.doStep(HarvestCommand(this, playerIndex, beanFieldIndex))
      notifyObservers(ObserverEvent.Harvest)
      return
    }

    notifyObservers(response)
  }

  def turn: Unit = {
    val response = argumentHandler.checkOrDelegate(
      args = Map(
        HandlerKey.Method.key -> "turn"
      ),
      phase = phase,
      game = game
    )

    if (response == HandlerResponse.Success) {
      undoManager.doStep(TurnCommand(this))
      notifyObservers(ObserverEvent.Turn)
      return
    }

    notifyObservers(response)
  }

  def take(playerIndex: Int, cardIndex: Int, beanFieldIndex: Int): Unit = {
    val response = argumentHandler.checkOrDelegate(
      args = Map(
        HandlerKey.Method.key -> "take",
        HandlerKey.PlayerFieldIndex.key -> playerIndex,
        HandlerKey.BeanFieldIndex.key -> beanFieldIndex,
        HandlerKey.TurnOverFieldIndex.key -> cardIndex
      ),
      phase = phase,
      game = game
    )

    if (response == HandlerResponse.Success) {
      undoManager.doStep(
        TakeCommand(this, playerIndex, cardIndex, beanFieldIndex)
      )
      notifyObservers(ObserverEvent.Take)
      return
    }

    notifyObservers(response)
  }
}
