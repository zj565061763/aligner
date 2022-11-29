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
                x = getXAlignStart(input)
                y = getYAlignTop(input)
            }
            Position.TopCenter -> {
                x = getXAlignCenter(input)
                y = getYAlignTop(input)
            }
            Position.TopEnd -> {
                x = getXAlignEnd(input)
                y = getYAlignTop(input)
            }

            Position.CenterStart -> {
                x = getXAlignStart(input)
                y = getYAlignCenter(input)
            }
            Position.Center -> {
                x = getXAlignCenter(input)
                y = getYAlignCenter(input)
            }
            Position.CenterEnd -> {
                x = getXAlignEnd(input)
                y = getYAlignCenter(input)
            }

            Position.BottomStart -> {
                x = getXAlignStart(input)
                y = getYAlignBottom(input)
            }
            Position.BottomCenter -> {
                x = getXAlignCenter(input)
                y = getYAlignBottom(input)
            }
            Position.BottomEnd -> {
                x = getXAlignEnd(input)
                y = getYAlignBottom(input)
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