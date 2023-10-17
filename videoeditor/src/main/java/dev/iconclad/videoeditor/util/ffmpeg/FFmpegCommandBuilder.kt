package dev.iconclad.videoeditor.util.ffmpeg

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegSessionCompleteCallback

class FFmpegCommandBuilder {
    private val command : MutableList<String> = mutableListOf()
   private fun formatCSeconds(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val secondsLeft = timeInSeconds - hours * 3600
        val minutes = secondsLeft / 60
        val seconds = secondsLeft - minutes * 60
        var formattedTime = ""
        if (hours < 10) formattedTime += "0"
        formattedTime += "$hours:"
        if (minutes < 10) formattedTime += "0"
        formattedTime += "$minutes:"
        if (seconds < 10) formattedTime += "0"
        formattedTime += seconds
        return formattedTime
    }

    private  fun formatMilliseconds(timeInMilliseconds: Long): String {
        val seconds = timeInMilliseconds / 1000
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)

    }

    // Giriş dosyasını ayarlamak için kullanılır.
    fun setInputPath(inputPath: String): FFmpegCommandBuilder {
        command.add("-i")
        command.add(inputPath)
        return this
    }

    // Çıkış dosyasını ayarlamak için kullanılır.
    fun setOutputPath(outputPath: String): FFmpegCommandBuilder {
        command.add("-c")
        command.add("copy")
        command.add(outputPath)
        return this
    }

    // Video kodeğini ayarlamak için kullanılır.
    fun setVideoCodec(videoCodec: String): FFmpegCommandBuilder {
        command.add("-c:v")
        command.add(videoCodec)
        return this
    }

    // Ses kodeğini ayarlamak için kullanılır.
    fun setAudioCodec(audioCodec: String): FFmpegCommandBuilder {
        command.add("-c:a")
        command.add(audioCodec)
        return this
    }

    // Video bit hızını ayarlamak için kullanılır.
    fun setBitrate(bitrate: String): FFmpegCommandBuilder {
        command.add("-b:v")
        command.add(bitrate)
        return this
    }

    // Video filtresini ayarlamak için kullanılır.
    fun setFilter(filter: String): FFmpegCommandBuilder {
        command.add("-vf")
        command.add("\"$filter\"")
        return this
    }

    // Ses bit hızını ayarlamak için kullanılır.
    fun setAudioBitrate(audioBitrate: String): FFmpegCommandBuilder {
        command.add("-b:a")
        command.add(audioBitrate)
        return this
    }

    // Ses örnekleme hızını ayarlamak için kullanılır.
    fun setAudioSampleRate(sampleRate: String): FFmpegCommandBuilder {
        command.add("-ar")
        command.add(sampleRate)
        return this
    }

    // Video süresini milisaniye cinsinden ayarlamak için kullanılır.
    fun setDuration(durationMillis: Long): FFmpegCommandBuilder {

        command.add("-t")
        command.add(formatMilliseconds(durationMillis))
        return this
    }

    // Video başlama zamanını milisaniye cinsinden ayarlamak için kullanılır.
    fun setStartTime(startTimeMillis: Long): FFmpegCommandBuilder {
        command.add("-ss")
        command.add(formatMilliseconds(startTimeMillis))
        return this
    }


    // Hızlı kodlama için bir ön ayar belirlemek için kullanılır.
    fun setPreset(preset: String): FFmpegCommandBuilder {
        command.add("-preset")
        command.add(preset)
        return this
    }

    // Çıkış dosyasını üzerine yazmak için kullanılır.
    fun setOverwriteOutput(): FFmpegCommandBuilder {
        command.add("-y")
        return this
    }

    fun setColorFilter(fFmpegColorBuilder: FFmpegColorBuilder){
        command.addAll(fFmpegColorBuilder.build())
    }

    // Ses kaldırma işlemini yapmak için kullanılır.
    fun removeAudio(inputVideo: String, outputVideo: String) {
        command.add("-i")
        command.add(inputVideo)
        command.add("-an")
        command.add(outputVideo)
    }

    // FFmpeg komutunu oluşturmak için kullanılır.
    fun build(): Array<String> {
        return command.toTypedArray()
    }

    // FFmpeg komutunu çalıştırmak ve çıktıları işlemek için kullanılır.
    fun execute(callback: FFmpegSessionCompleteCallback) {
        android.util.Log.i("FFmpegBuilder",command.joinToString())
        FFmpegKit.executeWithArgumentsAsync(build()) {
            callback.apply(it)
        }

    }
}
