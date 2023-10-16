package dev.iconclad.videoeditor.trimmer

class TrimmerUtils {
    companion object {
        fun formatCSeconds(timeInSeconds: Long): String {
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

        fun formatMilliseconds(timeInMilliseconds: Long): String {
            val seconds = timeInMilliseconds / 1000
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60

            return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)

        }
    }
}
