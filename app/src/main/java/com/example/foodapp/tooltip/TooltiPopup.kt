package com.example.foodapp.tooltip

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt

@Composable
fun TooltipPopup(
    modifier: Modifier = Modifier,
    requesterView: @Composable (Modifier) -> Unit,
    tooltipContent: @Composable () -> Unit,
) {
    var isShowTooltip by remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(TooltipPopupPosition()) }

    val view = LocalView.current.rootView

    if (isShowTooltip) {
        TooltipPopup(
            onDismissRequest = { isShowTooltip = !isShowTooltip },
            position = position
        ) {
            tooltipContent()
        }
    }

    requesterView(
        modifier
            .noRippleClickable { isShowTooltip = !isShowTooltip }
            .onGloballyPositioned { coordinates ->
                position = calculateTooltipPopupPosition(view, coordinates)
            }
    )
}

@Composable
fun TooltipPopup(
    position: TooltipPopupPosition,
    backgroundShape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = Color.Black,
    arrowHeight: Dp = 4.dp,
    horizontalPadding: Dp = 16.dp,
    onDismissRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var alignment = Alignment.TopCenter
    var offset = position.offset

    val horizontalPaddingInPx = with(LocalDensity.current) { horizontalPadding.toPx() }
    var arrowPositionX by remember { mutableStateOf(position.centerPositionX) }

    with(LocalDensity.current) {
        val arrowPaddingPx = arrowHeight.toPx().roundToInt() * 3
        if (position.alignment == TooltipAlignment.TopCenter) {
            alignment = Alignment.TopCenter
            offset = offset.copy(y = position.offset.y + arrowPaddingPx)
        } else {
            alignment = Alignment.BottomCenter
            offset = offset.copy(y = position.offset.y - arrowPaddingPx)
        }
    }

    val popupPositionProvider = remember(alignment, offset) {
        TooltipAlignmentOffsetPositionProvider(
            alignment = alignment,
            offset = offset,
            horizontalPaddingInPx = horizontalPaddingInPx,
            centerPositionX = position.centerPositionX
        ) { newPosition -> arrowPositionX = newPosition }
    }

    Popup(
        popupPositionProvider = popupPositionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(dismissOnBackPress = false)
    ) {
        BubbleLayout(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .background(backgroundColor, backgroundShape),
            alignment = position.alignment,
            arrowHeight = arrowHeight,
            arrowPositionX = arrowPositionX
        ) {
            content()
        }
    }
}

data class TooltipPopupPosition(
    val offset: IntOffset = IntOffset(0, 0),
    val alignment: TooltipAlignment = TooltipAlignment.TopCenter,
    val centerPositionX: Float = 0f
)

fun calculateTooltipPopupPosition(
    view: View,
    coordinates: LayoutCoordinates?
): TooltipPopupPosition {
    coordinates ?: return TooltipPopupPosition()

    val visibleWindowBounds = android.graphics.Rect()
    view.getWindowVisibleDisplayFrame(visibleWindowBounds)

    val boundsInWindow = coordinates.boundsInWindow()

    val heightAbove = boundsInWindow.top - visibleWindowBounds.top
    val heightBelow = visibleWindowBounds.bottom - visibleWindowBounds.top - boundsInWindow.bottom

    val centerPositionX = boundsInWindow.right - (boundsInWindow.right - boundsInWindow.left) / 2
    val offsetX = centerPositionX - visibleWindowBounds.centerX()

    return if (heightAbove < heightBelow) {
        TooltipPopupPosition(
            offset = IntOffset(coordinates.size.height, offsetX.toInt()),
            alignment = TooltipAlignment.TopCenter,
            centerPositionX = centerPositionX
        )
    } else {
        TooltipPopupPosition(
            offset = IntOffset(-coordinates.size.height, offsetX.toInt()),
            alignment = TooltipAlignment.BottomCenter,
            centerPositionX = centerPositionX
        )
    }
}

enum class TooltipAlignment {
    BottomCenter,
    TopCenter
}

internal class TooltipAlignmentOffsetPositionProvider(
    val alignment: Alignment,
    val offset: IntOffset,
    val centerPositionX: Float,
    val horizontalPaddingInPx: Float,
    private val onArrowPositionX: (Float) -> Unit
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        var popupPosition = IntOffset(anchorBounds.left, anchorBounds.top)

        val parentAlignmentPoint = alignment.align(IntSize.Zero, IntSize(anchorBounds.width, anchorBounds.height), layoutDirection)
        val relativePopupPos = alignment.align(IntSize.Zero, IntSize(popupContentSize.width, popupContentSize.height), layoutDirection)

        popupPosition += parentAlignmentPoint
        popupPosition -= IntOffset(relativePopupPos.x, relativePopupPos.y)
        popupPosition += offset

        val leftSpace = centerPositionX - horizontalPaddingInPx
        val rightSpace = windowSize.width - centerPositionX - horizontalPaddingInPx

        val halfPopupContentSize = popupContentSize.center.x

        when {
            halfPopupContentSize <= leftSpace && halfPopupContentSize <= rightSpace -> {
                popupPosition = IntOffset(centerPositionX.toInt() - halfPopupContentSize, popupPosition.y)
                onArrowPositionX(halfPopupContentSize.toFloat() - horizontalPaddingInPx)
            }
            halfPopupContentSize > rightSpace -> {
                popupPosition = IntOffset(centerPositionX.toInt(), popupPosition.y)
                onArrowPositionX(halfPopupContentSize + (halfPopupContentSize - rightSpace))
            }
            else -> {
                popupPosition = IntOffset(0, popupPosition.y)
                onArrowPositionX(centerPositionX - horizontalPaddingInPx)
            }
        }

        return popupPosition
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun BubbleLayout(
    modifier: Modifier = Modifier,
    alignment: TooltipAlignment = TooltipAlignment.TopCenter,
    arrowHeight: Dp,
    arrowPositionX: Float,
    content: @Composable () -> Unit
) {
    val arrowHeightPx = with(LocalDensity.current) { arrowHeight.toPx() }

    Box(
        modifier = modifier.drawBehind {
            val path = Path()

            if (alignment == TooltipAlignment.TopCenter) {
                val position = Offset(arrowPositionX, 0f)
                path.moveTo(position.x, position.y)
                path.lineTo(position.x - arrowHeightPx, position.y)
                path.lineTo(position.x, position.y - arrowHeightPx)
                path.lineTo(position.x + arrowHeightPx, position.y)
            } else {
                val arrowY = size.height
                val position = Offset(arrowPositionX, arrowY)
                path.moveTo(position.x, position.y)
                path.lineTo(position.x + arrowHeightPx, position.y)
                path.lineTo(position.x, position.y + arrowHeightPx)
                path.lineTo(position.x - arrowHeightPx, position.y)
            }

            drawPath(path = path, color = Color.Black)
            path.close()
        }
    ) {
        content()
    }
}
