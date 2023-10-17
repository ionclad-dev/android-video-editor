package dev.iconclad.videoeditor.util.ffmpeg

import android.graphics.ColorMatrix

class FFmpegColorBuilder() {
    private val command : MutableList<String> = mutableListOf()


    fun setBrightness(brightness: Float): FFmpegColorBuilder {
        command.add("-vf")
        command.add("eq=brightness=${brightness}")
        return this
    }

    fun setContrast(contrast: Float): FFmpegColorBuilder {
        command.add("-vf")
        command.add("eq=contrast=${contrast}")
        return this
    }

    fun setSaturation(saturation: Float): FFmpegColorBuilder {
        command.add("-vf")
        command.add("eq=saturation=${saturation}")
        return this
    }

    fun setGamma(gamma: Float): FFmpegColorBuilder {
        command.add("-vf")
        command.add("eq=gamma=${gamma}")
        return this
    }


    fun build(): Array<String> {
        return command.toTypedArray()
    }

}

data class FilterModel(val name:String,val videoFilter:String,val imageFilter: ColorMatrix)

val efectList: MutableList<FilterModel> = mutableListOf(
    FilterModel("Sepia", "colorchannelmixer=0.393:0.769:0.189:0.349:0.686:0.168:0.272:0.534:0.131", ColorMatrix(
        floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )),
    FilterModel("Negatif", "negate", ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        )
    )),
    FilterModel("Siyah-Beyaz", "colorchannelmixer=0.33:0.33:0.33:0.33:0.33:0.33:0.33:0.33:0.33", ColorMatrix().apply {
        setSaturation(0f)
    }),
    FilterModel("Aynı Tonlar", "lutyuv='u=128:v=128'", ColorMatrix(
        floatArrayOf(
            1f, 0.5f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )),
    FilterModel("Canlandırma", "eq=saturation=2", ColorMatrix(
        floatArrayOf(
            1.4f, 0f, 0f, 0f, 0f,
            0f, 1.4f, 0f, 0f, 0f,
            0f, 0f, 1.4f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )),
    FilterModel("Soluklaştırma", "eq=saturation=0.5", ColorMatrix(
        floatArrayOf(
            0.5f, 0f, 0f, 0f, 0f,
            0f, 0.5f, 0f, 0f, 0f,
            0f, 0f, 0.5f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )),
    FilterModel("Tek Renk (Kırmızı)", "colorkey=0xRRGGBB:0.1:0.1", ColorMatrix(floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    ))),
    FilterModel("Soğuk Mavi Tonu", "colorbalance=rs=0.2:gs=0.3:bs=0.9",  ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1.4f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )),
    FilterModel("Sıcak Sarı Tonu", "colorbalance=rs=0.8:gs=0.7:bs=0.2", ColorMatrix(
        floatArrayOf(
            1.4f, 0f, 0f, 0f, 0f,
            0f, 1.4f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )),
    FilterModel("Yüksek Kontrast", "eq=contrast=2:saturation=2:brightness=-0.2", ColorMatrix(
        floatArrayOf(
            2f, 0f, 0f, 0f, -255f,
            0f, 2f, 0f, 0f, -255f,
            0f, 0f, 2f, 0f, -255f,
            0f, 0f, 0f, 1f, 0f
        )
    ))
)
