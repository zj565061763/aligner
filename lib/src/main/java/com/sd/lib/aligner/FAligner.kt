package com.sd.lib.aligner

import com.sd.lib.aligner.Aligner.*
import kotlin.properties.Delegates

open class FAligner : Aligner {
    private val _coordinateTarget = IntArray(2)
    private val _coordinateSourceParent = IntArray(2)

    private var _x = 0
    private var _y = 0

    override var callback: Callback? = null

    override var position by Delegates.observable(Position.TopEnd) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            update()
        }
    }

    override var sourceLayoutInfo: SourceLayoutInfo? by Delegates.observable(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            update()
        }
    }

    override var targetLayoutInfo: LayoutInfo? by Delegates.observable(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            update()
        }
    }

    override fun update(): Boolean {
        // check null
        val callback = callback ?: return false
        val source = sourceLayoutInfo ?: return false
        val target = targetLayoutInfo ?: return false

        // check canUpdate
        if (!callback.canUpdate(source, target)) {
            return false
        }

        // check isReady
        if (!source.isReady) return false
        if (!target.isReady) return false

        // check parent
        val sourceParent = source.parentLayoutInfo ?: return false
        if (!sourceParent.isReady) return false

        val coordinateTarget = target.coordinate
        if (coordinateTarget === LayoutInfo.coordinateUnspecified) return false

        val coordinateSourceParent = sourceParent.coordinate
        if (coordinateSourceParent === LayoutInfo.coordinateUnspecified) return false

        _coordinateTarget[0] = coordinateTarget[0]
        _coordinateTarget[1] = coordinateTarget[1]
        _coordinateSourceParent[0] = coordinateSourceParent[0]
        _coordinateSourceParent[1] = coordinateSourceParent[1]

        when (position) {
            Position.TopStart -> {
                _x = getXAlignStart()
                _y = getYAlignTop()
            }
            Position.TopCenter -> {
                _x = getXAlignCenter(source, target)
                _y = getYAlignTop()
            }
            Position.TopEnd -> {
                _x = getXAlignEnd(source, target)
                _y = getYAlignTop()
            }

            Position.CenterStart -> {
                _x = getXAlignStart()
                _y = getYAlignCenter(source, target)
            }
            Position.Center -> {
                _x = getXAlignCenter(source, target)
                _y = getYAlignCenter(source, target)
            }
            Position.CenterEnd -> {
                _x = getXAlignEnd(source, target)
                _y = getYAlignCenter(source, target)
            }

            Position.BottomStart -> {
                _x = getXAlignStart()
                _y = getYAlignBottom(source, target)
            }
            Position.BottomCenter -> {
                _x = getXAlignCenter(source, target)
                _y = getYAlignBottom(source, target)
            }
            Position.BottomEnd -> {
                _x = getXAlignEnd(source, target)
                _y = getYAlignBottom(source, target)
            }
        }

        callback.onUpdate(_x, _y, source, target)
        return true
    }

    private fun getXAlignStart(): Int {
        return _coordinateTarget[0] - _coordinateSourceParent[0]
    }

    private fun getXAlignEnd(source: LayoutInfo, target: LayoutInfo): Int {
        return getXAlignStart() + (target.width - source.width)
    }

    private fun getXAlignCenter(source: LayoutInfo, target: LayoutInfo): Int {
        return getXAlignStart() + (target.width - source.width) / 2
    }

    private fun getYAlignTop(): Int {
        return _coordinateTarget[1] - _coordinateSourceParent[1]
    }

    private fun getYAlignBottom(source: LayoutInfo, target: LayoutInfo): Int {
        return getYAlignTop() + (target.height - source.height)
    }

    private fun getYAlignCenter(source: LayoutInfo, target: LayoutInfo): Int {
        return getYAlignTop() + (target.height - source.height) / 2
    }
}