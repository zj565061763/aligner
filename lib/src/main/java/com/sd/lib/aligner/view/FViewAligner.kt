package com.sd.lib.aligner.view

import android.view.View
import com.sd.lib.aligner.Aligner.LayoutInfo
import com.sd.lib.aligner.FAligner
import java.lang.ref.WeakReference

class FViewAligner : FAligner(), ViewAligner {

    override var target: View?
        get() {
            return targetLayoutInfo.let {
                if (it is ViewLayoutInfo) it.view else null
            }
        }
        set(value) {
            if (value != null) {
                setTargetLayoutInfo(InternalViewLayoutInfo(value))
            } else {
                if (targetLayoutInfo is InternalViewLayoutInfo) {
                    setTargetLayoutInfo(LayoutInfo.Unspecified)
                }
            }
        }

    override var source: View?
        get() {
            return sourceLayoutInfo.let {
                if (it is ViewLayoutInfo) it.view else null
            }
        }
        set(value) {
            if (value != null) {
                setSourceLayoutInfo(InternalViewLayoutInfo(value))
            } else {
                if (sourceLayoutInfo is InternalViewLayoutInfo) {
                    setSourceLayoutInfo(LayoutInfo.Unspecified)
                }
            }
        }

    override val sourceContainerLayoutInfo: LayoutInfo
        get() {
            val parentView = source.parentView()
            if (parentView == null) {
                if (super.sourceContainerLayoutInfo is InternalViewLayoutInfo) {
                    setSourceContainerLayoutInfo(LayoutInfo.Unspecified)
                }
            } else {
                val superInfo = super.sourceContainerLayoutInfo
                if (superInfo is InternalViewLayoutInfo) {
                    if (superInfo.view === parentView) {
                        // parent未发生变化
                        return superInfo
                    } else {
                        // parent变化了
                        setSourceContainerLayoutInfo(InternalViewLayoutInfo(parentView))
                    }
                } else if (superInfo === LayoutInfo.Unspecified) {
                    setSourceContainerLayoutInfo(InternalViewLayoutInfo(parentView))
                }
            }
            return super.sourceContainerLayoutInfo
        }

    private class InternalViewLayoutInfo(view: View) : ViewLayoutInfo(view)
}


open class ViewLayoutInfo(view: View) : LayoutInfo {
    private var _viewRef = WeakReference(view)
    private val _coordinateArray = IntArray(2)

    val view: View? get() = _viewRef.get()

    override val isReady: Boolean
        get() {
            if (view?.isAttachedToWindow == false) return false
            return width > 0 && height > 0
        }

    override val width: Int get() = view?.width ?: 0
    override val height: Int get() = view?.height ?: 0

    override val coordinate: IntArray
        get() {
            val view = view ?: return LayoutInfo.CoordinateUnspecified
            view.getLocationOnScreen(_coordinateArray)
            return _coordinateArray
        }
}

private fun View?.parentView(): View? {
    if (this == null) return null
    val parent = parent
    return if (parent is View) parent else null
}