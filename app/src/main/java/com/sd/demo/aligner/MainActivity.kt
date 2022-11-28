package com.sd.demo.aligner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.aligner.databinding.ActivityMainBinding
import com.sd.lib.aligner.Aligner
import com.sd.lib.aligner.view.FViewAligner
import com.sd.lib.aligner.view.ViewAligner

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    private val _aligner by lazy {
        FViewAligner().apply {
            // 设置源View
            this.source = _binding.viewSource

            // 设置目标View
            this.target = _binding.viewTarget

            // 设置回调对象
            this.callback = object : ViewAligner.ViewCallback() {
                override fun onUpdate(x: Int, y: Int, source: Aligner.SourceLayoutInfo, target: Aligner.LayoutInfo) {
                    logMsg { "($x, $y)" }
                    source as ViewAligner.ViewLayoutInfo
                    source.view!!.let { view ->
                        view.layout(x, y, x + view.measuredWidth, y + view.measuredHeight)
                    }
                }
            }
        }
    }

    private val _onPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        _aligner.update()
        true
    }

    override fun onClick(v: View) {
        when (v) {
            _binding.btnStart -> {
                // 开始
                with(window.decorView.viewTreeObserver) {
                    removeOnPreDrawListener(_onPreDrawListener)
                    addOnPreDrawListener(_onPreDrawListener)
                }
            }
            _binding.btnStop -> {
                // 停止
                with(window.decorView.viewTreeObserver) {
                    removeOnPreDrawListener(_onPreDrawListener)
                }
            }

            _binding.btnTopLeft -> _aligner.position = Aligner.Position.TopStart
            _binding.btnTopCenter -> _aligner.position = Aligner.Position.TopCenter
            _binding.btnTopRight -> _aligner.position = Aligner.Position.TopEnd

            _binding.btnCenterLeft -> _aligner.position = Aligner.Position.CenterStart
            _binding.btnCenter -> _aligner.position = Aligner.Position.Center
            _binding.btnCenterRight -> _aligner.position = Aligner.Position.CenterEnd

            _binding.btnBottomLeft -> _aligner.position = Aligner.Position.BottomStart
            _binding.btnBottomCenter -> _aligner.position = Aligner.Position.BottomCenter
            _binding.btnBottomRight -> _aligner.position = Aligner.Position.BottomEnd
        }
    }
}

inline fun logMsg(block: () -> String) {
    Log.i("aligner-demo", block())
}