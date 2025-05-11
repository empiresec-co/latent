package co.empiresec.latenet.utils

object TimeUtils {
    const val ONE_MINUTE = 60
    const val FIVE_MINUTES = 5 * ONE_MINUTE
    const val FIFTEEN_MINUTES = 15 * ONE_MINUTE
    const val ONE_HOUR = 60 * ONE_MINUTE
    const val EIGHT_HOURS = 8 * ONE_HOUR
    const val ONE_DAY = 24 * ONE_HOUR
    const val ONE_WEEK = 7 * ONE_DAY
    const val ONE_MONTH = 30 * ONE_DAY
    const val ONE_YEAR = 365 * ONE_DAY

    fun now() = System.currentTimeMillis() / 1000

    fun oneMinuteFromNow() = now() + ONE_MINUTE

    fun oneMinuteAgo() = now() - ONE_MINUTE

    fun fiveMinutesAgo() = now() - FIVE_MINUTES

    fun fifteenMinutesAgo() = now() - FIFTEEN_MINUTES

    fun oneHourAgo() = now() - ONE_HOUR

    fun oneHourAhead() = now() + ONE_HOUR

    fun oneDayAgo() = now() - ONE_DAY

    fun eightHoursAgo() = now() - EIGHT_HOURS

    fun twoDays() = ONE_DAY * 2

    fun oneWeekAgo() = now() - ONE_WEEK

    fun oneMonthAgo() = now() - ONE_MONTH

    fun randomWithTwoDays() = now() - RandomInstance.int(twoDays())
}
