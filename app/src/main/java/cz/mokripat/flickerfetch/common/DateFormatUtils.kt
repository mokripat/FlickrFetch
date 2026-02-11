package cz.mokripat.flickerfetch.common

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

/**
 * Utility class for date formatting manually handles potentially varied
 * date string formats from the Flickr API.
 */
object DateFormatUtils {

    // Using ISO_OFFSET_DATE_TIME to handle formats like "2025-04-16T11:24:13-08:00"
    private val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    // Target format example: "16 Apr 2025 11:24"
    private val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH)

    /**
     * Formats a date string from the Flickr API response.
     *
     * @param dateString The date string from the API (e.g., "2025-04-16T11:24:13-08:00")
     * @return Formatted date string (e.g., "16 Apr 2025 11:24"), or the original string if parsing fails.
     */
    fun formatDate(dateString: String): String {
        return try {
            val dateTime = ZonedDateTime.parse(dateString, inputFormatter)
            dateTime.format(outputFormatter)
        } catch (_: DateTimeParseException) {
            // Fallback to original string or try simpler ISO format if needed
            // For resilience, returning original string is often safer than crashing or showing empty
            dateString
        } catch (_: Exception) {
            dateString
        }
    }
}
