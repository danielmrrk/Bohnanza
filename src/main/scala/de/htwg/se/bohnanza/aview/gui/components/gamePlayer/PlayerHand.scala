package de.htwg.se.bohnanza.aview.gui.components.gamePlayer

import de.htwg.se.bohnanza.model.GameComponent.PlayerComponent.IPlayer

import de.htwg.se.bohnanza.aview.gui.components.global.Card
import de.htwg.se.bohnanza.aview.gui.components.global.Hand
import de.htwg.se.bohnanza.aview.gui.components.global.GameButtonFactory
import de.htwg.se.bohnanza.aview.gui.model.SelectionManager
import de.htwg.se.bohnanza.aview.gui.components.global.TurnOverFieldContainer

import scalafx.geometry.Pos
import scalafx.scene.layout.VBox

case class PlayerHand(
    currentViewPlayer: IPlayer,
    selectionManager: SelectionManager
) extends VBox {
  var flipped = true
  var selectableCard: Card = _
  val handcards: List[Card] = currentViewPlayer.hand.cards match {
    case Nil => List.empty
    case head :: tail =>
      selectableCard = new Card(
        bean = head,
        scaleFactor = 0.4,
        selectable = true,
        selectionManager = Some(selectionManager),
        handCard = true,
        selectedCards = List.empty
      )
      val otherCards = tail.map { bean =>
        new Card(
          bean = bean,
          scaleFactor = 0.4,
          selectionManager = None,
          handCard = true,
          selectedCards = List.empty
        )
      }
      selectableCard :: otherCards
  }
  val hand = Hand(cards = handcards)

  val buttonWidth = 180
  val buttonHeight = 35
  val buttonFontsize = 16
  val buttonSpacing = 5

  val flipCardsButton = GameButtonFactory.createGameButton(
    text = "Flip Hand Cards",
    width = buttonWidth,
    height = buttonHeight
  ) { () =>
    onFlipCardsButtonClick()
  }
  flipCardsButton.style = s"-fx-font-size: ${12}"

  def onFlipCardsButtonClick(): Unit = {
    flipped = !flipped

    if (flipped) {
      children = Seq(flipCardsButton, hand.flippAllCards())
    } else {
      children = Seq(flipCardsButton, hand)
    }
  }

  if (currentViewPlayer.hand.cards.isEmpty) { flipCardsButton.visible = false }
  else { flipCardsButton.visible = true }

  children = Seq(flipCardsButton, hand.flippAllCards())
  alignment = Pos.TOP_CENTER
  spacing = buttonSpacing
}