package com.sd.lib.aligner.view

import android.view.View
import com.sd.lib.aligner.Aligner
import com.sd.lib.aligner.Aligner.*

interface ViewAligner : Aligner {
    /**
     * 源View
     */
    var source: View?

    /**
     * 目标View
     */
    var target: View?


    interface ViewLayoutInfo : LayoutInfo {
        var view: View?
    }

    abstract class ViewCallback : Callback() {
        override fun canUpdate(source: SourceLayoutInfo, target: LayoutInfo): Boolean {
            if (source is ViewLayoutInfo && target is ViewLayoutInfo) {
                if (source.view == null) return false
                if (target.view == null) return false
            } else if (source is ViewLayoutInfo) {
                if (source.view == null) return false
            } else if (target is ViewLayoutInfo) {
                if (target.view == null) return false
            }
            return true
        }
    }
}