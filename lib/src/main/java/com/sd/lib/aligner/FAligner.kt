package com.sd.lib.aligner

import com.sd.lib.aligner.Aligner.*

open class FAligner : Aligner {
    private val _coordinateTarget = IntArray(2)
    private val _coordinateSourceContainer = IntArray(2)

    private var _position = Position.TopEnd
    private var _callback: Callback? = null

    private var _targetLayoutInfo = LayoutInfo.Unspecified
    private var _sourceLayoutInfo = LayoutInfo.Unspecified
    private var _sourceContainerLayoutInfo = LayoutInfo.Unspecified

    override val position: Position get() = _position
    override val targetLayoutInfo: LayoutInfo get() = _targetLayoutInfo
    override val sourceLayoutInfo: LayoutInfo get() = _sourceLayoutInfo
    override val sourceContainerLayoutInfo: LayoutInfo get() = _sourceContainerLayoutInfo

    override fun setCallback(callback: Callback?) {
        _callback = callback
    }

    override fun setPosition(position: Position) {
        if (_position != position) {
            _position = position
            update()
        }
    }

    override fun setTargetLayoutInfo(layoutInfo: LayoutInfo) {
        _targetLayoutInfo = layoutInfo
        update()
    }

    override fun setSourceLayoutInfo(layoutInfo: LayoutInfo) {
        _sourceLayoutInfo = layoutInfo
        update()
    }

    override fun setSourceContainerLayoutInfo(layoutInfo: LayoutInfo) {
        _sourceContainerLayoutInfo = layoutInfo
        update()
    }

    override fun update(): Result? {
        return updateInternal().also {
            _callback?.onResult(it)
        }
    }

    private fun updateInternal(): Result? {
        val target = targetLayoutInfo
        val source = sourceLayoutInfo
        val sourceContainer = sourceContainerLayoutInfo

        if (_callback?.canUpdate() == false) {
            return null
        }

        // check isReady
        if (!target.isReady) return null
        if (!source.isReady) return null
        if (!sourceContainer.isReady) return null

        // check coordinate
        val coordinateTarget = target.coordinate
        if (coordinateTarget === LayoutInfo.CoordinateUnspecified) return null

        val coordinateSourceContainer = sourceContainer.coordinate
        if (coordinateSourceContainer === LayoutInfo.CoordinateUnspecified) return null

        _coordinateTarget[0] = coordinateTarget[0]
        _coordinateTarget[1] = coordinateTarget[1]

        _coordinateSourceContainer[0] = coordinateSourceContainer[0]
        _coordinateSourceContainer[1] = coordinateSourceContainer[1]

        var x = 0
        var y = 0

        when (position) {
            Position.TopStart -> {
                x = getXAlignStart()
                y = getYAlignTop()
            }
            Position.TopCenter -> {
                x = getXAlignCenter(source, target)
                y = getYAlignTop()
            }
            Position.TopEnd -> {
                x = getXAlignEnd(source, target)
                y = getYAlignTop()
            }

            Position.CenterStart -> {
                x = getXAlignStart()
                y = getYAlignCenter(source, target)
            }
            Position.Center -> {
                x = getXAlignCenter(source, target)
                y = getYAlignCenter(source, target)
            }
            Position.CenterEnd -> {
                x = getXAlignEnd(source, target)
                y = getYAlignCenter(source, target)
            }

            Position.BottomStart -> {
                x = getXAlignStart()
                y = getYAlignBottom(source, target)
            }
            Position.BottomCenter -> {
                x = getXAlignCenter(source, target)
                y = getYAlignBottom(source, target)
            }
            Position.BottomEnd -> {
                x = getXAlignEnd(source, target)
                y = getYAlignBottom(source, target)
            }
        }

        return Result(x, y)
    }

    private fun getXAlignStart(): Int {
        return _coordinateTarget[0] - _coordinateSourceContainer[0]
    }

    private fun getXAlignEnd(source: LayoutInfo, target: LayoutInfo): Int {
        return getXAlignStart() + (target.width - source.width)
    }

    private fun getXAlignCenter(source: LayoutInfo, target: LayoutInfo): Int {
        return getXAlignStart() + (target.width - source.width) / 2
    }

    private fun getYAlignTop(): Int {
        return _coordinateTarget[1] - _coordinateSourceContainer[1]
    }

    private fun getYAlignBottom(source: LayoutInfo, target: LayoutInfo): Int {
        return getYAlignTop() + (target.height - source.height)
    }

    private fun getYAlignCenter(source: LayoutInfo, target: LayoutInfo): Int {
        return getYAlignTop() + (target.height - source.height) / 2
    }
}