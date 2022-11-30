package com.sd.lib.aligner

import com.sd.lib.aligner.Aligner.*

open class FAligner : Aligner {
    private var _position = Position.TopEnd

    override val position: Position get() = _position

    override fun setPosition(position: Position) {
        _position = position
    }

    override fun align(input: Input): Result {
        return alignInternal(input)
    }

    private fun alignInternal(input: Input): Result {
        var x = 0
        var y = 0

        when (position) {
            Position.TopStart -> {
                y = getYAlignTop(input) - input.sourceHeight
                x = getXAlignStart(input)
            }
            Position.TopCenter -> {
                y = getYAlignTop(input) - input.sourceHeight
                x = getXAlignCenter(input)
            }
            Position.TopEnd -> {
                y = getYAlignTop(input) - input.sourceHeight
                x = getXAlignEnd(input)
            }

            Position.BottomStart -> {
                y = getYAlignBottom(input) + input.sourceHeight
                x = getXAlignStart(input)
            }
            Position.BottomCenter -> {
                y = getYAlignBottom(input) + input.sourceHeight
                x = getXAlignCenter(input)
            }
            Position.BottomEnd -> {
                y = getYAlignBottom(input) + input.sourceHeight
                x = getXAlignEnd(input)
            }

            Position.StartTop -> {
                x = getXAlignStart(input) - input.sourceWidth
                y = getYAlignTop(input)
            }
            Position.StartCenter -> {
                x = getXAlignStart(input) - input.sourceWidth
                y = getYAlignCenter(input)
            }
            Position.StartBottom -> {
                x = getXAlignStart(input) - input.sourceWidth
                y = getYAlignBottom(input)
            }

            Position.EndTop -> {
                x = getXAlignEnd(input) + input.sourceWidth
                y = getYAlignTop(input)
            }
            Position.EndCenter -> {
                x = getXAlignEnd(input) + input.sourceWidth
                y = getYAlignCenter(input)
            }
            Position.EndBottom -> {
                x = getXAlignEnd(input) + input.sourceWidth
                y = getYAlignBottom(input)
            }

            Position.Center -> {
                x = getXAlignCenter(input)
                y = getYAlignCenter(input)
            }
        }

        return Result(input, x, y)
    }

    private fun getXAlignStart(input: Input): Int {
        return input.targetX - input.containerX
    }

    private fun getXAlignEnd(input: Input): Int {
        return getXAlignStart(input) + (input.targetWidth - input.sourceWidth)
    }

    private fun getXAlignCenter(input: Input): Int {
        return getXAlignStart(input) + (input.targetWidth - input.sourceWidth) / 2
    }

    private fun getYAlignTop(input: Input): Int {
        return input.targetY - input.containerY
    }

    private fun getYAlignBottom(input: Input): Int {
        return getYAlignTop(input) + (input.targetHeight - input.sourceHeight)
    }

    private fun getYAlignCenter(input: Input): Int {
        return getYAlignTop(input) + (input.targetHeight - input.sourceHeight) / 2
    }
}