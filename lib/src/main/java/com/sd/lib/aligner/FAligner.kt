package com.sd.lib.aligner

import com.sd.lib.aligner.Aligner.*
import kotlin.properties.Delegates

open class FAligner : Aligner {
    private val _coordinateTarget = IntArray(2)
    private val _coordinateSourceParent = IntArray(2)

    private var _x = 0
    private var _y = 0

    override var callback: Callback? = null

    override var position by Delegates.observable(Position.TopRight) { _, oldValue, newValue ->
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
            Position.TopLeft -> layoutTopLeft(source, target)
            Position.TopCenter -> layoutTopCenter(source, target)
            Position.TopRight -> layoutTopRight(source, target)

            Position.LeftCenter -> layoutLeftCenter(source, target)
            Position.Center -> layoutCenter(source, target)
            Position.RightCenter -> layoutRightCenter(source, target)

            Position.BottomLeft -> layoutBottomLeft(source, target)
            Position.BottomCenter -> layoutBottomCenter(source, target)
            Position.BottomRight -> layoutBottomRight(source, target)
        }

        callback.onUpdate(_x, _y, source, target)
        return true
    }

    //---------- position start----------

    private fun layoutTopLeft(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignLeft()
        _y = getYAlignTop()
    }

    private fun layoutTopCenter(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignTop()
    }

    private fun layoutTopRight(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignTop()
    }

    private fun layoutLeftCenter(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignLeft()
        _y = getYAlignCenter(source, target)
    }

    private fun layoutCenter(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignCenter(source, target)
    }

    private fun layoutRightCenter(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignCenter(source, target)
    }

    private fun layoutBottomLeft(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignLeft()
        _y = getYAlignBottom(source, target)
    }

    private fun layoutBottomCenter(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignBottom(source, target)
    }

    private fun layoutBottomRight(source: LayoutInfo, target: LayoutInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignBottom(source, target)
    }

    //---------- position end----------

    private fun getXAlignLeft(): Int {
        return _coordinateTarget[0] - _coordinateSourceParent[0]
    }

    private fun getXAlignRight(source: LayoutInfo, target: LayoutInfo): Int {
        return getXAlignLeft() + (target.width - source.width)
    }

    private fun getXAlignCenter(source: LayoutInfo, target: LayoutInfo): Int {
        return getXAlignLeft() + (target.width - source.width) / 2
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