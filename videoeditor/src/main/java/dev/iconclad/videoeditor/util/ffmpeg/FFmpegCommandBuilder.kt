package dev.iconclad.videoeditor.util.ffmpeg

import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.Executors


class FFmpegCommandBuilder(private val ffmpegPath: String) {
    private val command = mutableListOf(ffmpegPath)
    private val durationFormat = SimpleDateFormat("HH:mm:ss")

    // Giriş dosyasını ayarlamak için kullanılır.
    fun setInputPath(inputPath: String): FFmpegCommandBuilder {
        command.add("-i")
        command.add(inputPath)
        return this
    }

    // Çıkış dosyasını ayarlamak için kullanılır.
    fun setOutputPath(outputPath: String): FFmpegCommandBuilder {
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
        command.add(filter)
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
        val duration = durationFormat.format(Date(durationMillis))
        command.add("-t")
        command.add(duration)
        return this
    }

    // Video başlama zamanını milisaniye cinsinden ayarlamak için kullanılır.
    fun setStartTime(startTimeMillis: Long): FFmpegCommandBuilder {
        val startTime = durationFormat.format(Date(startTimeMillis))
        command.add("-ss")
        command.add(startTime)
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

    // Ses kaldırma işlemini yapmak için kullanılır.
    fun removeAudio(inputVideo: String, outputVideo: String) {
        command.add("-i")
        command.add(inputVideo)
        command.add("-an")
        command.add(outputVideo)
    }

    // FFmpeg komutunu oluşturmak için kullanılır.
    fun build(): List<String> {
        return command
    }

    // FFmpeg komutunu çalıştırmak ve çıktıları işlemek için kullanılır.
    fun execute(callback: (String) -> Unit) {
        val commandArray = command.toTypedArray()
        val processBuilder = ProcessBuilder(*commandArray)

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val process = processBuilder.start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    callback(line ?: "")
                }
                process.waitFor()
                process.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
