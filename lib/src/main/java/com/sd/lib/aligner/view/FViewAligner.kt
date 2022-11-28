package com.sd.lib.aligner.view

import android.view.View
import com.sd.lib.aligner.Aligner.LayoutInfo
import com.sd.lib.aligner.Aligner.SourceLayoutInfo
import com.sd.lib.aligner.FAligner
import com.sd.lib.aligner.view.ViewAligner.ViewLayoutInfo
import java.lang.ref.WeakReference

class FViewAligner : FAligner(), ViewAligner {

    override var source: View?
        get() {
            val info = sourceLayoutInfo ?: return null
            return if (info is ViewLayoutInfo) info.view else null
        }
        set(value) {
            val info = sourceLayoutInfo
            if (info is ViewLayoutInfo) {
                info.view = value
            } else {
                sourceLayoutInfo = WeakSourceViewLayoutInfo().apply {
                    this.view = value
                }
            }
            update()
        }

    override var target: View?
        get() {
            val info = targetLayoutInfo ?: return null
            return if (info is ViewLayoutInfo) info.view else null
        }
        set(value) {
            val info = targetLayoutInfo
            if (info is ViewLayoutInfo) {
                info.view = value
            } else {
                targetLayoutInfo = WeakViewLayoutInfo().apply {
                    this.view = value
                }
            }
            update()
        }
}


open class WeakViewLayoutInfo : ViewLayoutInfo {
    private var _viewRef: WeakReference<View>? = null
    private val _coordinateArray = IntArray(2)

    override val isReady: Boolean
        get() {
            if (view?.isAttachedToWindow == false) return false
            return width > 0 && height > 0
        }

    override var view: View?
        get() = _viewRef?.get()
        set(value) {
            val old = _viewRef?.get()
            if (old != value) {
                _viewRef = if (value == null) null else WeakReference(value)
                onViewChanged(old, value)
            }
        }

    override val width: Int
        get() = view?.width ?: 0

    override val height: Int
        get() = view?.height ?: 0

    override val coordinate: IntArray
        get() {
            val view = view ?: return LayoutInfo.coordinateUnspecified
            view.getLocationOnScreen(_coordinateArray)
            return _coordinateArray
        }

    protected open fun onViewChanged(old: View?, view: View?) {}
}

open class WeakSourceViewLayoutInfo : WeakViewLayoutInfo(), SourceLayoutInfo {
    private val _parentInfo = WeakViewLayoutInfo()

    override val parentLayoutInfo: LayoutInfo?
        get() {
            val view = view ?: return null
            val parent = view.parent ?: return null
            if (parent !is View) return null
            _parentInfo.view = parent
            return _parentInfo
        }
}