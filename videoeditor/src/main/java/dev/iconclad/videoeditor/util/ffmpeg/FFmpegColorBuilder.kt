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

/*
*  FilterModel("Sepia",  floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )*/

data class FilterModel(val name:String,val filter: FloatArray? = null,val colorchannelmixer:String? = null)

val efectList: MutableList<FilterModel> = mutableListOf(
    FilterModel("Filter Yok"),
    FilterModel("Sepia",  floatArrayOf(
        0.393f, 0.769f, 0.189f, 0f, 0f,
        0.349f, 0.686f, 0.168f, 0f, 0f,
        0.272f, 0.534f, 0.131f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
        ),"0.393:0.769:0.189:0.349:0.686:0.168:0.272:0.534:0.131"
 ),
    FilterModel("Negatif",  floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        ),"1:0:0:0:0:.5:0:0:0:0:0"
    ),
    FilterModel("Siyah-Beyaz",
        floatArrayOf(
            33f, 33f, 33f, 33f, 255f,
            33f, 33f, 33f, 3f, 255f,
            33f, 33f, 33f, 33f, 255f,
            33f, 33f, 33f, 33f, 0f
        ),"0.33:0.33:0.33:0.33:0.33:0.33:0.33:0.33:0.33"
        ),
    FilterModel("Aynı Tonlar",  floatArrayOf(
            1f, 0.5f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ),
    FilterModel("Canlandırma",   floatArrayOf(
            1.4f, 0f, 0f, 0f, 0f,
            0f, 1.4f, 0f, 0f, 0f,
            0f, 0f, 1.4f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ),
    FilterModel("Soluklaştırma",  floatArrayOf(
            0.5f, 0f, 0f, 0f, 0f,
            0f, 0.5f, 0f, 0f, 0f,
            0f, 0f, 0.5f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ),
    FilterModel("Tek Renk (Kırmızı)", floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    ),"1.5:0:0:0:1.5:0:0:0:0.5"),
    FilterModel("Soğuk Mavi Tonu",   floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1.4f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ),
    FilterModel("Sıcak Sarı Tonu",  floatArrayOf(
            1.4f, 0f, 0f, 0f, 0f,
            0f, 1.4f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ),
    FilterModel("Yüksek Kontrast",    floatArrayOf(
            2f, 0f, 0f, 0f, -255f,
            0f, 2f, 0f, 0f, -255f,
            0f, 0f, 2f, 0f, -255f,
            0f, 0f, 0f, 1f, 0f
        ),"1.5:0:0:0:1.5:0:0:0:1.5"
    )
)
