package com.sd.lib.aligner

fun Aligner.Input.toResult(): Aligner.Result {
    var x = 0
    var y = 0

    when (this.position) {
        Aligner.Position.TopStart -> {
            y = getYAlignTop() - this.sourceHeight
            x = getXAlignStart()
        }

        Aligner.Position.TopCenter -> {
            y = getYAlignTop() - this.sourceHeight
            x = getXAlignCenter()
        }

        Aligner.Position.TopEnd -> {
            y = getYAlignTop() - this.sourceHeight
            x = getXAlignEnd()
        }

        Aligner.Position.Top -> {
            y = getYAlignTop() - this.sourceHeight
            x = 0
        }

        Aligner.Position.BottomStart -> {
            y = getYAlignBottom() + this.sourceHeight
            x = getXAlignStart()
        }

        Aligner.Position.BottomCenter -> {
            y = getYAlignBottom() + this.sourceHeight
            x = getXAlignCenter()
        }

        Aligner.Position.BottomEnd -> {
            y = getYAlignBottom() + this.sourceHeight
            x = getXAlignEnd()
        }

        Aligner.Position.Bottom -> {
            y = getYAlignBottom() + this.sourceHeight
            x = 0
        }

        Aligner.Position.StartTop -> {
            x = getXAlignStart() - this.sourceWidth
            y = getYAlignTop()
        }

        Aligner.Position.StartCenter -> {
            x = getXAlignStart() - this.sourceWidth
            y = getYAlignCenter()
        }

        Aligner.Position.StartBottom -> {
            x = getXAlignStart() - this.sourceWidth
            y = getYAlignBottom()
        }

        Aligner.Position.Start -> {
            x = getXAlignStart() - this.sourceWidth
            y = 0
        }

        Aligner.Position.EndTop -> {
            x = getXAlignEnd() + this.sourceWidth
            y = getYAlignTop()
        }

        Aligner.Position.EndCenter -> {
            x = getXAlignEnd() + this.sourceWidth
            y = getYAlignCenter()
        }

        Aligner.Position.EndBottom -> {
            x = getXAlignEnd() + this.sourceWidth
            y = getYAlignBottom()
        }

        Aligner.Position.End -> {
            x = getXAlignEnd() + this.sourceWidth
            y = 0
        }

        Aligner.Position.Center -> {
            x = getXAlignCenter()
            y = getYAlignCenter()
        }
    }

    return Aligner.Result(this, x, y)
}

private fun Aligner.Input.getXAlignStart(): Int {
    return this.targetX - this.containerX
}

private fun Aligner.Input.getXAlignEnd(): Int {
    return getXAlignStart() + (this.targetWidth - this.sourceWidth)
}

private fun Aligner.Input.getXAlignCenter(): Int {
    return getXAlignStart() + (this.targetWidth - this.sourceWidth) / 2
}

private fun Aligner.Input.getYAlignTop(): Int {
    return this.targetY - this.containerY
}

private fun Aligner.Input.getYAlignBottom(): Int {
    return getYAlignTop() + (this.targetHeight - this.sourceHeight)
}

private fun Aligner.Input.getYAlignCenter(): Int {
    return getYAlignTop() + (this.targetHeight - this.sourceHeight) / 2
}